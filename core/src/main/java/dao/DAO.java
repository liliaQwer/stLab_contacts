package dao;

import model.Model;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T extends Model> {
    void beginTransaction() throws SQLException;
    void rollback() throws SQLException;
    void commit() throws SQLException;
    Model getModelById(int id) throws SQLException;
    List<? extends Model> getModelListPage(int pageNumber,  int pageSize) throws SQLException;
    List<? extends Model> getModelList() throws SQLException;
    void edit(T o) throws SQLException;
    void deleteById(int id) throws SQLException;
    void save(T o) throws SQLException;
}
