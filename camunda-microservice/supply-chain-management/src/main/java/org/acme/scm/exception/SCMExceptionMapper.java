package org.acme.scm.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.acme.scm.response.ErrorResponse;

import java.util.UUID;

import static org.acme.scm.constants.MessagingConstants.PROCESSING_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@Provider
@Slf4j
public class SCMExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        String errorId = UUID.randomUUID().toString();
        log.error("Processing error occurred with errorId[{}]: ", errorId, e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), PROCESSING_ERROR_MSG, errorId))
                .build();
    }
}
