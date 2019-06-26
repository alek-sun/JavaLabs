package ru.nsu.fit.fedyaeva.template.processors;

import java.util.Map;

public class StringProcessor implements TemplateProcessor {

    private String s;

    public StringProcessor(String str) {
        s = str;
    }

    @Override
    public void fillTemplate(StringBuilder text, Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) {
        text.append(s);
    }
}
