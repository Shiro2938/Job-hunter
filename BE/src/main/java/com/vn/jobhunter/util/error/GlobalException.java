package com.vn.jobhunter.util.error;

import com.vn.jobhunter.domain.Response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {InvalidException.class,
            BadCredentialsException.class, MissingRequestCookieException.class
    })
    public ResponseEntity<RestResponse<Object>> handleException(Exception ex) {
        RestResponse<Object> response = new RestResponse<>();
        response.setError("Exception occurs...");

        response.setMessage(ex.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleException(MethodArgumentNotValidException ex) {
        RestResponse<Object> response = new RestResponse<Object>();
        response.setError("Exception occurs...");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage()).toList();

        if (errors.size() > 1) {
            response.setMessage(errors);
        } else response.setMessage(errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(ex.getMessage());
        res.setError("404 NOT FOUND. URL may not exist...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {BadJwtException.class})
    public ResponseEntity<RestResponse<Object>> handleBadJWTE(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setMessage(ex.getMessage());
        res.setError("Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không truyền JWT ở header)...");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(value = {StorageException.class})
    public ResponseEntity<RestResponse<Object>> handleUploadFileException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Upload File exception occurs...");
        res.setMessage(ex.getMessage());

        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}
