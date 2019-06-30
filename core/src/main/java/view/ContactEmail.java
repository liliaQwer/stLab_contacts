package view;

public class ContactEmail {
    private String contactName;
    private String email;

    public ContactEmail() {
    }

    public ContactEmail(String contactName, String email) {
        this.contactName = contactName;
        this.email = email;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
