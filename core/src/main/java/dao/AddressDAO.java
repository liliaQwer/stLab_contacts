package dao;

import model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;
import utils.SearchCriteria;

import java.sql.*;
import java.util.List;

public class AddressDAO implements DAO<Address>{
    private final static Logger logger = LogManager.getLogger(AddressDAO.class);

    public AddressDAO(){
    }

    @Override
    public Address get(Connection connection, int id) throws SQLException {
        Address address = new Address();
        String query = "select * from address where contact_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            //logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                address.setContactId(id);
                address.setCity(rs.getString("city"));
                address.setCountry(rs.getString("country"));
                address.setPostalCode(rs.getString("postal_code"));
                address.setStreet(rs.getString("street"));
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
        return address;
    }

    @Override
    public int edit(Connection connection, Address o) throws SQLException {
        String countQuery = "select count(*) from address where contact_id = ?";
        String updateQuery = "UPDATE address SET country = ?, city = ?, street = ?, postal_code = ? WHERE (contact_id = ?)";
        try (PreparedStatement countSt = connection.prepareStatement(countQuery);
             PreparedStatement updateSt = connection.prepareStatement(updateQuery)){
            countSt.setInt(1, o.getContactId());
            //logger.info(countSt.toString());
            ResultSet countRs = countSt.executeQuery();
            countRs.next();
            if (countRs.getInt(1) == 0){
                if (!o.isEmpty()){
                    return save(connection, o);
                } else{
                    return 0;
                }
            }
            updateSt.setInt(5, o.getContactId());
            updateSt.setString(1, getStringOrNull(o.getCountry()));
            updateSt.setString(2, getStringOrNull(o.getCity()));
            updateSt.setString(3, getStringOrNull(o.getStreet()));
            updateSt.setString(4, getStringOrNull(o.getPostalCode()));
            logger.info(updateSt.toString());
            return updateSt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int save(Connection connection, Address o) throws SQLException{
        int result;
        String insertQuery = "INSERT INTO address (contact_id, country, city, street, postal_code) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(insertQuery)){
            st.setInt(1, o.getContactId());
            st.setString(2, getStringOrNull(o.getCountry()));
            st.setString(3, getStringOrNull(o.getCity()));
            st.setString(4, getStringOrNull(o.getStreet()));
            st.setString(5, getStringOrNull(o.getPostalCode()));
            logger.info(st.toString());
            result = st.executeUpdate();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int delete(Connection conn, int id) {
        return 0;
    }


    @Override
    public List<Address> getPage(Connection conn, SearchCriteria searchCriteria){
        return null;
    }

    @Override
    public int getCount(Connection conn, SearchCriteria searchCriteria){
        return 0;
    }

    @Override
    public List<Address> getList(Connection conn) {
        return null;
    }

    @Override
    public List<Address> getList(Connection conn, int param){
        return null;
    }
}
