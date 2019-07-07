package servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import upload.FileHelper;
import utils.ApplicationException;
import utils.Message;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(urlPatterns = {"/attachments/*"})
public class AttachmentServlet extends HttpServlet implements JsonSendable{
    private final static Logger logger = LogManager.getLogger(AttachmentServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String requestedFile = req.getPathInfo();
        if (requestedFile == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendJsonResponse(resp, new ApplicationException(Message.FILE_NOT_FOUND));
            return;
        }
        requestedFile = requestedFile.replaceAll("/","\\\\");
        //exclude directory
        String fileName = requestedFile.substring(requestedFile.lastIndexOf(File.separator));
        String realFileName = fileName.substring(fileName.indexOf("~") + 1);
        resp.setHeader("Content-disposition", "attachment;" + realFileName);

        OutputStream out = resp.getOutputStream();
        FileHelper fileHelper = FileHelper.getInstance();
        try {
            fileHelper.readAttachment(requestedFile, out);
            out.flush();
        }catch(IOException e){
            logger.error(e);
            resp.setHeader("Content-disposition", "inline");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendJsonResponse(resp, new ApplicationException(Message.FILE_NOT_FOUND));
        }
    }
}
