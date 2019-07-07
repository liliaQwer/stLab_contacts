package service;

import model.ContactFull;
import org.apache.commons.fileupload.FileItem;
import utils.ApplicationException;
import utils.SearchCriteria;
import view.ContactEmail;
import view.ContactView;

import java.util.List;

public interface ContactService {
    ContactFull get(int id) throws ApplicationException;
    List<ContactFull> getPage(SearchCriteria searchCriteria) throws ApplicationException;
    int getCount(SearchCriteria searchCriteria) throws ApplicationException;
    //List<Contact> getList() throws ApplicationException;
    void edit(ContactView o, List<FileItem> fileItemList) throws ApplicationException;
    void delete(String idListStr) throws ApplicationException;
    void save(ContactView o, List<FileItem> fileItemList) throws ApplicationException;
    List<ContactEmail> getTodayBirthdayContactsEmails() throws ApplicationException;
}
