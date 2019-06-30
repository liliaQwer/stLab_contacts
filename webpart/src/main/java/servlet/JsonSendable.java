package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public interface JsonSendable {
    default void sendJsonResponse(HttpServletResponse response, Object view) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, view);
            System.out.println(mapper.writeValueAsString(view));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
