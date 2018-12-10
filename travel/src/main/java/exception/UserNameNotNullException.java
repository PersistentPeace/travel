package exception;

public class UserNameNotNullException extends Exception {
    public UserNameNotNullException() {
    }

    public UserNameNotNullException(String msg) {
        super(msg);
    }
}
