package dao;

import model.Address;
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
        return null;
    }

    @Override
    public List<Contact> getPage(int pageNumber, int pageSize) throws ApplicationException {
        List<Contact> modelList = new ArrayList<>();
        String query = "select * from contact where active_status = ? limit ?,?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            st.setInt(2, pageSize * (pageNumber - 1));
            st.setInt(3, pageSize);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Contact model = new Contact();
                model.setId(rs.getInt("id"));
                model.setName(rs.getString("name"));
                model.setSurName(rs.getString("surname"));
                model.setLastName(rs.getString("patronymic"));
                model.setBirthDay(rs.getDate("birthday"));
                model.setCompany(rs.getString("company"));
                modelList.add(model);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return modelList == null ? Collections.emptyList() : modelList;
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
        return 0;
    }

}
