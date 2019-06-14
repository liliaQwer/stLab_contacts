package view;

import java.util.List;

public class ContactsPageView {
    private List<View> contactsList;
    private int pageSize;
    private int pageNumber;
    private int totalAmount;

    public ContactsPageView(List<View> contactsList, int pageNumber, int pageSize, int totalAmount){
        this.contactsList = contactsList;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalAmount = totalAmount;
    }

    public List<View> getContactsList() {

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
