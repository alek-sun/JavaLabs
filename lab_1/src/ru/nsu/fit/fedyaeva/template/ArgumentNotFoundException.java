package ru.nsu.fit.fedyaeva.template;

public class ArgumentNotFoundException extends Exception {
    private String argument;

    public ArgumentNotFoundException(String arg) {
        argument = arg;
    }

    public String getMessage() {
        return "Argument not found: " + argument;
    }
}
