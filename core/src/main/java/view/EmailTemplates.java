package view;

import email.TemplateType;

import java.util.ArrayList;

public class EmailTemplates {
    ArrayList<TemplateType> templateList;

    public EmailTemplates() {
    }

    public EmailTemplates(ArrayList<TemplateType> templateList) {
        this.templateList = templateList;
    }

    public ArrayList<TemplateType> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(ArrayList<TemplateType> templateList) {
        this.templateList = templateList;
    }
}
