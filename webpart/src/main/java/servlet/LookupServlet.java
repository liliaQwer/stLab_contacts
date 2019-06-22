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
public class LookupServlet extends HttpServlet {

    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LookupService service = new LookupServiceImpl(dataSource);
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
}
