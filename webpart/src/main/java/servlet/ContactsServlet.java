package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ContactFull;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.ContactService;
import service.ContactServiceImpl;
import upload.FileHelper;
import utils.ApplicationException;
import utils.DateFormatter;
import utils.SearchCriteria;
import view.ContactView;
import view.ContactsAndSearchCriteria;
import view.ViewHelper;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/contacts/*"})
public class ContactsServlet extends HttpServlet {
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
        System.out.println("doPut");
        ContactView contact = null;
        List<FileItem> fileItems = new ArrayList<>();
        try {
            List formItems = getFormItems(request);
            Iterator iter = formItems.iterator();
            // iterates over form's fields
            while (iter.hasNext()) {
                Object param = iter.next();
                System.out.println("param = " + param);
                FileItem item = (FileItem) param;
                if (!item.isFormField()) {
                    fileItems.add(item);
                } else {
                    System.out.println("value=" + item.getString());
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString("UTF-8"), ContactView.class);
                }
            }
            ContactService service = new ContactServiceImpl(dataSource);
            service.edit(contact);
            int contactId = contact.getId();
            FileHelper fileCreator = FileHelper.getInstance();
            fileItems.forEach(item -> fileCreator.upload(item, contactId));
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

        ContactView contact = null;
        List<FileItem> fileItems = new ArrayList<>();
        try {
            List formItems = getFormItems(request);
            Iterator iter = formItems.iterator();
            // iterates over form's fields
            while (iter.hasNext()) {
                Object param = iter.next();
                System.out.println("param = " + param);
                FileItem item = (FileItem) param;
                if (!item.isFormField()) {
                    fileItems.add(item);
                } else {
                    System.out.println("value=" + item.getString());
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString(), ContactView.class);
                }
            }
            ContactService service = new ContactServiceImpl(dataSource);
            service.save(contact);
            int contactId = contact.getId();
            FileHelper fileCreator = FileHelper.getInstance();
            fileItems.forEach(item -> fileCreator.upload(item, contactId));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
    }

    private List getFormItems(HttpServletRequest request) throws FileUploadException {
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        return upload.parseRequest(request);
    }


    private void getContactList(HttpServletRequest request, HttpServletResponse response) {
        SearchCriteria searchCriteria;
        try {
            searchCriteria = parseRequestParams(request);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ContactsAndSearchCriteria contactsPageView = null;
        ContactService service = new ContactServiceImpl(dataSource);
        try {
            service.getPage(searchCriteria);
            contactsPageView = ViewHelper.prepareContactsAndPageInfoView(service.getPage(searchCriteria),
                    searchCriteria, service.getCount(searchCriteria));
            sendJsonResponse(response, contactsPageView);
        } catch (ApplicationException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
    }

    private SearchCriteria parseRequestParams(HttpServletRequest request) throws IllegalAccessException, ParseException {
        SearchCriteria searchCriteria = new SearchCriteria();
        Field[] allFields = SearchCriteria.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(allFields).map(field->field.getName()).collect(Collectors.toList());
        Enumeration requestParams= request.getParameterNames();
        while (requestParams.hasMoreElements()){
            String param = requestParams.nextElement().toString();
            if (request.getParameter(param).trim().isEmpty()){
                continue;
            }
            System.out.println("*************Param = " + param);
            int fieldIdx = fieldNames.indexOf(param);
            System.out.println("Idx = " + fieldIdx);
            System.out.println("fields[fieldIdx].getType() = " + allFields[fieldIdx].getType());
            System.out.println("request.getParameter(param) = " + request.getParameter(param));
            if (fieldIdx != -1){
                if (allFields[fieldIdx].getType() == Integer.class){
                    System.out.println("Int try = ");
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, Integer.parseInt(request.getParameter(param)));
                }else if (allFields[fieldIdx].getType() == String.class){
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, request.getParameter(param));
                    System.out.println("in String.class) = " + request.getParameter(param));
                }else if(allFields[fieldIdx].getType() == LocalDate.class){
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, DateFormatter.parseDate(request.getParameter(param)));
                }
            }
        }
        System.out.println("searchCriteria pN=" + searchCriteria.getPageNumber() + " ps=" + searchCriteria.getPageSize() +
                " n=" + searchCriteria.getName());
        return searchCriteria;
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


