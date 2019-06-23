package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import model.Attachment;
import model.Contact;
import model.ContactFull;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.ContactService;
import service.ContactServiceImpl;
import utils.ApplicationException;
import view.ContactView;
import view.ContactsAndPageInfo;
import view.ViewHelper;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.util.Iterator;
import java.util.List;


@WebServlet(urlPatterns = {"/contacts/*"})
public class ContactsServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    //private ContactServiceImpl service = new ContactServiceImpl(dataSource);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null) {
            getContactList(request, response);
        } else {
            getContact(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        System.out.println(uploadPath);
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        ContactView contact = null;
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            // iterates over form's fields
            while (iter.hasNext()) {
                Object param = iter.next();
                System.out.println("param = " + param);
                FileItem item = (FileItem) param;
                System.out.println("item.getFieldName() = " + item.getFieldName());
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    System.out.println("content type = " + item.getContentType() + " item.getFileName=" + item.getName());
                    String fileName = new File(item.getName()).getName();
                    System.out.println("fileName =" + fileName);
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    // saves the file on disk
                    item.write(storeFile);
                } else {
                    System.out.println("value=" + item.getString());
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString(), ContactView.class);
                }
            }
            ContactService service = new ContactServiceImpl(dataSource);
            service.edit(contact);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("in post");
        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        System.out.println(uploadPath);
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        ContactView contact = null;
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            // iterates over form's fields
            while (iter.hasNext()) {
                Object param = iter.next();
                System.out.println("param = " + param);
                FileItem item = (FileItem) param;
                System.out.println("item.getFieldName() = " + item.getFieldName());
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    System.out.println("content type = " + item.getContentType() + " item.getFileName=" + item.getName());
                    String fileName = new File(item.getName()).getName();
                    System.out.println("fileName =" + fileName);
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    // saves the file on disk
                    item.write(storeFile);
                } else {
                    System.out.println("value=" + item.getString());
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString(), ContactView.class);

                }
            }
            ContactService service = new ContactServiceImpl(dataSource);
            service.save(contact);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
    }

    private void getContactList(HttpServletRequest request, HttpServletResponse response) {
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
        }
        //EmailTemplate.getTemplates();


//        try {
//            new EmailTemplate().sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

    private void getContact(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ContactServiceImpl service = new ContactServiceImpl(dataSource);
        System.out.println("getContact " + request.getPathInfo());
        try {
            ContactFull contact = service.get(getIdFromPath(request));
            ContactView view = ViewHelper.prepareContactView(contact);
            sendJsonResponse(response, view);
        } catch (ApplicationException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ContactServiceImpl service = new ContactServiceImpl(dataSource);
        System.out.println("doDelete");
        try {
            service.delete(getIdStrFromPath(request));
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
        //System.out.println("gotView");
        //sendJsonResponse(response, viewList);
    }

    public void sendJsonResponse(HttpServletResponse response, Object view) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, view);
            System.out.println(mapper.writeValueAsString(view));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int getIdFromPath(HttpServletRequest request) {
        String idPath = request.getPathInfo();
        if (idPath == null) {
            return 0;
        }
        try {
            return Integer.parseInt(idPath.substring(1));
        } catch (Exception e) {
            return 0;
        }
    }

    private String getIdStrFromPath(HttpServletRequest request) {
        String idPath = request.getPathInfo();
        if (idPath == null) {
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

            /*MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("qwer");
            dataSource.setURL("jdbc:mysql://localhost:3306/contacts_karavaichyk?serverTimezone=EST5EDT");
            Connection connection = dataSource.getConnection();
            System.out.println("dataSource");

        */


