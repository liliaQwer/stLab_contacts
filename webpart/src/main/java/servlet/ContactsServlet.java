package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ContactFull;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ContactService;
import service.ContactServiceImpl;
import upload.FileHelper;
import utils.*;
import view.ContactView;
import view.ContactsAndSearchCriteria;
import view.MessageInfo;
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
public class ContactsServlet extends HttpServlet implements JsonSendable {
    private final static Logger logger = LogManager.getLogger(ContactsServlet.class);
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 50;  // 50MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 50; // 50MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    private ContactService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
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
            sendJsonResponse(response, new ApplicationException(Message.CONTACT_NOT_FOUND));
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
                FileItem item = (FileItem) param;
                if (!item.isFormField()) {
                    fileItems.add(item);
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString("UTF-8"), ContactView.class);
                    Validator.validate(contact);
                }
            }
            service.edit(contact, fileItems);
            sendJsonResponse(response, new MessageInfo(Message.CONTACT_UPDATED));
        } catch (ApplicationException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        // checks if the request actually contains upload file
        if (!isMultipartContent(request, response)){
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
                FileItem item = (FileItem) param;
                if (!item.isFormField()) {
                    fileItems.add(item);
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    contact = mapper.readValue(item.getString("UTF-8"), ContactView.class);
                    Validator.validate(contact);
                }
            }
            service.save(contact, fileItems);
            sendJsonResponse(response, new MessageInfo(Message.CONTACT_SAVED));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, ex);
        }
    }

    private List getFormItems(HttpServletRequest request) throws ApplicationException{
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        try {
            return upload.parseRequest(request);
        } catch (FileUploadException e) {
            logger.error(e);
            throw new ApplicationException();
        }
    }

    private boolean isMultipartContent(HttpServletRequest request, HttpServletResponse response){
        if (ServletFileUpload.isMultipartContent(request)) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        sendJsonResponse(response, new ApplicationException());
        return false;
    }

    private void getContactList(HttpServletRequest request, HttpServletResponse response) {
        SearchCriteria searchCriteria;
        try {
            searchCriteria = parseRequestParams(request);
        } catch (Exception e) {
            logger.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, new ApplicationException());
            return;
        }
        ContactsAndSearchCriteria contactsPageView;
        try {
            contactsPageView = ViewHelper.prepareContactsAndPageInfoView(service.getPage(searchCriteria),
                    searchCriteria, service.getCount(searchCriteria));
            sendJsonResponse(response, contactsPageView);
        } catch (ApplicationException e) {
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
            int fieldIdx = fieldNames.indexOf(param);
            if (fieldIdx != -1){
                if (allFields[fieldIdx].getType() == Integer.class){
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, Integer.parseInt(request.getParameter(param)));
                }else if (allFields[fieldIdx].getType() == String.class){
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, request.getParameter(param));
                }else if(allFields[fieldIdx].getType() == LocalDate.class){
                    allFields[fieldIdx].setAccessible(true);
                    allFields[fieldIdx].set(searchCriteria, DateFormatter.parseDate(request.getParameter(param)));
                }
            }
        }
        return searchCriteria;
    }

    private void getContact(HttpServletRequest request, HttpServletResponse response){
        try {
            ContactFull contact = service.get(getIdFromPath(request));
            ContactView view = ViewHelper.prepareContactView(contact);
            sendJsonResponse(response, view);
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        if (request.getPathInfo() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendJsonResponse(response, new ApplicationException());
            return;
        }
        try {
            service.delete(getIdStrFromPath(request));
            sendJsonResponse(response, new MessageInfo(Message.CONTACT_DELETED));
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
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

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ContactServiceImpl(dataSource);
    }

}