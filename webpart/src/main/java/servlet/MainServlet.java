package servlet;

import dao.ContactDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DAO;
import email.EmailTemplate;
import model.ContactModel;
import model.Model;
import service.ContactService;
import utils.ApplicationException;
import view.ContactShortView;
import view.ContactsPageView;
import view.View;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(urlPatterns = {"/contact"})
public class MainServlet extends HttpServlet {

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    //private ContactService service = new ContactService(dataSource);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ContactService service = new ContactService(dataSource);
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        ContactsPageView contactsPageView = null;
        try {
            contactsPageView = service.getContactListPage(pageNumber, pageSize);
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
            return;
        }
        System.out.println("gotView");
        //EmailTemplate.getTemplates();
        sendJsonResponse(response, contactsPageView);

//        try {
//            new EmailTemplate().sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ContactService service = new ContactService(dataSource);
        System.out.println("doDelete");
        try {
            service.deleteContact(request.getParameter("id"));
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        //System.out.println("gotView");
        //sendJsonResponse(response, viewList);
    }

    public void sendJsonResponse(HttpServletResponse response, Object view) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, view);
        System.out.println(mapper.writeValueAsString(view));
        out.flush();
    }

}
    /*
    try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/contacts_karavaichyk?serverTimezone=EST5EDT",
                    "root", "qwer");
            System.out.println("getSchema" + connection.getSchema());
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from contact");
            System.out.println("in result");
            rs.next();
            System.out.println(rs.getString("name"));
            st.close();
            connection.close();*/

            /*MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("qwer");
            dataSource.setURL("jdbc:mysql://localhost:3306/contacts_karavaichyk?serverTimezone=EST5EDT");
            Connection connection = dataSource.getConnection();
            System.out.println("dataSource");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from contact");
            System.out.println("in result");
            rs.next();
            System.out.println(rs.getString("name"));
            st.close();
            connection.close();
        */


