package dao;

import model.IdDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApplicationException;
import utils.SearchCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenderDAO implements DAO<IdDescription>{
    private final static Logger logger = LogManager.getRootLogger();

    public GenderDAO(){
    }

    @Override
    public List<IdDescription> getList(Connection connection) throws SQLException {
        List<IdDescription> list = new ArrayList<>();
        String query = "select * from gender";
        try (PreparedStatement st = connection.prepareStatement(query)){
            //logger.info(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                IdDescription gender = new IdDescription();
                gender.setId(rs.getInt("id"));
                gender.setDescription(rs.getString("description"));
                list.add(gender);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
        return list;
    }

    @Override
    public IdDescription get(Connection connection, int id){
        return null;
    }

    @Override
    public List<IdDescription> getPage(Connection connection, SearchCriteria searchCriteria){
        return null;
    }

    @Override
    public int getCount(Connection connection, SearchCriteria searchCriteria){
        return 0;
    }

    @Override
    public List<IdDescription> getList(Connection connection, int param){
        return null;
    }

    @Override
    public int edit(Connection connection, IdDescription o){
        return 0;
    }

    @Override
    public int delete(Connection connection, int id){
        return 0;
    }

    @Override
    public int save(Connection connection, IdDescription o){
        return 0;
    }
}
