package view;

import model.Attachment;
import model.ContactFull;
import model.IdDescription;
import utils.DateFormatter;
import utils.SearchCriteria;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewHelper {

    public static ContactsAndSearchCriteria prepareContactsAndPageInfoView(List<ContactFull> list, SearchCriteria searchCriteria, int totalAmount) {
        List<ContactShort> contactShortList = new ArrayList<>();
        for (ContactFull contact : list) {
            ContactShort view = new ContactShort(contact);
            contactShortList.add(view);
        }
        return new ContactsAndSearchCriteria(contactShortList, searchCriteria, totalAmount);
    }

    public static LookupsView prepareLookupsView(List<IdDescription> genderList, List<IdDescription> maritalStatusList,
                                                 List<IdDescription> phoneTypesList) {
        LookupsView lookupsView = new LookupsView();
        lookupsView.setGenderList(genderList);
        lookupsView.setMaritalStatusList(maritalStatusList);
        lookupsView.setPhoneTypesList(phoneTypesList);
        return lookupsView;
    }

    public static ContactView prepareContactView(ContactFull contactFull) {
        ContactView contactView = new ContactView();
        contactView.setId(contactFull.getContact().getId());
        LocalDate birthday = contactFull.getContact().getBirthday();
        contactView.setBirthday(birthday != null ? DateFormatter.formatDate(birthday) : null);
        contactView.setCompany(contactFull.getContact().getCompany());
        contactView.setEmail(contactFull.getContact().getEmail());
        contactView.setGender(contactFull.getContact().getGender());
        contactView.setMaritalStatus(contactFull.getContact().getMaritalStatus());
        contactView.setName(contactFull.getContact().getName());
        contactView.setNationality(contactFull.getContact().getNationality());
        contactView.setPatronymic(contactFull.getContact().getPatronymic());
        contactView.setSite(contactFull.getContact().getSite());
        contactView.setProfilePhoto(contactFull.getContact().getProfilePhoto());
        contactView.setSurname(contactFull.getContact().getSurname());
        contactView.setAddressInfo(contactFull.getAddress());
        PhoneDetails phoneDetails = new PhoneDetails();
        phoneDetails.setPhonesList(contactFull.getPhonesList());
        contactView.setPhoneInfo(phoneDetails);
        AttachmentDetails attachmentDetails = new AttachmentDetails();
        attachmentDetails.setAttachmentsList(contactFull.getAttachmentsList().stream().
               map(att -> new AttachmentView(att)).collect(Collectors.toList()));
        contactView.setAttachmentsInfo(attachmentDetails);
        return contactView;
    }

    public static Attachment prepareAttachment(AttachmentView attachmentView) throws ParseException {
        Attachment attachment = new Attachment();
        attachment.setId(attachmentView.getId());
        attachment.setContactId(attachmentView.getContactId());
        attachment.setComment(attachmentView.getComment());
        attachment.setFileName(attachmentView.getFileName());
        attachment.setUploadDate(DateFormatter.parseDate(attachmentView.getUploadDate()));
        return attachment;
    }
}
