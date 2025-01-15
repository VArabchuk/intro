package mate.academy.intro.exception;

public class FieldValidationException extends RuntimeException {

    public FieldValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
