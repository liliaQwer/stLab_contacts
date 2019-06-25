package dao;

import model.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO implements DAO<Attachment> {
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger(AttachmentDAO.class);

    public AttachmentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Attachment get(int id) throws ApplicationException {
        return null;
    }

    @Override
    public List<Attachment> getPage(int pageNumber, int pageSize) throws ApplicationException {
        return null;
    }

    @Override
    public int getCount() throws ApplicationException {
        return 0;
    }

    @Override
    public List<Attachment> getList() throws ApplicationException {
        return null;
    }

    @Override
    public List<Attachment> getList(int param) throws ApplicationException {
        List<Attachment> attachmentList = new ArrayList<>();
        String query = "select * from attachment where contact_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, param);
            logger.info(st.toString());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Attachment attachment = new Attachment();
                attachment.setId(rs.getInt("id"));
                attachment.setContactId(param);
                attachment.setFileName(rs.getString("file_name"));
                attachment.setComment(rs.getString("comment"));
                attachment.setUploadDate(rs.getDate("upload_date").toLocalDate());
                attachmentList.add(attachment);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return attachmentList;
    }

    @Override
    public int edit(Attachment o) throws ApplicationException {
        String updateQuery = "UPDATE attachment set file_name = ?, comment = ?, upload_date = ? where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(updateQuery)){
            st.setString(1, o.getFileName());
            st.setString(2, getStringOrNull(o.getComment()));
            System.out.println("upload in edit DAO " + o.getUploadDate());
            st.setObject(3, o.getUploadDate(), Types.DATE);
            st.setInt(4, o.getId());
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public int delete(int id) throws ApplicationException {
        String query = "delete from attachment where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, id);
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public int save(Attachment o) throws ApplicationException {
        String insertQuery = "INSERT INTO attachment (contact_id, file_name, comment, upload_date) VALUES (?, ?, ?, CURDATE())";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(insertQuery)){
            st.setInt(1, o.getContactId());
            st.setString(2, o.getFileName());
            st.setString(3, o.getComment());
            logger.info(st.toString());
            return st.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

}
