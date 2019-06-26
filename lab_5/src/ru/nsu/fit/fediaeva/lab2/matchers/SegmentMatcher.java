package ru.nsu.fit.fediaeva.lab2.matchers;

import java.util.Map;

public interface SegmentMatcher {
    int getPriority();

    void setDescr(String d);

    String getDescription();

    String getType();

    boolean match(String segment, Map<String, String> pathInfo);

    boolean equals(SegmentMatcher s);
}
