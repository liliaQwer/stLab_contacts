package view;

import com.fasterxml.jackson.annotation.JsonFormat;
import model.Address;
import model.Contact;

import java.sql.Date;
import java.util.List;

public class ContactView{
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private Date birthday;
    private String company;
    private String email;
    private String site;
    private Integer gender;
    private Integer maritalStatus;
    private String nationality;
    private Address addressInfo;
    private PhoneDetails phoneInfo;
    private AttachmentDetails attachmentsInfo;

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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


    public Contact getContact(){
        return new Contact(id, name, surname, patronymic, birthday, company, site, email, nationality, gender, maritalStatus);
    }

    public PhoneDetails getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(PhoneDetails phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public AttachmentDetails getAttachmentsInfo() {
        return attachmentsInfo;
    }

    public void setAttachmentsInfo(AttachmentDetails attachmentsInfo) {
        this.attachmentsInfo = attachmentsInfo;
    }
}
