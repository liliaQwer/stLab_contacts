package dao;

import model.AddressModel;
import model.ContactModel;
import model.Model;
import utils.ContactStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class ContactDAO implements DAO<ContactModel>{
    private DataSource dataSource;
    //private Connection conn;
    private String NEXT_PAGE = "next";

    public ContactDAO(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        //conn = dataSource.getConnection();
    }

    @Override
    public void beginTransaction() throws SQLException {
        //conn.setAutoCommit(false);
    }

    @Override
    public void rollback() throws SQLException {
        //conn.rollback();
        //conn.setAutoCommit(true);
    }

    @Override
    public void commit() throws SQLException {
        //conn.commit();
        //conn.setAutoCommit(true);
    }

    @Override
    public ContactModel getModelById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ContactModel> getModelListPage(int pageNumber, int pageSize) throws SQLException {
        List<ContactModel> modelList = new ArrayList<>();
        AddressDAO addressDAO = new AddressDAO(dataSource);
        StringBuilder queryBuilder = new StringBuilder("select * from contact where status = '");
        queryBuilder.append(ContactStatus.ACTIVATED.getStatus());
        queryBuilder.append("' limit ");
        queryBuilder.append(pageSize * (pageNumber - 1));
        queryBuilder.append(",");
        queryBuilder.append(pageSize);
        System.out.println("before getConnect ");
        try (Connection connection = dataSource.getConnection();
             Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(queryBuilder.toString());
            System.out.println(queryBuilder.toString());
            while(rs.next()){
                ContactModel model = new ContactModel();
                model.setId(rs.getInt("id"));
                model.setName(rs.getString("name"));
                model.setSurName(rs.getString("surname"));
                model.setLastName(rs.getString("patronymic"));
                model.setBirthDay(rs.getDate("birthday"));
                model.setCompany(rs.getString("company"));
                Model addressModel = addressDAO.getModelById(model.getId());
                model.setAddress(addressModel != null ? (AddressModel)addressModel : null);
                modelList.add(model);
                System.out.println(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList == null ? Collections.emptyList() : modelList;
    }

    @Override
    public List<? extends Model> getModelList() throws SQLException {
        return null;
    }

    @Override
    public void edit(ContactModel o) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {
        String query = "update contact set status = '" + ContactStatus.DEACTIVATED.getStatus() + "' where id = " + id;
        System.out.println("before" + query);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(query)){
            int result = st.executeUpdate();
            System.out.println("result " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ContactModel o) throws SQLException {

    }

}
