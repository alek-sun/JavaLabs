package ru.nsu.fit.fedyaeva.template.processors;

import ru.nsu.fit.fedyaeva.template.ArgumentNotFoundException;

import java.util.Map;

public class RepeatProcessor implements TemplateProcessor {

    private String key;
    private String repeatStr;

    public RepeatProcessor(String k, String str) {
        key = k;
        repeatStr = str;
    }

    @Override
    public void fillTemplate(StringBuilder sb, Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) throws ArgumentNotFoundException {
        if (iterations == null || iterations.get(key) == null) {
            throw new ArgumentNotFoundException(key);
        }
        int count = iterations.get(key);
        int i;
        for (i = 0; i < count; i++) {
            sb.append(repeatStr);
        }
    }
}
