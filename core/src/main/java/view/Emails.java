package view;

import model.IdDescription;

import java.util.ArrayList;

public class Emails {
    private ArrayList<String> emailList= new ArrayList<>();

    public Emails() {
    }

    public ArrayList<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }
}
