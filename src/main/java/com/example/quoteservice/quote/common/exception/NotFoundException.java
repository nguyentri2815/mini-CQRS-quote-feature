package com.example.quoteservice.quote.common.exception;

public class NotFoundException extends RuntimeException{
    public  NotFoundException(String message){
        super(message);
    }
}