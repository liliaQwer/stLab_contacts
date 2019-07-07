package view;

import model.Attachment;
import utils.DateFormatter;

public class AttachmentView {
    private int id;
    private int contactId;
    private String fileName;
    private String uploadDate;
    private String comment;
    private String uuid;

    public AttachmentView(Attachment attachment) {
        this.uploadDate = DateFormatter.formatDate(attachment.getUploadDate());
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.comment = attachment.getComment();
        this.contactId = attachment.getContactId();
        this.uuid = attachment.getUuid();
    }

    public AttachmentView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
