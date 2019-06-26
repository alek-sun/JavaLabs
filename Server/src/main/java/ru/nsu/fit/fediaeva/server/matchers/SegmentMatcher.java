package ru.nsu.fit.fediaeva.server.matchers;

import java.util.Map;

public interface SegmentMatcher {
    int getPriority();

    void setDescr(String d);

    String getDescription();

    boolean match(String segment, Map<String, String> pathInfo);

    boolean equals(Object o);
}
