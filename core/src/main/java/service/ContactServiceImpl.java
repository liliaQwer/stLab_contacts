package service;

import dao.AddressDAO;
import dao.ContactDAO;
import dao.DAO;
import model.Address;
import model.Contact;
import utils.ApplicationException;
import view.ContactsAndPageInfo;

import javax.sql.DataSource;
import java.util.List;

public class ContactServiceImpl implements ContactService{
    private DAO contactDAO;
    private DAO addressDAO;

    public ContactServiceImpl(DataSource dataSource){
        this.contactDAO = new ContactDAO(dataSource);
        this.addressDAO = new AddressDAO(dataSource);
    }

    @Override
    public List<Contact> getPage(int pageNumber, int pageSize) throws ApplicationException {
        ContactsAndPageInfo pageView = null;
        List<Contact> contactList = contactDAO.getPage(pageNumber, pageSize);
        for (Contact contact : contactList) {
            contact.setAddress((Address)addressDAO.get(contact.getId()));
        }
        return contactList;
    }

    @Override
    public int getCount() throws ApplicationException {
        return contactDAO.getCount();
    }

    @Override
    public void delete(String idListStr) throws ApplicationException {
        String[] idList = idListStr.split(",");
        int result = 0;
        for (String id: idList){
            result += contactDAO.delete(Integer.parseInt(id));
        }
        if (result != idList.length) {
            throw new ApplicationException("There was an error during deleting records");
        }
    }

}
