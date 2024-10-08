package com.ksm.imgtouart.exceptions.com;

public class COMPortOpenException extends RuntimeException {
    public COMPortOpenException() {
        super("Failed to open COM port.");
    }
}