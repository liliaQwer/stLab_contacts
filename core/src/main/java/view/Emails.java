package view;

import model.IdDescription;

import java.util.ArrayList;

public class Emails {
    private ArrayList<ContactEmail> emailList= new ArrayList<>();

    public Emails() {
    }

    public ArrayList<ContactEmail> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<ContactEmail> emailList) {
        this.emailList = emailList;
    }
}
