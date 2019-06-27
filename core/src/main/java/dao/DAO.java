package dao;

import utils.ApplicationException;
import utils.SearchCriteria;

import java.util.List;

public interface DAO<T> {
    T get(int id) throws ApplicationException;
    List<T> getPage(SearchCriteria searchCriteria) throws ApplicationException;
    int getCount(SearchCriteria searchCriteria) throws ApplicationException;
    List<T> getList() throws ApplicationException;
    List<T> getList(int param) throws ApplicationException;
    int edit(T o) throws ApplicationException;
    int delete(int id) throws ApplicationException;
    int save(T o) throws ApplicationException;
    default String getStringOrNull(String fieldValue){
        if (fieldValue != null && fieldValue.trim().isEmpty()){
            return null;
        }
        return fieldValue;
    }
}
