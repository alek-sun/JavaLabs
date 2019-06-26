package ru.nsu.fit.fedyaeva.template.processors;

import ru.nsu.fit.fedyaeva.template.ArgumentNotFoundException;

import java.util.Map;

public class ValueProcessor implements TemplateProcessor {
    private String key;

    public ValueProcessor(String k) {
        key = k;
    }

    @Override
    public void fillTemplate(StringBuilder sb, Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) throws
            ArgumentNotFoundException {
        if (values == null || values.get(key) == null) {
            throw new ArgumentNotFoundException(key);
        }
        String s = values.get(key);
        sb.append(s);
    }
}
