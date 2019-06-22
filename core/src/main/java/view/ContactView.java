package view;

import model.Address;

import java.util.List;

public class ContactView {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String birthDay;
    private String company;
    private String email;
    private String site;
    private Integer gender;
    private Integer maritalStatus;
    private String nationality;
    private Address addressInfo;
    private List<PhoneDetails> phoneInfo;
    private List<AttachmentDetails> attachmentInfo;

    public ContactView() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Address getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(Address addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<PhoneDetails> getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(List<PhoneDetails> phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public List<AttachmentDetails> getAttachmentInfo() {
        return attachmentInfo;
    }

    public void setAttachmentInfo(List<AttachmentDetails> attachmentInfo) {
        this.attachmentInfo = attachmentInfo;
    }
}
