package view;

import model.Attachment;

import java.io.Serializable;
import java.util.List;

public class AttachmentDetails implements Serializable {
    List<Integer> deletedIds;
    List<Attachment> attachmentsList;

    public AttachmentDetails() {
    }

    public List<Integer> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public List<Attachment> getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List<Attachment> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }
}
