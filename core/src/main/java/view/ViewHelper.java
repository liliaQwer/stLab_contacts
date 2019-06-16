package view;

import model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ViewHelper {
    public static ContactsAndPageInfo prepareContactsAndPageInfoView(List<Contact> list, int pageNumber, int pageSize, int totalAmount){
        List<ContactShort> contactShortList = new ArrayList<>();
        for (Contact contact : list) {
            ContactShort view = new ContactShort(contact);
            contactShortList.add(view);
        }
        return new ContactsAndPageInfo(contactShortList, pageNumber, pageSize, totalAmount);
    }

}
