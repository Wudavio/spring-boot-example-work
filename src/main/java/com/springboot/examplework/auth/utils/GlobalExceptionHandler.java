package com.springboot.examplework.auth.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.springboot.examplework.core.dto.AjaxDTO;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AjaxDTO> handleValidationException(MethodArgumentNotValidException ex) {
        AjaxDTO dto = new AjaxDTO();
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        dto.setData(errorMap);
        dto.setMessage("參數驗證失敗");
        dto.setStatusFail();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
}