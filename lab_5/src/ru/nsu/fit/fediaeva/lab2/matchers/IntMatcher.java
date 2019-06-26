package ru.nsu.fit.fediaeva.lab2.matchers;

import java.util.Map;

public class IntMatcher implements SegmentMatcher {
    private String descr;
    private final String type = getClass().toString();

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

    @Override
    public String getType() {
        return type;
    }

    /**
     * @param segment Checked string
     * @return
     * *True if segment is a decimal number,
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
    public boolean equals(SegmentMatcher s) {
        return s.getType().equals(type);
    }
}
