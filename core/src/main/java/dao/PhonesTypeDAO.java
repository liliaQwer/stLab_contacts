package dao;

import model.IdDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;
import utils.SearchCriteria;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhonesTypeDAO implements DAO<IdDescription> {
    private DataSource dataSource;
    private final static Logger logger = LogManager.getRootLogger();

    public PhonesTypeDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public IdDescription get(int id) throws ApplicationException {
        return null;
    }

    @Override
    public List<IdDescription> getPage(SearchCriteria searchCriteria) throws ApplicationException {
        return null;
    }

    @Override
    public int getCount(SearchCriteria searchCriteria) throws ApplicationException {
        return 0;
    }

    @Override
    public List<IdDescription> getList() throws ApplicationException {
        List<IdDescription> list = new ArrayList<>();
        String query = "select * from phone_type";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            logger.info(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                IdDescription phoneType = new IdDescription();
                phoneType.setId(rs.getInt("id"));
                phoneType.setDescription(rs.getString("description"));
                list.add(phoneType);
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
    public int edit(IdDescription o) throws ApplicationException {
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
