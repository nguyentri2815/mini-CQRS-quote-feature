package com.example.quoteservice.common.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}