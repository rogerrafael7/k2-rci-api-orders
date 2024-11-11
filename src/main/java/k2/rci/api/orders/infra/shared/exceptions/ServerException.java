package k2.rci.api.orders.infra.shared.exceptions;

public class ServerException extends RuntimeException {

    SERVER_EXCEPTION_CAUSE cause;

    public ServerException(String message, SERVER_EXCEPTION_CAUSE cause) {
        super(message);
        this.cause = cause;
    }

    public SERVER_EXCEPTION_CAUSE getCustomCause() {
        return cause;
    }
}
