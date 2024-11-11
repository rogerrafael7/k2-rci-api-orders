package k2.rci.api.orders.application.providers;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import k2.rci.api.orders.infra.shared.exceptions.ServerException;

import java.util.logging.Logger;

@Provider
public class GlobalExceptionsHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionsHandler.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        LOG.severe(exception.getClass().getName());
        LOG.severe(exception.getMessage());

        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "Internal server error";

        if (exception instanceof ServerException serverException) {
            statusCode = switch (serverException.getCustomCause()) {
                case NOT_FOUND -> Response.Status.NOT_FOUND.getStatusCode();
                case BAD_REQUEST -> Response.Status.BAD_REQUEST.getStatusCode();
                case INTERNAL_SERVER_ERROR -> Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            };
            message = exception.getMessage();
        } else if (exception instanceof ClientErrorException) {
            statusCode = ((ClientErrorException) exception).getResponse().getStatus();
            message = exception.getMessage();
        }

        return Response.status(statusCode)
                .entity(new ErrorResponse(message, statusCode))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    static record ErrorResponse(String message, int statusCode) {
    }
}