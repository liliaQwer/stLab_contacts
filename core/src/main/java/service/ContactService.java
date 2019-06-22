package service;

import model.Contact;
import model.ContactFull;
import utils.ApplicationException;

import java.util.List;

public interface ContactService {
    //Contact get(int id) throws ApplicationException;
    List<ContactFull> getPage(int pageNumber, int pageSize) throws ApplicationException;
    int getCount() throws ApplicationException;
    //List<Contact> getList() throws ApplicationException;
    //void update(Contact o) throws ApplicationException;
    void delete(String idListStr) throws ApplicationException;
    //void save(Contact o) throws ApplicationException;
}
