package model;

import java.util.List;

public class ContactFull {
    private Contact contact;
    private Address address;
    private String profilePhoto;
    private List<Attachment> attachmentsList;
    private List<Phone> phonesList;

    public ContactFull() {
    }

    public ContactFull(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    public List<Attachment> getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List<Attachment> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }

    public List<Phone> getPhonesList() {
        return phonesList;
    }

    public void setPhonesList(List<Phone> phonesList) {
        this.phonesList = phonesList;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
