package dao;

import model.Contact;
import org.apache.logging.log4j.LogManager;
import utils.ApplicationException;
import utils.ContactStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class ContactDAO implements DAO<Contact>{
    private DataSource dataSource;
    private final static Logger logger = LogManager.getRootLogger();

    public ContactDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Contact get(int id) throws ApplicationException {
        String query = "select * from contact where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
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
            contact.setBirthday(rs.getDate("birthday"));
            contact.setCompany(rs.getString("company"));
            contact.setSite(rs.getString("site"));
            contact.setEmail(rs.getString("email"));
            contact.setGender(rs.getInt("gender_id"));
            contact.setMaritalStatus(rs.getInt("marital_status_id"));
            contact.setNationality(rs.getString("nationality"));
            return contact;
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public List<Contact> getPage(int pageNumber, int pageSize) throws ApplicationException {
        List<Contact> contactList = new ArrayList<>();
        String query = "select * from contact where active_status = ? limit ?,?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            st.setInt(2, pageSize * (pageNumber - 1));
            st.setInt(3, pageSize);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setSurname(rs.getString("surname"));
                contact.setPatronymic(rs.getString("patronymic"));
                contact.setBirthday(rs.getDate("birthday"));
                contact.setCompany(rs.getString("company"));
                contactList.add(contact);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return contactList == null ? Collections.emptyList() : contactList;
    }

    @Override
    public int getCount() throws ApplicationException {
        String query = "select count(*) amount from contact where active_status = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt("amount");
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public List<Contact> getList() throws ApplicationException {
        return null;
    }

    @Override
    public List<Contact> getList(int param) throws ApplicationException {
        return null;
    }

    @Override
    public int update(Contact o) throws ApplicationException {
        return 0;
    }

    @Override
    public int delete(int id) throws ApplicationException {
        String query = "update contact set active_status = ? where id = ?";
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
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
                " email, gender_id, active_status) values(?,?,?,?,?,?,?,?,?,?)";
        String lastIdQuery = "SELECT last_insert_id()";
        int result;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertSt = connection.prepareStatement(insertQuery);
             PreparedStatement lastIdSt = connection.prepareStatement(lastIdQuery)
             ){
            insertSt.setString(1, o.getName());
            insertSt.setString(2, getStringOrNull(o.getPatronymic()));
            insertSt.setString(3, o.getSurname());
            insertSt.setDate(4, o.getBirthday());
            insertSt.setString(5, getStringOrNull(o.getCompany()));
            insertSt.setString(6, getStringOrNull(o.getNationality()));
            insertSt.setObject(7, o.getMaritalStatus(), Types.INTEGER);
            insertSt.setString(8, getStringOrNull(o.getEmail()));
            insertSt.setObject(9, o.getGender(), Types.INTEGER);
            insertSt.setString(10, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            logger.info(insertSt.toString());
            result = insertSt.executeUpdate();
            if (result == 1){
                ResultSet rs = lastIdSt.executeQuery();
                if (rs.next()){
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
