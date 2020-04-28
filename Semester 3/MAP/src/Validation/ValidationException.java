package Validation;

public class ValidationException extends Exception {
    String error;
    public ValidationException(String error){
        super(error);
        this.error = error;
    }
    public String getError(){
        return error;
    }
}
