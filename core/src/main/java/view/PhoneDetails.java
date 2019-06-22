package view;

import model.Phone;

import java.util.List;

public class PhoneDetails {
    List<Integer> deletedIds;
    List<Phone> phonesList;

    public PhoneDetails() {
    }

    public List<Integer> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public List<Phone> getPhonesList() {
        return phonesList;
    }

    public void setPhonesList(List<Phone> phonesList) {
        this.phonesList = phonesList;
    }
}
