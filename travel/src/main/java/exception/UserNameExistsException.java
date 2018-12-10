package exception;

public class UserNameExistsException extends Exception {
    public UserNameExistsException() {
    }

    public UserNameExistsException(String msg) {
        super(msg);
    }
}
