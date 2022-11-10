package com.badfox.basicpro.advice;

import com.badfox.basicpro.comm.BaseException;
import com.badfox.basicpro.comm.ResultCode;
import com.badfox.basicpro.comm.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {"com.badfox.basicpro"})
public class ControllerResponseAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        /**
         * response是ResultVo对象，或者注释了 NotControllerResponseAdvice都不包装
         */
        return !returnType.getParameterType().isAssignableFrom(ResultVo.class)
                || returnType.hasMethodAnnotation(NotControllerResponseAdvice.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValueAsString(new ResultVo(body));
            } catch (JsonProcessingException e) {
                throw new BaseException(ResultCode.RESPONSE_PACK_ERROR, e.getMessage());
            }
        }
        return new ResultVo(body);
    }
}
