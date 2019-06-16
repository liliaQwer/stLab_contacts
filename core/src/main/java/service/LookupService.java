package service;

import model.IdDescription;

import java.util.List;

public interface LookupService {

    public List<IdDescription> getGenderList();
    public List<IdDescription> getMaritalStatusList();
    public List<IdDescription> getPhoneTypesList();
}
