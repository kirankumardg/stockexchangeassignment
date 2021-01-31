package org.com.exception;


public class OrderException extends Exception {
    private static final long serialVersionUID = 6111010203853573098L;

    public OrderException(String msg) {
        super(msg);
    }
}