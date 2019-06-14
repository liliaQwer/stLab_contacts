package dao;

import model.Model;
import utils.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T extends Model> {
    void beginTransaction() throws ApplicationException;
    void rollback() throws ApplicationException;
    void commit() throws ApplicationException;
    Model getModelById(int id) throws ApplicationException;
    List<? extends Model> getModelListPage(int pageNumber,  int pageSize) throws ApplicationException;
    int getModelListCount() throws ApplicationException;
    List<? extends Model> getModelList() throws ApplicationException;
    void edit(T o) throws ApplicationException;
    void deleteById(int id) throws ApplicationException;
    void save(T o) throws ApplicationException;
}
