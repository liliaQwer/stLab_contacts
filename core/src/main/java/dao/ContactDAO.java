package dao;

import model.Contact;
import org.apache.logging.log4j.LogManager;
import utils.ApplicationException;
import utils.ContactStatus;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import utils.SearchCriteria;

@FunctionalInterface
interface Consumer<X, Y, Z> {
    void apply(X x, Y y, Z z) throws SQLException;
}

class MapData {
    public MapData(Object value, Consumer<PreparedStatement, Integer, Object> consumer) {
        this.value = value;
        this.consumer = consumer;
    }

    public Object value;
    public Consumer<PreparedStatement, Integer, Object> consumer;
}

public class ContactDAO implements DAO<Contact> {
    private DataSource dataSource;
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

    public ContactDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Contact get(int id) throws ApplicationException {
        String query = "select * from contact where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new ApplicationException();
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
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public List<Contact> getPage(SearchCriteria searchCriteria) throws ApplicationException {
        List<Contact> contactList = new ArrayList<>();
        String query = "select * from contact c left join address a on c.id=a.contact_id  where ";
        Connection connection = null;
        PreparedStatement st = null;

        try {
            connection = dataSource.getConnection();
            st = prepareSelectQueryAndStatemant(query, searchCriteria, connection, false);
                /*
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            st.setInt(2, pageSize * (pageNumber - 1));
            st.setInt(3, pageSize);

                 */
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
            connection.close();
        } catch (Exception e) {
            if (st != null){
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            e.printStackTrace();
            throw new ApplicationException();
        }
        return contactList == null ? Collections.emptyList() : contactList;
    }

    private PreparedStatement prepareSelectQueryAndStatemant(String query, SearchCriteria searchCriteria, Connection con,
                                                             boolean isTotalCountQuery) throws SQLException, IllegalAccessException {
        StringBuilder sbQuery = new StringBuilder(query);//"select * from contact where active_status = ? limit ?,?"
        Field[] allFields = SearchCriteria.class.getDeclaredFields();
        // List<String> fieldNames = Arrays.stream(allFields).map(field->field.getName()).collect(Collectors.toList());
        AtomicInteger index = new AtomicInteger();
        Map<Integer, MapData> mapData = new HashMap<>();
        Arrays.stream(allFields).filter(field -> {
            field.setAccessible(true);
            try {
                if (field.get(searchCriteria) != null && fieldsDictionary.containsKey(field.getName())) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }).forEach(field -> {
            try {
                sbQuery.append(fieldsDictionary.get(field.getName())).append("=? and ");

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
                            statement.setObject(idx, value)
                    ));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        if (isTotalCountQuery){
            sbQuery.append(" active_status = ?");
        }else{
            sbQuery.append(" active_status = ? limit ?,?");
        }
        System.out.println("sbQuery.toString = " + sbQuery.toString());
        PreparedStatement st = con.prepareStatement(sbQuery.toString());
        for (Map.Entry<Integer, MapData> cursor : mapData.entrySet()) {
            Integer idx = cursor.getKey();
            MapData mData = cursor.getValue();
            mData.consumer.apply(st, idx, mData.value);
        }
        st.setString(index.incrementAndGet(), String.valueOf(ContactStatus.ACTIVATED.getStatus()));
        if (!isTotalCountQuery){
            st.setInt(index.incrementAndGet(), searchCriteria.getPageSize() * (searchCriteria.getPageNumber() - 1));
            st.setInt(index.incrementAndGet(), searchCriteria.getPageSize());
        }
        return st;
    }



    @Override
    public int getCount(SearchCriteria searchCriteria) throws ApplicationException {
        String query = "select count(*) amount from contact c left join address a on c.id=a.contact_id  where ";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = dataSource.getConnection();
            st = prepareSelectQueryAndStatemant(query, searchCriteria, connection, true);

            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            rs.next();
            int result = rs.getInt("amount");
            st.close();
            connection.close();
            return result;
        } catch (Exception e) {
            if (st != null){
                try {
                    st.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            e.printStackTrace();
            throw new ApplicationException();
        }
    }

    @Override
    public List<Contact> getList() throws ApplicationException {
        ArrayList<Contact> contactList = new ArrayList<>();
        String query = "select * from contact where active_status = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
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
                contact.setSite(rs.getString("site"));
                contact.setEmail(rs.getString("email"));
                contact.setGender(rs.getInt("gender_id"));
                contact.setMaritalStatus(rs.getInt("marital_status_id"));
                contact.setNationality(rs.getString("nationality"));
                contact.setProfilePhoto(rs.getString("profile_photo"));
                contactList.add(contact);
            }
            return contactList;
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public List<Contact> getList(int param) throws ApplicationException {
        return null;
    }

    @Override
    public int edit(Contact o) throws ApplicationException {
        String updateQuery = "UPDATE contact SET name = ?, patronymic = ?, surname = ?, birthday = ?, company = ?, " +
                "nationality = ?, marital_status_id = ?, site = ?, email = ?, gender_id = ?, profile_photo = ? WHERE (id = ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertSt = connection.prepareStatement(updateQuery)
        ) {
            insertSt.setString(1, o.getName());
            insertSt.setString(2, getStringOrNull(o.getPatronymic()));
            insertSt.setString(3, o.getSurname());
            insertSt.setObject(4, o.getBirthday(), Types.DATE);
            insertSt.setString(5, getStringOrNull(o.getCompany()));
            insertSt.setString(6, getStringOrNull(o.getNationality()));
            insertSt.setObject(7, o.getMaritalStatus(), Types.INTEGER);
            insertSt.setObject(8, getStringOrNull(o.getSite()));
            insertSt.setString(9, getStringOrNull(o.getEmail()));
            insertSt.setObject(10, o.getGender(), Types.INTEGER);
            insertSt.setString(11, getStringOrNull(o.getProfilePhoto()));
            insertSt.setInt(12, o.getId());
            logger.info(insertSt.toString());
            return insertSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public int delete(int id) throws ApplicationException {
        String query = "update contact set active_status = ? where id = ?";
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, String.valueOf(ContactStatus.DEACTIVATED.getStatus()));
            st.setInt(2, id);
            logger.info(st.toString());
            result = st.executeUpdate();
            System.out.println("result " + result);
            return result;
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public int save(Contact o) throws ApplicationException {
        String insertQuery = "insert into contact(name, patronymic, surname, birthday, company, nationality, marital_status_id," +
                " email, gender_id, site, profile_photo, active_status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastIdQuery = "SELECT last_insert_id()";
        int result;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertSt = connection.prepareStatement(insertQuery);
             PreparedStatement lastIdSt = connection.prepareStatement(lastIdQuery)
        ) {
            insertSt.setString(1, o.getName());
            insertSt.setString(2, getStringOrNull(o.getPatronymic()));
            insertSt.setString(3, o.getSurname());
            insertSt.setObject(4, o.getBirthday(), Types.DATE);
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
            System.out.println("result " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new ApplicationException();
        }
    }

}
