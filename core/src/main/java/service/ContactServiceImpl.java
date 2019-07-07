package service;

import dao.*;
import model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import upload.FileHelper;
import utils.ApplicationException;
import utils.Message;
import utils.SearchCriteria;
import view.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactServiceImpl implements ContactService {
    private final static Logger logger = LogManager.getLogger(ContactServiceImpl.class);
    private DataSource dataSource;
    private DAO contactDAO;
    private DAO addressDAO;
    private DAO attachmentDAO;
    private DAO phoneDAO;

    public ContactServiceImpl(DataSource dataSource) {
        this(dataSource, new ContactDAO(), new AddressDAO(), new AttachmentDAO(), new PhoneDAO());
    }

    public ContactServiceImpl(DataSource dataSource, DAO<Contact> contactDAO, DAO<Address> addressDAO, DAO<Attachment> attachmentDAO, DAO<Phone> phoneDAO) {
        this.dataSource = dataSource;
        this.contactDAO = contactDAO;
        this.addressDAO = addressDAO;
        this.attachmentDAO = attachmentDAO;
        this.phoneDAO = phoneDAO;
    }

    @Override
    public ContactFull get(int id) throws ApplicationException {
        Connection connection = null;
        ContactFull contactFull = null;
        try {
            connection = dataSource.getConnection();
            contactFull = new ContactFull((Contact) contactDAO.get(connection, id));
            contactFull.setAddress((Address) addressDAO.get(connection, id));
            contactFull.setAttachmentsList(attachmentDAO.getList(connection, id));
            contactFull.setPhonesList(phoneDAO.getList(connection, id));
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {

                }
            }
            throw new ApplicationException();
        }
        return contactFull;
    }

    @Override
    public List<ContactFull> getPage(SearchCriteria searchCriteria) throws ApplicationException {
        Connection connection = null;
        List<ContactFull> contactFullList = null;
        try {
            connection = dataSource.getConnection();
            List<Contact> contactList = contactDAO.getPage(connection, searchCriteria);
            contactFullList = new ArrayList<>();
            for (Contact contact : contactList) {
                ContactFull contactFull = new ContactFull(contact);
                contactFull.setAddress((Address) addressDAO.get(connection, contact.getId()));
                contactFullList.add(contactFull);
            }
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return contactFullList;
    }

    @Override
    public int getCount(SearchCriteria searchCriteria) throws ApplicationException {
        Connection connection = null;
        int result = 0;
        try {
            connection = dataSource.getConnection();
            result = contactDAO.getCount(connection, searchCriteria);
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return result;
    }

    @Override
    public void edit(ContactView o, List<FileItem> fileItemList) throws ApplicationException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            contactDAO.edit(connection, o.getContact());
            o.getAddressInfo().setContactId(o.getId());
            addressDAO.edit(connection, o.getAddressInfo());
            PhoneDetails phoneDetails = o.getPhoneInfo();
            if (phoneDetails.getDeletedIds() != null) {
                for (Integer id : phoneDetails.getDeletedIds()) {
                    phoneDAO.delete(connection, id);
                }
            }
            for (Phone phone : phoneDetails.getPhonesList()) {
                phone.setContactId(o.getId());
                if (phone.getId() > 0) {
                    phoneDAO.edit(connection, phone);
                } else {
                    phoneDAO.save(connection, phone);
                }
            }
            FileHelper fileCreator = FileHelper.getInstance();
            AttachmentDetails attachmentDetails = o.getAttachmentsInfo();
            if (attachmentDetails.getDeletedIds() != null) {
                for (Integer id : attachmentDetails.getDeletedIds()) {
                    Attachment attachment = (Attachment)attachmentDAO.get(connection, id);
                    fileCreator.deleteAttachment(attachment);
                    attachmentDAO.delete(connection, id);
                }
            }
            for (AttachmentView attachmentView : attachmentDetails.getAttachmentsList()) {
                attachmentView.setContactId(o.getId());
                Attachment attachment = ViewHelper.prepareAttachment(attachmentView);
                int  attachId;
                if (attachment.getId() > 0) {
                    attachId = attachment.getId();
                    attachmentDAO.edit(connection, attachment);
                } else {
                    attachId = attachmentDAO.save(connection, attachment);
                }
                Attachment attach = ((Attachment)attachmentDAO.get(connection, attachId));// get attachment with uuid
                fileItemList.stream().forEach(item -> {
                    if (item.getFieldName().endsWith("_" + attachmentView.getId())){
                        fileCreator.uploadAttachment(item, attach);
                    }
                });
            }

            connection.commit();
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            throw new ApplicationException();
        }
    }

    @Override
    public void delete(String idListStr) throws ApplicationException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String[] idList = idListStr.split(",");
            int result = 0;
            for (String id : idList) {
                result += contactDAO.delete(connection, Integer.parseInt(id));
            }
            if (result != idList.length) {
                throw new ApplicationException(Message.CONTACT_NOT_DELETED);
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
    }

    @Override
    public void save(ContactView o, List<FileItem> fileItemList) throws ApplicationException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            int contactId = contactDAO.save(connection, o.getContact());

            if (contactId < 1) {
                throw new ApplicationException(Message.CONTACT_NOT_SAVED);
            }
            o.setId(contactId);
            if (!o.getAddressInfo().isEmpty()) {
                o.getAddressInfo().setContactId(contactId);
                addressDAO.save(connection, o.getAddressInfo());
            }
            FileHelper fileCreator = FileHelper.getInstance();
            for (AttachmentView attachmentView : o.getAttachmentsInfo().getAttachmentsList()) {
                attachmentView.setContactId(contactId);
                Attachment attachment = null;
                try {
                    attachment = ViewHelper.prepareAttachment(attachmentView);
                } catch (ParseException e) {
                    logger.error(e);
                    throw new ApplicationException(e.getMessage());
                }
                int  attachId = attachmentDAO.save(connection, attachment);
                Attachment attach = ((Attachment)attachmentDAO.get(connection, attachId));// get attachment with uuid
                fileItemList.stream().forEach(item -> {
                    if (item.getFieldName().endsWith("_" + attachmentView.getId())){
                        fileCreator.uploadAttachment(item, attach);
                    }
                });
            }
            for (Phone phone : o.getPhoneInfo().getPhonesList()) {
                phone.setContactId(contactId);
                phoneDAO.save(connection, phone);
            }
            fileItemList.forEach(item -> {
                if (item.getFieldName().equals("profilePhoto")){
                    fileCreator.uploadProfilePhoto(item, contactId);
                }
            });
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        } catch (ParseException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            logger.error(e);
            throw new ApplicationException();
        }

    }

    @Override
    public List<ContactEmail> getTodayBirthdayContactsEmails() throws ApplicationException {
        Connection connection = null;
        ArrayList<ContactEmail> emailList = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            ArrayList<Contact> contactList = (ArrayList<Contact>) contactDAO.getList(connection);
            contactList.stream().filter(contact -> {
                LocalDate birthday = contact.getBirthday();
                LocalDate today = LocalDate.now();
                if (birthday == null) {
                    return false;
                }
                if (birthday.getMonth() == today.getMonth() && birthday.getDayOfMonth() == today.getDayOfMonth()) {
                    return contact.getEmail() != null;
                }
                return false;
            })
                    .forEach(contact ->
                            emailList.add(new ContactEmail(contact.getName(), contact.getEmail())));

            connection.close();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return emailList;
    }

}
