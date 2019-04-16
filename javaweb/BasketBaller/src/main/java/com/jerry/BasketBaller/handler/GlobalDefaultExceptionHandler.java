package com.jerry.BasketBaller.handler;

import com.jerry.BasketBaller.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse<String> defaultExceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("全局异常:{}",e.getMessage());
        return ServerResponse.createByErrorMessage(e.getMessage());
    }
}
