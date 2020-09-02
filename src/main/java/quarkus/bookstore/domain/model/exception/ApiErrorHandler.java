package quarkus.bookstore.domain.model.exception;

import lombok.extern.jbosslog.JBossLog;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

import static quarkus.bookstore.application.Constants.LOG_FORMAT_ERROR;

@Provider
@JBossLog
public class ApiErrorHandler implements ExceptionMapper<BusinessException> {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(BusinessException exception) {
        String method = resourceInfo.getResourceMethod().getName();
        String clazz = resourceInfo.getResourceClass().getName();
        String path = uriInfo.getAbsolutePath().getPath();

        log.error(String.format(LOG_FORMAT_ERROR, clazz, method, path, exception.getErrorCode(), exception.getErrorMessage()));

        return Response.status(Response.Status.BAD_REQUEST).entity(
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(Response.Status.BAD_REQUEST)
                        .code(exception.getErrorCode())
                        .message(exception.getErrorMessage())
                        .path(path)
                        .build()
        ).build();
    }

}
