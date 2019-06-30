package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.LookupService;
import service.LookupServiceImpl;
import utils.ApplicationException;
import view.LookupsView;
import view.ViewHelper;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/lookups"})
public class LookupServlet extends HttpServlet implements JsonSendable{

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    private LookupService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LookupsView lookupsView = null;
        try {
            lookupsView = ViewHelper.prepareLookupsView(service.getGenderList(),
                    service.getMaritalStatusList(), service.getPhoneTypesList());
            sendJsonResponse(response, lookupsView);
        } catch (ApplicationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        service = new LookupServiceImpl(dataSource);
    }
}
