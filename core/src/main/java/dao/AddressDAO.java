package dao;

import model.AddressModel;
import model.Model;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AddressDAO implements DAO<AddressModel>{
    private DataSource dataSource;
    //private Connection conn;

    public AddressDAO(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        //conn = dataSource.getConnection();
    }
    @Override
    public void beginTransaction() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public Model getModelById(int id) {
        AddressModel model = new AddressModel();
        String query = "select * from address where contact_id = " + id;
        try (Connection connection = dataSource.getConnection();
             Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(query);
            System.out.println(query);
            if (rs.next()){
                model.setId(id);
                model.setCity(rs.getString("city"));
                model.setCountry(rs.getString("country"));
                model.setIndex(rs.getString("index"));
                model.setStreet(rs.getString("street"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public List<? extends Model> getModelListPage(int pageNumber, int pageSize) throws SQLException {
        return null;
    }

    @Override
    public List<Model> getModelList() {
        return null;
    }

    @Override
    public void edit(AddressModel o) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void save(AddressModel o) {

    }
}
