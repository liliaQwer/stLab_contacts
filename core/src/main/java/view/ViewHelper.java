package view;

import model.Contact;
import model.ContactFull;
import model.IdDescription;

import java.util.ArrayList;
import java.util.List;

public class ViewHelper {

    public static ContactsAndPageInfo prepareContactsAndPageInfoView(List<ContactFull> list, int pageNumber, int pageSize, int totalAmount){
        List<ContactShort> contactShortList = new ArrayList<>();
        for (ContactFull contact : list) {
            ContactShort view = new ContactShort(contact);
            contactShortList.add(view);
        }
        return new ContactsAndPageInfo(contactShortList, pageNumber, pageSize, totalAmount);
    }

    public static  LookupsView prepareLookupsView(List<IdDescription> genderList, List<IdDescription> maritalStatusList,
                                                  List<IdDescription> phoneTypesList){
        LookupsView lookupsView = new LookupsView();
        lookupsView.setGenderList(genderList);
        lookupsView.setMaritalStatusList(maritalStatusList);
        lookupsView.setPhoneTypesList(phoneTypesList);
        return lookupsView;

    }
}
