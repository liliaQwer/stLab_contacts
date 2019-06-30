package service;

import dao.DAO;
import dao.GenderDAO;
import dao.MaritalStatusDAO;
import dao.PhonesTypeDAO;
import model.IdDescription;
import utils.ApplicationException;
import view.LookupsView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
        DAO<IdDescription> dao = new GenderDAO();
        Connection connection = null;
        List<IdDescription> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            dao = new GenderDAO();
            list= dao.getList(connection);
            connection.close();
        } catch (SQLException e) {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return list;
    }

    public List<IdDescription> getMaritalStatusList() throws ApplicationException {
        DAO<IdDescription> dao = new MaritalStatusDAO();
        Connection connection = null;
        List<IdDescription> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            list = dao.getList(connection);
            connection.close();
        }catch (SQLException e) {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return list;
    }

    public List<IdDescription> getPhoneTypesList() throws ApplicationException {
        DAO<IdDescription> dao = new PhonesTypeDAO();
        Connection connection = null;
        List<IdDescription> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            list = dao.getList(connection);
            connection.close();
        }catch (SQLException e) {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException();
        }
        return list;
    }
}
