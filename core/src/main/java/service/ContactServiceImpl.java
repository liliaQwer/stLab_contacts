package service;

import dao.*;
import model.*;
import utils.ApplicationException;
import utils.SearchCriteria;
import view.*;

import javax.sql.DataSource;
import java.text.ParseException;
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
        contactFull.setAttachmentsList(attachmentDAO.getList(id));
        contactFull.setPhonesList(phoneDAO.getList(id));
        return contactFull;
    }

    @Override
    public List<ContactFull> getPage(SearchCriteria searchCriteria) throws ApplicationException {
        List<Contact> contactList = contactDAO.getPage(searchCriteria);
        List<ContactFull> contactFullList = new ArrayList<>();
        for (Contact contact : contactList) {
            ContactFull contactFull = new ContactFull(contact);
            contactFull.setAddress((Address)addressDAO.get(contact.getId()));
            contactFullList.add(contactFull);
        }
        return contactFullList;
    }

    @Override
    public int getCount(SearchCriteria searchCriteria) throws ApplicationException {
        return contactDAO.getCount(searchCriteria);
    }

    @Override
    public void edit(ContactView o) throws ApplicationException {
        try {
            contactDAO.edit(o.getContact());
            o.getAddressInfo().setContactId(o.getId());
            addressDAO.edit(o.getAddressInfo());
            PhoneDetails phoneDetails = o.getPhoneInfo();
            if (phoneDetails.getDeletedIds() != null){
                for (Integer id: phoneDetails.getDeletedIds()){
                    phoneDAO.delete(id);
                }
            }
            phoneDetails.getPhonesList().forEach(phone->{
                phone.setContactId(o.getId());
                try {
                    if (phone.getId() > 0) {
                        phoneDAO.edit(phone);
                    } else {
                        phoneDAO.save(phone);
                    }
                }catch (ApplicationException e) {
                    e.printStackTrace();
                }
            });
            AttachmentDetails attachmentDetails= o.getAttachmentsInfo();
            if (attachmentDetails.getDeletedIds() != null){
                for (Integer id: attachmentDetails.getDeletedIds()){
                    attachmentDAO.delete(id);
                }
            }
            attachmentDetails.getAttachmentsList().forEach(attachmentView->{
                attachmentView.setContactId(o.getId());
                Attachment attachment;
                try {
                    attachment = ViewHelper.prepareAttachment(attachmentView);
                    if (attachment.getId() > 0) {
                        attachmentDAO.edit(attachment);
                    } else {
                        attachmentDAO.save(attachment);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }catch (ApplicationException e) {
                    e.printStackTrace();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApplicationException();
        }
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
        int contactId;
        try {
            contactId = contactDAO.save(o.getContact());
        } catch (ParseException e) {
            throw new ApplicationException("ParseException");
        }
        if (contactId < 1){
            throw new ApplicationException("Cannot save contact");
        }
        if (!o.getAddressInfo().isEmpty()){
            o.getAddressInfo().setContactId(contactId);
            addressDAO.save(o.getAddressInfo());
        }
        if (!o.getAttachmentsInfo().getAttachmentsList().isEmpty()){
            for (AttachmentView attachmentView: o.getAttachmentsInfo().getAttachmentsList()){
                attachmentView.setContactId(contactId);
                Attachment attachment = null;
                try {
                    attachment = ViewHelper.prepareAttachment(attachmentView);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new ApplicationException("ParseException");
                }
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
