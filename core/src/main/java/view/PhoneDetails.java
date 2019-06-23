package view;

import com.fasterxml.jackson.annotation.JsonFormat;
import model.Phone;

import java.io.Serializable;
import java.util.List;

public class PhoneDetails implements Serializable {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<Integer> deletedIds;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
