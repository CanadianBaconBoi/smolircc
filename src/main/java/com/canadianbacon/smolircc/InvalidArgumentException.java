package com.canadianbacon.smolircc;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
