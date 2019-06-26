package ru.nsu.fit.fedyaeva.template;

public class IncorrectFormException extends Exception {
    private String descr;

    IncorrectFormException(String description) {
        descr = description;
    }

    public String getMessage() {
        return descr;
    }
}
