package com.test.meeting.exception;

import com.test.meeting.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public Response handleServletRequestException(ServletRequestBindingException srbExc) {
        return Response.build().withErrors(new Error(srbExc.getMessage())).create();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response constraintException(ConstraintViolationException exception) {
        List<Error> errorList = new ArrayList<Error>();
        exception.getConstraintViolations().forEach(constraintViolation ->
                errorList.add(new Error("Validation Error",
                                        String.format("%s - %s", constraintViolation.getPropertyPath(), constraintViolation.getMessage())))
        );
        return Response.build().withErrors(errorList.toArray(new Error[0])).create();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoDataFoundException.class)
    public Response NoDataFoundException(NoDataFoundException exception) {
        return Response.build()
                .withErrors(exception.getError())
                .create();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BaseException.class)
    public Response baseException(BaseException exception) {
        return Response.build()
                .withErrors(exception.getError())
                .create();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Response handleException(Exception exception) {

        log.error(exception.getMessage(), exception);
        return Response.build().withErrors(new Error(exception.getMessage())).create();
    }
}
