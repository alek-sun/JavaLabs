package ru.nsu.fit.fediaeva.server.matchers;

import java.util.Map;
import java.util.Objects;

/**
 * Checks strings for equivalence
 */

public class StringMatcher implements SegmentMatcher {
    private String segm;
    private String descr;

    public StringMatcher(String segment, String description) {
        segm = segment;
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
     * @return Matched is string equals with internal string
     */
    @Override
    public boolean match(String segment, Map<String, String> pathInfo) {
        if (!segm.equals(segment)) {
            return false;
        }
        if (descr != null && pathInfo != null) {
            pathInfo.put(descr, segment);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringMatcher that = (StringMatcher) o;
        return Objects.equals(segm, that.segm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segm);
    }
}
