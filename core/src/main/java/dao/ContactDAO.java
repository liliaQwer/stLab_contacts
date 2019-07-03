package dao;

import model.Contact;
import org.apache.logging.log4j.LogManager;
import utils.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

public class ContactDAO implements DAO<Contact> {
    private final static Logger logger = LogManager.getRootLogger();
    private static Map<String, String> fieldsDictionary;

    static {
        fieldsDictionary = new HashMap<>();
        fieldsDictionary.put("name", "name");
        fieldsDictionary.put("surname", "surname");
        fieldsDictionary.put("patronymic", "patronymic");
        fieldsDictionary.put("gender", "gender_id");
        fieldsDictionary.put("birthday", "birthday");
        fieldsDictionary.put("maritalStatus", "marital_status_id");
        fieldsDictionary.put("country", "country");
        fieldsDictionary.put("city", "city");
        fieldsDictionary.put("street", "street");
        fieldsDictionary.put("postalCode", "postal_code");
        fieldsDictionary.put("nationality", "nationality");
    }

    public ContactDAO() {

    }

    @Override
    public Contact get(Connection connection, int id) throws SQLException {
        String query = "select * from contact where id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            //logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Contact contact = new Contact();
            contact.setId(rs.getInt("id"));
            contact.setName(rs.getString("name"));
            contact.setSurname(rs.getString("surname"));
            contact.setPatronymic(rs.getString("patronymic"));
            contact.setBirthday(rs.getObject("birthday", LocalDate.class));
            contact.setCompany(rs.getString("company"));
            contact.setSite(rs.getString("site"));
            contact.setEmail(rs.getString("email"));
            contact.setGender(rs.getInt("gender_id"));
            contact.setMaritalStatus(rs.getInt("marital_status_id"));
            contact.setNationality(rs.getString("nationality"));
            contact.setProfilePhoto(rs.getString("profile_photo"));
            return contact;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public List<Contact> getPage(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        List<Contact> contactList = new ArrayList<>();
        String query = "select * from contact c left join address a on c.id=a.contact_id  where ";
        PreparedStatement st = null;
        try{
            st = prepareSelectQueryAndStatemant(query, searchCriteria, connection, false);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setSurname(rs.getString("surname"));
                contact.setPatronymic(rs.getString("patronymic"));
                contact.setBirthday(rs.getObject("birthday", LocalDate.class));
                contact.setCompany(rs.getString("company"));
                contactList.add(contact);
            }
            st.close();
        } catch (SQLException e) {
            if (st != null){
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            throw e;
        }
        return contactList;
    }

    private PreparedStatement prepareSelectQueryAndStatemant(String query, SearchCriteria searchCriteria, Connection con,
                                                             boolean isTotalCountQuery) throws SQLException {
        StringBuilder sbQuery = new StringBuilder(query);//"select * from contact where active_status = ? limit ?,?"
        Field[] allFields = SearchCriteria.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(allFields).map(field -> field.getName()).collect(Collectors.toList());
        AtomicInteger index = new AtomicInteger();
        Map<Integer, MapData> mapData = new HashMap<>();
        Arrays.stream(allFields).filter(field -> {
            field.setAccessible(true);
            try {
                if (field.get(searchCriteria) != null && (field.getName().contains("Operator") || fieldsDictionary.containsKey(field.getName()))) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }).forEach(field -> {
            try {
                //if fieldName contains 'operator', set its value to query string
                if (field.getName().contains("Operator")){
                    String columnName = field.getName().replace("Operator", "");
                    sbQuery.append(fieldsDictionary.get(columnName)).append(field.get(searchCriteria)).append("? and ");
                    return;
                }else{
                    //append field to query string  if it doesn't have appropriate operator field
                    if (!fieldNames.stream().anyMatch(name -> name.equals(field.getName() + "Operator"))) {
                        sbQuery.append(fieldsDictionary.get(field.getName())).append("=? and ");
                    }
                }
                if (field.getType() == Integer.class) {
                    mapData.put(index.incrementAndGet(), new MapData(field.get(searchCriteria), (statement, idx, value) ->
                            statement.setInt(idx, (Integer) value)
                    ));
                } else if (field.getType() == String.class) {
                    mapData.put(index.incrementAndGet(), new MapData(field.get(searchCriteria), (statement, idx, value) ->
                            statement.setString(idx, (String) value)
                    ));
                } else if (field.getType() == LocalDate.class) {
                    mapData.put(index.incrementAndGet(), new MapData(field.get(searchCriteria), (statement, idx, value) ->
                            statement.setDate(idx, DateFormatter.convertToDatabaseColumn((LocalDate)value), Calendar.getInstance())
                    ));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        if (isTotalCountQuery){
            sbQuery.append(" active_status = ?");
        }else{
            sbQuery.append(" active_status = ? order by name limit ?,?");
        }
        System.out.println("sbQuery.toString = " + sbQuery.toString());
        PreparedStatement st = con.prepareStatement(sbQuery.toString());
        for (Map.Entry<Integer, MapData> cursor : mapData.entrySet()) {
            Integer idx = cursor.getKey();
            MapData mData = cursor.getValue();
            mData.getConsumer().apply(st, idx, mData.getValue());
        }
        st.setString(index.incrementAndGet(), String.valueOf(ContactStatus.ACTIVATED.getStatus()));
        if (!isTotalCountQuery){
            st.setInt(index.incrementAndGet(), searchCriteria.getPageSize() * (searchCriteria.getPageNumber() - 1));
            st.setInt(index.incrementAndGet(), searchCriteria.getPageSize());
        }
        return st;
    }

    @Override
    public int getCount(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        String query = "select count(*) amount from contact c left join address a on c.id=a.contact_id  where ";
        PreparedStatement st = null;
        try {
            st = prepareSelectQueryAndStatemant(query, searchCriteria, connection, true);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            rs.next();
            int result = rs.getInt("amount");
            st.close();
            return result;
        } catch (SQLException e) {
            if (st != null){
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            throw e;
        }
    }

    @Override
    public List<Contact> getList(Connection connection) throws SQLException {
        ArrayList<Contact> contactList = new ArrayList<>();
        String query = "select * from contact where active_status = ? order by name";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            //logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setSurname(rs.getString("surname"));
                contact.setPatronymic(rs.getString("patronymic"));
                contact.setBirthday(rs.getObject("birthday", LocalDate.class));
                contact.setCompany(rs.getString("company"));
                contact.setSite(rs.getString("site"));
                contact.setEmail(rs.getString("email"));
                contact.setGender(rs.getInt("gender_id"));
                contact.setMaritalStatus(rs.getInt("marital_status_id"));
                contact.setNationality(rs.getString("nationality"));
                contact.setProfilePhoto(rs.getString("profile_photo"));
                contactList.add(contact);
            }
            st.close();
            return contactList;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public List<Contact> getList(Connection connection, int param){
        return null;
    }

    @Override
    public int edit(Connection connection, Contact o) throws SQLException {
        String updateQuery = "UPDATE contact SET name = ?, patronymic = ?, surname = ?, birthday = ?, company = ?, " +
                "nationality = ?, marital_status_id = ?, site = ?, email = ?, gender_id = ?, profile_photo = ? WHERE (id = ?)";
        try {
            PreparedStatement insertSt = connection.prepareStatement(updateQuery);
            insertSt.setString(1, o.getName());
            insertSt.setString(2, getStringOrNull(o.getPatronymic()));
            insertSt.setString(3, o.getSurname());
            insertSt.setDate(4, DateFormatter.convertToDatabaseColumn(o.getBirthday()), Calendar.getInstance());
            //insertSt.setObject(4, o.getBirthday(), Types.DATE);
            insertSt.setString(5, getStringOrNull(o.getCompany()));
            insertSt.setString(6, getStringOrNull(o.getNationality()));
            insertSt.setObject(7, o.getMaritalStatus(), Types.INTEGER);
            insertSt.setObject(8, getStringOrNull(o.getSite()));
            insertSt.setString(9, getStringOrNull(o.getEmail()));
            insertSt.setObject(10, o.getGender(), Types.INTEGER);
            insertSt.setString(11, getStringOrNull(o.getProfilePhoto()));
            insertSt.setInt(12, o.getId());
            logger.info(insertSt.toString());
            int result = insertSt.executeUpdate();
            insertSt.close();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int delete(Connection connection, int id) throws SQLException {
        String query = "update contact set active_status = ? where id = ?";
        int result;
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, String.valueOf(ContactStatus.DEACTIVATED.getStatus()));
            st.setInt(2, id);
            logger.info(st.toString());
            result = st.executeUpdate();
            System.out.println("result " + result);
            st.close();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int save(Connection connection, Contact o) throws SQLException {
        String insertQuery = "insert into contact(name, patronymic, surname, birthday, company, nationality, marital_status_id," +
                " email, gender_id, site, profile_photo, active_status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastIdQuery = "SELECT last_insert_id()";
        int result;
        try {
            PreparedStatement insertSt = connection.prepareStatement(insertQuery);
            PreparedStatement lastIdSt = connection.prepareStatement(lastIdQuery);
            insertSt.setString(1, o.getName());
            insertSt.setString(2, getStringOrNull(o.getPatronymic()));
            insertSt.setString(3, o.getSurname());
            insertSt.setDate(4, DateFormatter.convertToDatabaseColumn(o.getBirthday()), Calendar.getInstance());
            //insertSt.setObject(4, o.getBirthday(), Types.DATE);
            insertSt.setString(5, getStringOrNull(o.getCompany()));
            insertSt.setString(6, getStringOrNull(o.getNationality()));
            insertSt.setObject(7, o.getMaritalStatus(), Types.INTEGER);
            insertSt.setString(8, getStringOrNull(o.getEmail()));
            insertSt.setObject(9, o.getGender(), Types.INTEGER);
            insertSt.setObject(10, getStringOrNull(o.getSite()));
            insertSt.setObject(11, getStringOrNull(o.getProfilePhoto()));
            insertSt.setString(12, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            logger.info(insertSt.toString());
            result = insertSt.executeUpdate();
            if (result == 1) {
                ResultSet rs = lastIdSt.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
            insertSt.close();
            lastIdSt.close();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }
}
