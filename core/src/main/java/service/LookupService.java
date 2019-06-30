package service;

import model.IdDescription;
import utils.ApplicationException;

import java.util.List;

public interface LookupService {

     List<IdDescription> getGenderList()  throws ApplicationException;
     List<IdDescription> getMaritalStatusList()  throws ApplicationException;
     List<IdDescription> getPhoneTypesList()  throws ApplicationException;
}
