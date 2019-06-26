package ru.nsu.fit.fediaeva.server.matchers;

import java.util.Map;
import java.util.Objects;

public class IntMatcher implements SegmentMatcher {
    private String descr;

    public IntMatcher(String description) {
        descr = description;
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


    /**
     * @param segment Checked string
     * @return *True if segment is a decimal number,
     * *False otherwise
     */
    @Override
    public boolean match(String segment, Map<String, String> pathInfo) {
        try {
            Integer.parseInt(segment);
        } catch (NumberFormatException e) {
            return false;
        }
        if (descr != null) {
            pathInfo.put(descr, segment);
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
        return Objects.hash(descr);
    }
}
