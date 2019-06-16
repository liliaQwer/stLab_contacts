package view;

import java.util.List;

public class ContactsAndPageInfo {
    private List<ContactShort> contactsList;
    private int pageSize;
    private int pageNumber;
    private int totalAmount;

    public ContactsAndPageInfo(List<ContactShort> contactsList, int pageNumber, int pageSize, int totalAmount){
        this.contactsList = contactsList;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalAmount = totalAmount;
    }

    public List<ContactShort> getContactsList() {
        return contactsList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
