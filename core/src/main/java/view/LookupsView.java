package view;

import model.IdDescription;

import java.util.List;

public class LookupsView {
    private List<IdDescription> genderList;
    private List<IdDescription> maritalStatusList;
    private List<IdDescription> phoneTypesList;

    public LookupsView() {
    }

    public List<IdDescription> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<IdDescription> genderList) {
        this.genderList = genderList;
    }

    public List<IdDescription> getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(List<IdDescription> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    public List<IdDescription> getPhoneTypesList() {
        return phoneTypesList;
    }

    public void setPhoneTypesList(List<IdDescription> phoneTypesList) {
        this.phoneTypesList = phoneTypesList;
    }
}
