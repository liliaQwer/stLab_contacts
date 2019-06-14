package service;

import dao.ContactDAO;
import dao.DAO;
import model.ContactModel;
import model.Model;
import utils.ApplicationException;
import utils.LastPageCounter;
import view.ContactShortView;
import view.ContactsPageView;
import view.View;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService {
    private DataSource dataSource;

    public ContactService(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ContactsPageView getContactListPage(int pageNumber, int pageSize) throws ApplicationException {
        DAO dao;
        ContactsPageView pageView = null;
        dao = new ContactDAO(dataSource);
        System.out.println("dao");
        List<Model> modelList = dao.getModelListPage(pageNumber, pageSize);
        System.out.println("modelList");
        List<View> viewList = new ArrayList<>();
        for (Model model : modelList) {
            View view = new ContactShortView((ContactModel) model);
            viewList.add(view);
        }
        int contactsCount = dao.getModelListCount();
        pageView = new ContactsPageView(viewList, pageNumber, pageSize, contactsCount);
        return pageView;
    }

    public void deleteContact(String idListStr) throws ApplicationException {
        DAO dao = new ContactDAO(dataSource);
        String[] idList = idListStr.split(",");
        for (String id: idList){
            dao.deleteById(Integer.parseInt(id));
        }
    }
}
