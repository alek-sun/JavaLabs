package ru.nsu.fit.fediaeva.server.matchers;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class PredicateMatcher implements SegmentMatcher {
    private final String type = getClass().toString();
    private String descr;
    private Predicate<String> predicate;

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
    public boolean match(String segment, Map<String, String> pathInfo) {
        if (descr != null) {
            pathInfo.put(descr, segment);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredicateMatcher that = (PredicateMatcher) o;
        return Objects.equals(predicate, that.predicate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(predicate, type);
    }
}
