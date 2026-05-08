package com.example.quoteservice.quote.common.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}