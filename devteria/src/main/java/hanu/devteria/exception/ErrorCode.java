package hanu.devteria.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    KEY_INVALID(1001, "Key validation is invalid",HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed!",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "User name must be at least 3 characterrs!",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characterrs!",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed !!",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Unauthorized",HttpStatus.FORBIDDEN),
    INVALID_DOB (1008, "Your age mus be at least {min}",HttpStatus.BAD_REQUEST),
    UNCATEGOIED_EXCEPTION(9999, "You do not have permission!!", HttpStatus.INTERNAL_SERVER_ERROR);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode=statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
