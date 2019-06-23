package dao;

import model.Attachment;
import model.Phone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhoneDAO implements DAO<Phone> {
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger(PhoneDAO.class);

    public PhoneDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Phone get(int id) throws ApplicationException {
        return null;
    }

    @Override
    public List<Phone> getPage(int pageNumber, int pageSize) throws ApplicationException {
        return null;
    }

    @Override
    public int getCount() throws ApplicationException {
        return 0;
    }

    @Override
    public List<Phone> getList() throws ApplicationException {
        return null;
    }

    @Override
    public List<Phone> getList(int param) throws ApplicationException {
        List<Phone> phoneList = new ArrayList<>();
        String query = "select * from phone where contact_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, param);
            logger.info(st.toString());
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
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return phoneList;
    }

    @Override
    public int update(Phone o) throws ApplicationException {
        return 0;
    }

    @Override
    public int delete(int id) throws ApplicationException {
        return 0;
    }

    @Override
    public int save(Phone o) throws ApplicationException {
        String insertQuery = "INSERT INTO phone (contact_id, country_code, oper_code, number, type, comment) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(insertQuery)){
            st.setInt(1, o.getContactId());
            st.setInt(2, o.getCountryCode());
            st.setInt(3, o.getOperatorCode());
            st.setInt(4, o.getPhoneNumber());
            st.setInt(5, o.getPhoneType());
            st.setString(6, getStringOrNull(o.getComment()));
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

}
