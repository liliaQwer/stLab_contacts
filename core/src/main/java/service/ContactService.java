package service;

import dao.ContactDAO;
import dao.DAO;
import model.ContactModel;
import model.Model;
import view.ContactShortView;
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

    public List<View> getContactListPage(int pageNumber, int pageSize){
        DAO dao;
        List<Model> modelList;
        List<View> viewList = new ArrayList<>();
        try {
            dao = new ContactDAO(dataSource);
            System.out.println("dao");
            modelList = dao.getModelListPage(pageNumber, pageSize);
            System.out.println("modelList");
            for (Model model : modelList) {
                View view = new ContactShortView((ContactModel) model);
                viewList.add(view);
            }
        }catch (SQLException e) {
            System.out.println("sqlException");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
        return viewList;
    }

    public void deleteContact(String idListStr){
        try {
            DAO dao = new ContactDAO(dataSource);
            String[] idList = idListStr.split(",");
            for (String id: idList){
                dao.deleteById(Integer.parseInt(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
