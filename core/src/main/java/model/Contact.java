package model;

import java.sql.Date;
import java.time.LocalDate;

public class Contact {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthday;
    private String company;
    private String site;
    private String email;
    private String nationality;
    private Integer gender;
    private Integer maritalStatus;

    public Contact() {

    }

    public Contact(int id, String name, String surname, String patronymic, LocalDate birthday, String company, String site,
                   String email, String nationality, Integer gender, Integer maritalStatus) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.company = company;
        this.site = site;
        this.email = email;
        this.nationality = nationality;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return (name == null) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return (surname == null) ? "" : surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return (patronymic == null) ? "" : patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
