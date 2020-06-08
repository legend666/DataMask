package cn.com.taiji.fsb.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ExceptionHandlerAdvice{

    @ExceptionHandler(value = {Exception.class})
    public String resultMsg(){
        return "status/error";
    }
}
