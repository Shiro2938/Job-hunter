package com.vn.jobhunter.util.error;

import com.vn.jobhunter.domain.Response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {InvalidException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse> handleException(Exception ex) {
        RestResponse response = new RestResponse();
        response.setError("Exception occurs...");
        response.setMessage(ex.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse> handleException(MethodArgumentNotValidException ex) {
        RestResponse response = new RestResponse();
        response.setError("Exception occurs...");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage()).toList();

        if (errors.size() > 1) {
            response.setMessage(errors);
        } else response.setMessage(errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
