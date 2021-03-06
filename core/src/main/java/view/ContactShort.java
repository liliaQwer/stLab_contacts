package view;

import model.Address;
import model.ContactFull;
import utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactShort {
    private int id;
    private String fullName;
    private String birthday;
    private String company;
    private String address;
    private String email;

    public ContactShort(ContactFull model){
        this.id = model.getContact().getId();
        this.company = model.getContact().getCompany();
        LocalDate birthday = model.getContact().getBirthday();
        this.birthday = birthday != null ? DateFormatter.formatDate(birthday) : null;
        this.address = prepareAddress(model.getAddress());
        this.email = model.getContact().getEmail();
        this.fullName = prepareFullName(model.getContact().getName(), model.getContact().getSurname(), model.getContact().getPatronymic());
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    private String prepareFullName(String name, String surName, String lastName){
        String fullName = Stream.of(name, surName, lastName)
                .filter(s -> s != null)
                .collect(Collectors.joining(" "));
        return fullName;
    }

    private String prepareAddress(Address address){
        String addressLine = Stream.of(address.getCountry(), address.getCity(), address.getStreet())
                .filter(s -> s != null)
                .collect(Collectors.joining(", "));
        return addressLine;
    }

    public String getEmail() {
        return email;
    }

}
