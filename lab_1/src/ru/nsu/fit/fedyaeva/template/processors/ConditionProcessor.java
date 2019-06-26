package ru.nsu.fit.fedyaeva.template.processors;

import ru.nsu.fit.fedyaeva.template.ArgumentNotFoundException;

import java.util.Map;

public class ConditionProcessor implements TemplateProcessor {

    private String key;
    private String str;

    public ConditionProcessor(String k, String s) {
        key = k;
        str = s;
    }

    @Override
    public void fillTemplate(StringBuilder sb, Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) throws ArgumentNotFoundException {
        if (conditions == null || conditions.get(key) == null) {
            throw new ArgumentNotFoundException(key);
        }
        boolean b = conditions.get(key);
        if (b) {
            sb.append(str);
        }
    }
}
