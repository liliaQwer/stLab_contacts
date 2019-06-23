package model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Address {
    private int contactId = -1;
    private String postalCode;
    private String street;
    private String city;
    private String country;

    public boolean isEmpty() {
        return Stream.of(postalCode, street, city, country)
                .filter(s -> s != null)
                .collect(Collectors.joining("")).isEmpty();
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
