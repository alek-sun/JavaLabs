package ru.nsu.fit.fediaeva.lab2.matchers;

import java.util.Map;
import java.util.function.Predicate;

public class PredicateMatcher implements SegmentMatcher {
    private String descr;
    private Predicate<String> predicate;
    private final String type = getClass().toString();

    PredicateMatcher(Predicate<String> pred, String description) {
        descr = description;
        predicate = pred;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void setDescr(String d) {
        descr = d;
    }

    @Override
    public String getDescription() {
        return descr;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean match(String segment, Map<String, String> pathInfo) {
        if (descr != null) {
            pathInfo.put(descr, segment);
        }
        return true;
    }

    @Override
    public boolean equals(SegmentMatcher s) {
        return s.getType().equals(type);
    }
}
