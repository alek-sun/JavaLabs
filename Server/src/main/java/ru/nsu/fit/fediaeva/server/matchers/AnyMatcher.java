package ru.nsu.fit.fediaeva.server.matchers;

import java.util.Map;
import java.util.Objects;

public class AnyMatcher implements SegmentMatcher {
    private String description;

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
    public boolean match(String segment, Map<String, String> pathInfo) {
        if (description != null) {
            pathInfo.put(description, segment);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
