package dao;

import model.AddressModel;
import model.ContactModel;
import model.Model;
import org.apache.logging.log4j.LogManager;
import utils.ApplicationException;
import utils.ContactStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class ContactDAO implements DAO<ContactModel>{
    private DataSource dataSource;
    private final static Logger logger = LogManager.getRootLogger();

    public ContactDAO(DataSource dataSource) throws ApplicationException {
        this.dataSource = dataSource;
        //conn = dataSource.getConnection();
    }

    @Override
    public void beginTransaction() throws ApplicationException {
        //conn.setAutoCommit(false);
    }

    @Override
    public void rollback() throws ApplicationException {
        //conn.rollback();
        //conn.setAutoCommit(true);
    }

    @Override
    public void commit() throws ApplicationException {
        //conn.commit();
        //conn.setAutoCommit(true);
    }

    @Override
    public ContactModel getModelById(int id) throws ApplicationException {
        return null;
    }

    @Override
    public List<ContactModel> getModelListPage(int pageNumber, int pageSize) throws ApplicationException {
        List<ContactModel> modelList = new ArrayList<>();
        AddressDAO addressDAO = new AddressDAO(dataSource);
        String query = "select * from contact where active_status = ? limit ?,?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, String.valueOf(ContactStatus.ACTIVATED.getStatus()));
            st.setInt(2, pageSize * (pageNumber - 1));
            st.setInt(3, pageSize);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                ContactModel model = new ContactModel();
                model.setId(rs.getInt("id"));
                model.setName(rs.getString("name"));
                model.setSurName(rs.getString("surname"));
                model.setLastName(rs.getString("patronymic"));
                model.setBirthDay(rs.getDate("birthday"));
                model.setCompany(rs.getString("company"));
                Model addressModel = addressDAO.getModelById(model.getId());
                model.setAddress(addressModel != null ? (AddressModel)addressModel : null);
                modelList.add(model);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return modelList == null ? Collections.emptyList() : modelList;
    }

    @Override
    public int getModelListCount() throws ApplicationException {
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
    public List<? extends Model> getModelList() throws ApplicationException {
        return null;
    }

    @Override
    public void edit(ContactModel o) throws ApplicationException {

    }

    @Override
    public void deleteById(int id) throws ApplicationException {
        String query = "update contact set active_status = ? where id = ?";
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, String.valueOf(ContactStatus.DEACTIVATED.getStatus()));
            st.setInt(2, id);
            logger.info(st.toString());
            result = st.executeUpdate();
            System.out.println("result " + result);
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        if (result == 0) {
            throw new ApplicationException("No records to delete");
        }
    }

    @Override
    public void save(ContactModel o) throws ApplicationException {

    }

}
