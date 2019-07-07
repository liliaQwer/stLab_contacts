package dao;

import model.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DateFormatter;
import utils.SearchCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AttachmentDAO implements DAO<Attachment> {
    private final static Logger logger = LogManager.getLogger(AttachmentDAO.class);

    public AttachmentDAO(){
    }

    @Override
    public List<Attachment> getList(Connection connection, int param) throws SQLException {
        List<Attachment> attachmentList = new ArrayList<>();
        String query = "select * from attachment where contact_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, param);
            //logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Attachment attachment = new Attachment();
                attachment.setId(rs.getInt("id"));
                attachment.setContactId(param);
                attachment.setFileName(rs.getString("file_name"));
                attachment.setComment(rs.getString("comment"));
                attachment.setUploadDate(rs.getDate("upload_date").toLocalDate());
                attachment.setUuid(rs.getString("uuid"));
                attachmentList.add(attachment);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
        return attachmentList;
    }

    @Override
    public int edit(Connection connection, Attachment o) throws SQLException {
        String updateQuery = "UPDATE attachment set file_name = ?, comment = ?, upload_date = ? where id = ?";
        try (PreparedStatement st = connection.prepareStatement(updateQuery)){
            st.setString(1, o.getFileName());
            st.setString(2, getStringOrNull(o.getComment()));
            st.setDate(3, DateFormatter.convertToDatabaseColumn(o.getUploadDate()), Calendar.getInstance());
            //st.setObject(3, o.getUploadDate(), Types.DATE);
            st.setInt(4, o.getId());
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int delete(Connection connection, int id) throws SQLException {
        String query = "delete from attachment where id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public int save(Connection connection, Attachment o) throws SQLException {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String lastIdQuery = "SELECT last_insert_id()";
        String insertQuery = "INSERT INTO attachment (contact_id, file_name, comment, upload_date, uuid) VALUES (?, ?, ?, CURDATE(), ?)";
        try (PreparedStatement insertSt = connection.prepareStatement(insertQuery);
                PreparedStatement lastIdSt = connection.prepareStatement(lastIdQuery)){
            insertSt.setInt(1, o.getContactId());
            insertSt.setString(2, o.getFileName());
            insertSt.setString(3, o.getComment());
            insertSt.setString(4,randomUUIDString);
            logger.info(insertSt.toString());
            int result = insertSt.executeUpdate();
            if (result == 1) {
                ResultSet rs = lastIdSt.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
                return result;
            }
            return 0;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }


    @Override
    public Attachment get(Connection connection, int id) throws SQLException{
        String query = "select * from attachment where id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                Attachment attachment = new Attachment();
                attachment.setId(id);
                attachment.setContactId(rs.getInt("contact_id"));
                attachment.setFileName(rs.getString("file_name"));
                attachment.setComment(rs.getString("comment"));
                attachment.setUploadDate(rs.getDate("upload_date").toLocalDate());
                attachment.setUuid(rs.getString("uuid"));
                return attachment;
            }
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public List<Attachment> getPage(Connection connection, SearchCriteria searchCriteria){
        return null;
    }

    @Override
    public int getCount(Connection connection, SearchCriteria searchCriteria){
        return 0;
    }

    @Override
    public List<Attachment> getList(Connection connection){
        return null;
    }
}
