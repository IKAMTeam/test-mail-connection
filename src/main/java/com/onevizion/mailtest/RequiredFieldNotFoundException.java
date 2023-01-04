package com.onevizion.mailtest;

import java.util.HashSet;
import java.util.Properties;

public class RequiredFieldNotFoundException extends RuntimeException {
    public RequiredFieldNotFoundException(String name) {
        super(name + " is not found");
    }
}
