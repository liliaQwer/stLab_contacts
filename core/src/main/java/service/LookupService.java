package service;

import model.IdDescription;
import utils.ApplicationException;

import java.util.List;

public interface LookupService {

    public List<IdDescription> getGenderList()  throws ApplicationException;
    public List<IdDescription> getMaritalStatusList()  throws ApplicationException;
    public List<IdDescription> getPhoneTypesList()  throws ApplicationException;
}
