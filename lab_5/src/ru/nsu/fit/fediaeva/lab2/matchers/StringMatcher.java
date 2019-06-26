package ru.nsu.fit.fediaeva.lab2.matchers;

import java.util.Map;

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

    @Override
    public String getType() {
        return segm;
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
    public boolean equals(SegmentMatcher s) {
        return s.getType().equals(segm);
    }


}
