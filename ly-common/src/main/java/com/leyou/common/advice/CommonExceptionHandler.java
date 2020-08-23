package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResult> handleException(LyException lyException){
        ExceptionEnum exceptionEnum = lyException.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode())
                .body(new ExceptionResult( exceptionEnum.getCode() , exceptionEnum.getMessage() ));
    }
}
