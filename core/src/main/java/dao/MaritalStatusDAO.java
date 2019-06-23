package dao;

import model.IdDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaritalStatusDAO implements DAO<IdDescription> {
    private DataSource dataSource;
    private final static Logger logger = LogManager.getRootLogger();

    public MaritalStatusDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public IdDescription get(int id) throws ApplicationException {
        return null;
    }

    @Override
    public List<IdDescription> getPage(int pageNumber, int pageSize) throws ApplicationException {
        return null;
    }

    @Override
    public int getCount() throws ApplicationException {
        return 0;
    }

    @Override
    public List<IdDescription> getList() throws ApplicationException {
        List<IdDescription> list = new ArrayList<>();
        String query = "select * from marital_status";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            logger.info(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                IdDescription maritalStatus = new IdDescription();
                maritalStatus.setId(rs.getInt("id"));
                maritalStatus.setDescription(rs.getString("description"));
                list.add(maritalStatus);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException();
        }
        return list;
    }

    @Override
    public List<IdDescription> getList(int param) throws ApplicationException {
        return null;
    }

    @Override
    public int update(IdDescription o) throws ApplicationException {
        return 0;
    }

    @Override
    public int delete(int id) throws ApplicationException {
        return 0;
    }

    @Override
    public int save(IdDescription o) throws ApplicationException {
        return 0;
    }
}
