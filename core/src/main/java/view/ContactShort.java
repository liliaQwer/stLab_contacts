package view;

import model.Address;
import model.Contact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactShort {
    private int id;
    private String fullName;
    private String birthDay;
    private String company;
    private String address;
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    public ContactShort(Contact model){
        this.id = model.getId();
        this.company = model.getCompany();
        this.birthDay = formatDate(model.getBirthDay());
        this.address = prepareAddress(model.getAddress());
        this.fullName = prepareFullName(model.getName(), model.getSurName(), model.getLastName());
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthDay() {
        return birthDay;
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

    private String formatDate(Date date){
        return df.format(date);
    }
}
