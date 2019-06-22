package view;

import model.Attachment;

import java.util.List;

public class AttachmentDetails {
    List<Integer> deletedIds;
    List<Attachment> attachmentList;

    public AttachmentDetails() {
    }

    public List<Integer> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
