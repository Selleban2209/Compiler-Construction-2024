package errors;

public class TypeError extends Exception {

    private String message;

    public TypeError(String message) {
        super(message);
      
    }
    
}
