package com.ksm.imgtouart.exceptions.cli;

import org.apache.commons.cli.Option;

public class IllegalArgumentValueException extends RuntimeException {
    public IllegalArgumentValueException(Option option) {
        super("Illegal value for option: " + option.getKey());
    }
}
