package utils;

public enum ContactStatus {
    DEACTIVATED('0'), ACTIVATED('1');
    private char status;
    ContactStatus (char status){
        this.status = status;
    }
    public char getStatus(){
        return status;
    }
}
