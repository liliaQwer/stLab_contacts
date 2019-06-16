package dao;

import utils.ApplicationException;

import java.util.List;

public interface DAO<T> {
    T get(int id) throws ApplicationException;
    List<T> getPage(int pageNumber,  int pageSize) throws ApplicationException;
    int getCount() throws ApplicationException;
    List<T> getList() throws ApplicationException;
    int update(T o) throws ApplicationException;
    int delete(int id) throws ApplicationException;
    int save(T o) throws ApplicationException;
}
