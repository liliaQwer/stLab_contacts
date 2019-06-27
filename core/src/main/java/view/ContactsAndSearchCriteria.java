package view;

import utils.SearchCriteria;

import java.util.List;

public class ContactsAndSearchCriteria {
    private List<ContactShort> contactsList;
    private SearchCriteria searchCriteria;

    private int totalAmount;

    public ContactsAndSearchCriteria(List<ContactShort> contactsList, SearchCriteria searchCriteria, int totalAmount){
        this.contactsList = contactsList;
        this.searchCriteria = searchCriteria;
        this.totalAmount = totalAmount;
    }

    public List<ContactShort> getContactsList() {
        return contactsList;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
