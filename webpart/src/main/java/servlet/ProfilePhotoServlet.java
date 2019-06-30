package servlet;

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
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFile = req.getPathInfo();
        if (requestedFile != null){
            requestedFile = requestedFile.replaceAll("/","\\\\");
        }
        System.out.println("requestedFile2=" + requestedFile);
        OutputStream out = resp.getOutputStream();
        FileHelper fileHelper = FileHelper.getInstance();
        try {
            fileHelper.readProfilePhoto(requestedFile, out);
            out.flush();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
