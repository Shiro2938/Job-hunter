package com.vn.jobhunter.domain.Response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private Object message;
    private String error;
    private T data;
}
