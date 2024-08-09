package org.servicenow.leaderelection.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FileStorageException extends RuntimeException {

    HttpStatus httpStatus;

    String message;

    public FileStorageException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
