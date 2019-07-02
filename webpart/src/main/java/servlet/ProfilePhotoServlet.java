package servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import upload.FileHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(urlPatterns = {"/profilePhotos/*"})
public class ProfilePhotoServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(ProfilePhotoServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFile = req.getPathInfo();
        if (requestedFile != null){
            requestedFile = requestedFile.replaceAll("/","\\\\");
        }
        OutputStream out = resp.getOutputStream();
        FileHelper fileHelper = FileHelper.getInstance();
        try {
            fileHelper.readProfilePhoto(requestedFile, out);
            out.flush();
        }catch(IOException ex){
            logger.error(ex);
        }
    }
}
