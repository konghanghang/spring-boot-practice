package com.test.exception;

import com.test.model.MessageCode;
import com.test.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResultModel noHandlerFoundExceptionHandler(NoHandlerFoundException e, HttpServletRequest request) {
        return new ResultModel().code(HttpStatus.NOT_FOUND.value()).message(String.format("url:%s not found", request.getServletPath()));
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResultModel authorizeExceptionHandler(AuthorizeException e, HttpServletRequest request, HttpServletResponse response) {
        logger.error("AuthorizeException, url:{}, method:{}, code:{}, message:{}", request.getServletPath(), request.getMethod(),
                e.getMessageCode().getCode(), e.getMessageCode().getMessage());
        response.setStatus(e.getMessageCode().getCode());
        return ResultModel.isFail(e.getMessageCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel exceptionHandler(Exception e, HttpServletRequest request) {
        logger.error("Exception, url:{}, message:{}", request.getRequestURI(), e.getMessage());
        return ResultModel.isFail(MessageCode.INTERNET_ERROR_500);
    }

}
