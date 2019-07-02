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
        String fileName = requestedFile.substring(requestedFile.lastIndexOf(File.separator));
        resp.setHeader("Content-disposition", "attachment;" + fileName);
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
