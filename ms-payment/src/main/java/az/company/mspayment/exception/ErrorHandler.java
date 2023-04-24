package az.company.mspayment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.company.mspayment.model.constant.ExceptionConstant.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleInternal(Exception exception){
        log.error("Exception" + exception);
        return new ResponseEntity<>(new ExceptionResponse(UNEXPECTED_EXCEPTION_CODE,UNEXPECTED_EXCEPTION_MESSAGE), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handleNotFound(NotFoundException exception){
        log.error("No Found Exception" + exception);
        return new ExceptionResponse(COUNTRY_NOT_FOUND_CODE,COUNTRY_NOT_FOUNT_MESSAGE);
    }
}
