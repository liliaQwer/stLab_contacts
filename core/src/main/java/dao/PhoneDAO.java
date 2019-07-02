package dao;

import model.Phone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;
import utils.SearchCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneDAO implements DAO<Phone> {
    private final static Logger logger = LogManager.getLogger(PhoneDAO.class);

    public PhoneDAO() {

    }

    @Override
    public List<Phone> getList(Connection connection, int param) throws SQLException {
        List<Phone> phoneList = new ArrayList<>();
        String query = "select * from phone where contact_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, param);
            //ogger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Phone phone = new Phone();
                phone.setId(rs.getInt("id"));
                phone.setContactId(param);
                phone.setCountryCode(rs.getInt("country_code"));
                phone.setOperatorCode(rs.getInt("oper_code"));
                phone.setPhoneNumber(rs.getInt("number"));
                phone.setPhoneType(rs.getInt("type"));
                phone.setComment(rs.getString("comment"));
                phoneList.add(phone);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
        return phoneList;
    }

    @Override
    public int edit(Connection connection, Phone o) throws SQLException {
        String insertQuery = "update phone set id=?, country_code=?, oper_code=?, number=?, type=?, comment=? where contact_id=?";
        try (PreparedStatement st = connection.prepareStatement(insertQuery)){
            st.setInt(1, o.getId());
            st.setObject(2, o.getCountryCode(), Types.INTEGER);
            st.setObject(3, o.getOperatorCode(), Types.INTEGER);
            st.setObject(4, o.getPhoneNumber(), Types.INTEGER);
            st.setObject(5, o.getPhoneType(), Types.INTEGER);
            st.setString(6, getStringOrNull(o.getComment()));
            st.setInt(7, o.getContactId());
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int delete(Connection connection, int id) throws SQLException {
        String query = "delete from phone where id =?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int save(Connection connection, Phone o) throws SQLException {
        String insertQuery = "INSERT INTO phone (contact_id, country_code, oper_code, number, type, comment) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(insertQuery)){
            st.setInt(1, o.getContactId());
            st.setObject(2, o.getCountryCode(), Types.INTEGER);
            st.setObject(3, o.getOperatorCode(), Types.INTEGER);
            st.setObject(4, o.getPhoneNumber(), Types.INTEGER);
            st.setObject(5, o.getPhoneType(), Types.INTEGER);
            st.setString(6, getStringOrNull(o.getComment()));
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Phone get(Connection connection, int id){
        return null;
    }

    @Override
    public List<Phone> getPage(Connection connection, SearchCriteria searchCriteria){
        return null;
    }

    @Override
    public int getCount(Connection connection, SearchCriteria searchCriteria){
        return 0;
    }

    @Override
    public List<Phone> getList(Connection connection){
        return null;
    }

}
