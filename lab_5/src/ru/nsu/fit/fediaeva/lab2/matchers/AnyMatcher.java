package ru.nsu.fit.fediaeva.lab2.matchers;

import java.util.Map;

public class AnyMatcher implements SegmentMatcher {
    private String description;
    private final String type = getClass().toString();

    public AnyMatcher(String descr) {
        description = descr;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void setDescr(String d) {
        description = d;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean match(String segment, Map<String, String> pathInfo) {
        if (description != null) {
            pathInfo.put(description, segment);
        }
        return true;
    }

    @Override
    public boolean equals(SegmentMatcher s) {
        return s.getType().equals(type);
    }

}
