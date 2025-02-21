package com.gustavo.nlw.eventos.exception;

public class SubscriptionConflictException extends RuntimeException{
    public SubscriptionConflictException(String msg){
        super(msg);
    }
}
