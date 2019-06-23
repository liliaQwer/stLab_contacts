package service;

import dao.*;
import model.*;
import utils.ApplicationException;
import view.ContactView;
import view.ContactsAndPageInfo;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class ContactServiceImpl implements ContactService{
    private DAO contactDAO;
    private DAO addressDAO;
    private DAO attachmentDAO;
    private DAO phoneDAO;

    public ContactServiceImpl(DataSource dataSource){
        this.contactDAO = new ContactDAO(dataSource);
        this.addressDAO = new AddressDAO(dataSource);
        this.attachmentDAO = new AttachmentDAO(dataSource);
        this.phoneDAO = new PhoneDAO(dataSource);
    }

    @Override
    public ContactFull get(int id) throws ApplicationException {
        ContactFull contactFull = new ContactFull((Contact) contactDAO.get(id));
        contactFull.setAddress((Address) addressDAO.get(id));
        contactFull.setAttachmentList(attachmentDAO.getList(id));
        contactFull.setPhoneList(phoneDAO.getList(id));
        return contactFull;
    }

    @Override
    public List<ContactFull> getPage(int pageNumber, int pageSize) throws ApplicationException {
        List<Contact> contactList = contactDAO.getPage(pageNumber, pageSize);
        List<ContactFull> contactFullList = new ArrayList<>();
        for (Contact contact : contactList) {
            ContactFull contactFull = new ContactFull(contact);
            contactFull.setAddress((Address)addressDAO.get(contact.getId()));
            contactFullList.add(contactFull);
        }
        return contactFullList;
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

    @Override
    public void save(ContactView o) throws ApplicationException {
        int contactId = contactDAO.save(o.getContact());
        if (contactId < 1){
            throw new ApplicationException("Cannot save contact");
        }
        if (!o.getAddressInfo().isEmpty()){
            o.getAddressInfo().setContactId(contactId);
            addressDAO.save(o.getAddressInfo());
        }
        if (!o.getAttachmentsInfo().getAttachmentsList().isEmpty()){
            for (Attachment attachment: o.getAttachmentsInfo().getAttachmentsList()){
                attachment.setContactId(contactId);
                attachmentDAO.save(attachment);
            }
        }
        if (!o.getPhoneInfo().getPhonesList().isEmpty()){
            for (Phone phone: o.getPhoneInfo().getPhonesList()){
                phone.setContactId(contactId);
                phoneDAO.save(phone);
            }
        }
    }

}
