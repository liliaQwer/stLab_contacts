package view;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDetails implements Serializable {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<Integer> deletedIds;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<AttachmentView> attachmentsList;

    public AttachmentDetails() {
    }

    public List<Integer> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public List<AttachmentView> getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List<AttachmentView> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }
}
