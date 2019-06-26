package ru.nsu.fit.fedyaeva.template.processors;

import java.util.Map;

public interface TemplateProcessor {
    void fillTemplate(StringBuilder s, Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) throws ru.nsu.fit.fedyaeva.template.ArgumentNotFoundException;
}
