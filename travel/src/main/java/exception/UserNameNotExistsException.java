package exception;

public class UserNameNotExistsException extends Exception {
    public UserNameNotExistsException() {
    }

    public UserNameNotExistsException(String msg) {
        super(msg);
    }
}
