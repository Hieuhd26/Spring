package hanu.devteria.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    KEY_INVALID(1001, "Key validation is invalid"),
    USER_EXISTED(1002, "User existed!"),
    USERNAME_INVALID(1003, "User name must be at least 3 characterrs!"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characterrs!"),
    USER_NOT_EXISTED(1005, "User not existed !!"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    UNCATEGOIED_EXCEPTION(9999, "Uncategorize error");


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

}
