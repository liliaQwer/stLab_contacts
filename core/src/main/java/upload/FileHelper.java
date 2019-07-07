package upload;

import model.Attachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class FileHelper {

    private final static Logger logger = LogManager.getLogger(FileHelper.class);
    private static String attachmentLocation;
    private static String profilePhotoLocation;
    private static String defaultProfile;
    private static FileHelper instance;
    private FileHelper(){

    }
    public static FileHelper getInstance(){
        if(instance == null){
            instance = new FileHelper();
            init();
        }
        return instance;
    }

    private static void init(){
        try (InputStream input = FileHelper.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            attachmentLocation = prop.getProperty("attachment.location");
            profilePhotoLocation = prop.getProperty("profilePhoto.location");
            defaultProfile = prop.getProperty("default.profilePhoto.file");
            File uploadDir = new File(attachmentLocation);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            uploadDir = new File(profilePhotoLocation);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            System.out.println("attaLoc=" + attachmentLocation);
            System.out.println("profLoc=" + profilePhotoLocation);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
    public boolean uploadProfilePhoto(FileItem item, int contactId){
        String fileName = item.getName();
        System.out.println("fileName = " + fileName);
        //build path: propertyLocation/contactId/fileName
        StringBuilder filePathSb = new StringBuilder();
        filePathSb.append(profilePhotoLocation).append(File.separator).append(contactId);
        File profileDir = new File(filePathSb.toString());
        if (profileDir.exists()) {
            File[] profiles = profileDir.listFiles();
            for (File profile : profiles) {
                profile.delete();
            }
        }

        new File(filePathSb.toString()).mkdirs();
        filePathSb.append(File.separator).append(fileName);
        File storeFile = new File(filePathSb.toString());
        try {
            item.write(storeFile);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    public void readAttachment(String requestedFile, OutputStream out) throws IOException {
        File file = new File(attachmentLocation + requestedFile);
        if (!file.exists()){
            throw new FileNotFoundException();
        }
        try (InputStream in = new FileInputStream(file)){
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
        }catch(IOException e){
            logger.error(e);
            throw e;
        }
    }

    public boolean uploadAttachment(FileItem item, Attachment attachment){
        String fileName = item.getName();
        System.out.println("fileName = " + fileName);
        //build path: propertyLocation/contactId/uuid-fileName
        StringBuilder filePathSb = new StringBuilder();
        filePathSb.append(attachmentLocation).append(File.separator).append(attachment.getContactId());
        File contactDir = new File(filePathSb.toString());
        contactDir.mkdirs();
        //delete old attachment if exists
        File[] files = contactDir.listFiles();
        Arrays.stream(files).forEach(file -> {
            if (file.getName().startsWith(attachment.getUuid())) {
                file.delete();
            }
        });
        filePathSb.append(File.separator).append(attachment.getUuid()).append("~").append(fileName);
        System.out.println(filePathSb.toString());
        File storeFile = new File(filePathSb.toString());
        try {
            item.write(storeFile);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    public void deleteAttachment(Attachment attachment) {
        StringBuilder filePathSb = new StringBuilder();
        filePathSb.append(attachmentLocation).append(File.separator).append(attachment.getContactId());
        filePathSb.append(File.separator).append(attachment.getUuid()).append("~").append(attachment.getFileName());
        System.out.println(filePathSb.toString());
        File fileToDelete = new File(filePathSb.toString());
        if (fileToDelete.exists()){
            fileToDelete.delete();
        }
    }

    public void readProfilePhoto(String requestedFile, OutputStream out) throws IOException {
        File file = new File(profilePhotoLocation + requestedFile);
        if (!file.exists()){
            file = new File(profilePhotoLocation + File.separator + defaultProfile);
        }
        try (InputStream in = new FileInputStream(file)){
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
        }catch(IOException e){
            e.printStackTrace();
            throw e;
        }

    }
}
