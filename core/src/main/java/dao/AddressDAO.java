package dao;

import model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class AddressDAO implements DAO<Address>{
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger(AddressDAO.class);
    //private Connection conn;

    public AddressDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Address get(int id) throws ApplicationException {
        Address model = new Address();
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
                model.setPostalCode(rs.getString("postal_code"));
                model.setStreet(rs.getString("street"));
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return model;
    }

    @Override
    public List<Address> getPage(int pageNumber, int pageSize) throws ApplicationException {
        return null;
    }

    @Override
    public int getCount() throws ApplicationException {
        return 0;
    }

    @Override
    public List<Address> getList() {
        return null;
    }

    @Override
    public int update(Address o) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public int save(Address o) {
        return 0;
    }
}
