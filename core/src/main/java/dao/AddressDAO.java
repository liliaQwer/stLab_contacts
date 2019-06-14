package dao;

import model.AddressModel;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class AddressDAO implements DAO<AddressModel>{
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger(AddressDAO.class);
    //private Connection conn;

    public AddressDAO(DataSource dataSource) throws ApplicationException {
        this.dataSource = dataSource;
        //conn = dataSource.getConnection();
    }
    @Override
    public void beginTransaction() throws ApplicationException {

    }

    @Override
    public void rollback() throws ApplicationException {

    }

    @Override
    public void commit() throws ApplicationException {

    }

    @Override
    public Model getModelById(int id) throws ApplicationException {
        AddressModel model = new AddressModel();
        String query = "select * from address where contact_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                model.setId(id);
                model.setCity(rs.getString("city"));
                model.setCountry(rs.getString("country"));
                model.setIndex(rs.getString("index"));
                model.setStreet(rs.getString("street"));
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return model;
    }

    @Override
    public List<? extends Model> getModelListPage(int pageNumber, int pageSize) throws ApplicationException {
        return null;
    }

    @Override
    public int getModelListCount() throws ApplicationException {
        return 0;
    }

    @Override
    public List<Model> getModelList() {
        return null;
    }

    @Override
    public void edit(AddressModel o) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void save(AddressModel o) {

    }
}
