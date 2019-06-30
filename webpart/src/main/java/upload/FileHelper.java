package upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
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
    public static boolean upload(FileItem item, int contactId){
        //System.out.println("content type = " + item.getContentType());
        String fileName = item.getName();
        System.out.println("fileName = " + fileName);
        //build path: propertyLocation/contactId/fileName
        StringBuilder filePathSb = new StringBuilder();
        if (item.getFieldName().equals("profilePhoto")){
            filePathSb.append(profilePhotoLocation).append(File.separator).append(contactId);
            File profileDir = new File(filePathSb.toString());
            if (profileDir.exists()){
                File[] profiles = profileDir.listFiles();
                for(File profile: profiles){
                    profile.delete();
                }
            }
        }else{
            filePathSb.append(attachmentLocation).append(File.separator).append(contactId);
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

    public static void readAttachment(String requestedFile, OutputStream out) throws IOException {
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

    public static void readProfilePhoto(String requestedFile, OutputStream out) throws IOException {
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
