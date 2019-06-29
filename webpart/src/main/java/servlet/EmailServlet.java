package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import email.EmailTemplate;
import model.IdDescription;
import service.ContactService;
import service.ContactServiceImpl;
import utils.ApplicationException;
import view.Emails;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/email"})
public class EmailServlet extends HttpServlet {
    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("pathInfo=" + pathInfo);
        if ( request.getParameter("ids") == null){
            loadEmailTemplates();
        }else{
            loadContactsEmail(response, request.getParameter("ids"));
        }
    }

    private void loadEmailTemplates(){
        try {
            EmailTemplate.getTemplates();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


//        try {
//            new EmailTemplate().sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

    private void loadContactsEmail(HttpServletResponse response, String contactsIdStr){
        System.out.println("contactIdstr= " + contactsIdStr);
        List<Integer> idList = Arrays.stream(contactsIdStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        Emails emails = new Emails();
        ContactService service = new ContactServiceImpl(dataSource);
        try {
            for(Integer id: idList){
                String email = service.get(id).getContact().getEmail();
                if (email != null){
                    emails.getEmailList().add(email);
                    System.out.println("email=" + email);
                }
            }
            sendJsonResponse(response, emails);
        } catch (ApplicationException e) {
            e.printStackTrace();
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
