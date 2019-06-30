package view;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;

public class SendEmailData {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    ArrayList<ContactEmail> emailList;
    String subject;
    String text;
    String template;

    public SendEmailData() {
    }

    public ArrayList<ContactEmail> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<ContactEmail> emailList) {
        this.emailList = emailList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
