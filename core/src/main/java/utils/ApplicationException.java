package utils;

public class ApplicationException extends Exception {
    public ApplicationException(String error){
        super(error);
    }
    public ApplicationException(){
        super("An error has occurred during processing request");
    }
}
