package com.badfox.basicpro.advice;

import com.badfox.basicpro.comm.BaseException;
import com.badfox.basicpro.comm.ResultCode;
import com.badfox.basicpro.comm.ResultVo;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({BindException.class})
    public ResultVo MethodArgumentNotValidExceptionHandler(BindException e) {
        /**
         * 从异常对象中拿到 ObjectError 对象
         */
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResultVo(ResultCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ResultVo BaseExceptionHandler(BaseException e) {
        return new ResultVo(e.getCode(), e.getMsg(), e.getMessage());
    }
}
