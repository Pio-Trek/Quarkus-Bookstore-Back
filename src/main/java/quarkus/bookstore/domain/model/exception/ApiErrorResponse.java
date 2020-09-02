package quarkus.bookstore.domain.model.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Getter
@Builder
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private Response.Status status;
    private String code;
    private String message;
    private String path;

}
