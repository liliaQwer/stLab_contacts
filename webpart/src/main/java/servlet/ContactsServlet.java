package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.ContactService;
import service.ContactServiceImpl;
import utils.ApplicationException;
import view.ContactsAndPageInfo;
import view.ViewHelper;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = {"/contacts/*"})
public class ContactsServlet extends HttpServlet {

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    //private ContactServiceImpl service = new ContactServiceImpl(dataSource);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("pATH: " + request.getPathInfo());
        if (request.getPathInfo() == null) {
            getContactList(request, response);
        } else {
            getContact(request, response);
        }
    }

    private void getContactList(HttpServletRequest request, HttpServletResponse response){
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        ContactsAndPageInfo contactsPageView = null;
        ContactService service = new ContactServiceImpl(dataSource);
        try {
            contactsPageView = ViewHelper.prepareContactsAndPageInfoView(service.getPage(pageNumber, pageSize),
                    pageNumber, pageSize, service.getCount());
            sendJsonResponse(response, contactsPageView);
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
            return;
        }
        //EmailTemplate.getTemplates();


//        try {
//            new EmailTemplate().sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

    private void getContact(HttpServletRequest request, HttpServletResponse response)throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ContactServiceImpl service = new ContactServiceImpl(dataSource);
        System.out.println("doDelete");
        try {
            service.delete(getIdStrFromPath(request));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
        //System.out.println("gotView");
        //sendJsonResponse(response, viewList);
    }

    public void sendJsonResponse(HttpServletResponse response, Object view)  {
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, view);
            System.out.println(mapper.writeValueAsString(view));
            out.flush();
        }catch (IOException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int getIdFromPath(HttpServletRequest request){
        String idPath = request.getPathInfo();
        if (idPath == null){
            return 0;
        }
        try{
            return Integer.parseInt(idPath.substring(1));
        }catch(Exception e){
            return 0;
        }
    }

    private String getIdStrFromPath(HttpServletRequest request){
        String idPath = request.getPathInfo();
        if (idPath == null){
            return null;
        }
        return idPath.substring(1);
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


