package service;

import model.Contact;
import model.ContactFull;
import utils.ApplicationException;
import view.ContactView;

import java.util.List;

public interface ContactService {
    ContactFull get(int id) throws ApplicationException;
    List<ContactFull> getPage(int pageNumber, int pageSize) throws ApplicationException;
    int getCount() throws ApplicationException;
    //List<Contact> getList() throws ApplicationException;
    void edit(ContactView o) throws ApplicationException;
    void delete(String idListStr) throws ApplicationException;
    void save(ContactView o) throws ApplicationException;
}
