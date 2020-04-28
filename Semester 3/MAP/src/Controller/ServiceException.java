package Controller;

public class ServiceException extends  Exception {
    private String error;

    public ServiceException(String error){
        super(error);
        this.error = error;
    }
    public String getError(){return error;}
}
