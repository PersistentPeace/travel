package exception;

public class PasswordErrorException extends Exception{
    public PasswordErrorException() {
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }
}
