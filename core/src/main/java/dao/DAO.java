package dao;

import utils.ApplicationException;
import utils.SearchCriteria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T get(Connection conn, int id) throws SQLException;
    List<T> getPage(Connection conn, SearchCriteria searchCriteria) throws SQLException;
    int getCount(Connection conn, SearchCriteria searchCriteria) throws SQLException;
    List<T> getList(Connection conn) throws SQLException;
    List<T> getList(Connection conn, int param) throws SQLException;
    int edit(Connection conn, T o) throws SQLException;
    int delete(Connection conn, int id) throws SQLException;
    int save(Connection conn, T o) throws SQLException;
    default String getStringOrNull(String fieldValue){
        if (fieldValue != null && fieldValue.trim().isEmpty()){
            return null;
        }
        return fieldValue;
    }
}
