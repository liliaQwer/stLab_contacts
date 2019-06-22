package service;

import dao.DAO;
import dao.GenderDAO;
import dao.MaritalStatusDAO;
import dao.PhonesTypeDAO;
import model.IdDescription;
import utils.ApplicationException;
import view.LookupsView;

import javax.sql.DataSource;
import java.util.List;

public class LookupServiceImpl implements LookupService{
    private DataSource dataSource;

    public LookupServiceImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public LookupsView getAllDictionaries() throws ApplicationException {
        LookupsView view = new LookupsView();
        view.setGenderList(getGenderList());
        view.setMaritalStatusList(getMaritalStatusList());
        view.setPhoneTypesList(getPhoneTypesList());
        return view;
    }

    public List<IdDescription> getGenderList() throws ApplicationException {
        DAO<IdDescription> dao = new GenderDAO(dataSource);
        return dao.getList();
    }

    public List<IdDescription> getMaritalStatusList() throws ApplicationException {
        DAO<IdDescription> dao = new MaritalStatusDAO(dataSource);
        return dao.getList();
    }

    public List<IdDescription> getPhoneTypesList() throws ApplicationException {
        DAO<IdDescription> dao = new PhonesTypeDAO(dataSource);
        return dao.getList();
    }
}
