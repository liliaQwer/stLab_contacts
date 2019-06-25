package servlet;

import upload.FileHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(urlPatterns = {"/attachments/*"})
public class AttachmentServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFile = req.getPathInfo();
        if (requestedFile == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        requestedFile = requestedFile.replaceAll("/","\\\\");
        String fileName = requestedFile.substring(requestedFile.lastIndexOf(File.separator));
        resp.setHeader("Content-disposition", "attachment;" + fileName);
        OutputStream out = resp.getOutputStream();
        FileHelper fileHelper = FileHelper.getInstance();
        try {
            fileHelper.readAttachment(requestedFile, out);
            out.flush();
        }catch(FileNotFoundException e){
            resp.setHeader("Content-disposition", "inline");
            resp.getOutputStream().print("Sorry, your file was not found...");
        }catch(IOException ex){
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
