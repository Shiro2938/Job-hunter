package com.vn.jobhunter.util;

import com.vn.jobhunter.domain.Response.RestResponse;
import com.vn.jobhunter.util.annotation.APIMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse _response = ((ServletServerHttpResponse) response).getServletResponse();

        if (body instanceof String) {
            return body;
        }

        if (_response.getStatus() >= 400) {
            return body;
        } else {
            RestResponse restResponse = new RestResponse();
            restResponse.setStatusCode(_response.getStatus());
            restResponse.setData(body);

            APIMessage message = returnType.getMethodAnnotation(APIMessage.class);

            restResponse.setMessage(message != null ? message.value() : "CALL API SUCCESS");
            return restResponse;
        }

    }
}
