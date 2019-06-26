package ru.nsu.fit.fediaeva.lab2.matchers;

import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.pipeline.Handler;
import ru.nsu.fit.fediaeva.lab2.handlers.HttpHandler;
import ru.nsu.fit.fediaeva.lab2.pipeline.IncomingPipeline;
import ru.nsu.fit.fediaeva.lab2.pipeline.OutcomingPipeline;

import java.util.*;

/**
 * Tree which nodes are SegmentMatcher's and handlers's.
 * handlers's is equal with null everywhere besides tree leaves.
 * Each node has a list of branches
 */
public class MatcherTree {
    private SegmentMatcher segMatcher;
    private ArrayList<MatcherTree> branches;
    private HttpHandler handler;
    private IncomingPipeline incomingPipeline;
    private OutcomingPipeline outcomingPipeline;

    public MatcherTree() {
        segMatcher = new StringMatcher("root", null);
        branches = new ArrayList<>();
        handler = null;
        incomingPipeline = null;
        outcomingPipeline = null;
    }

    private MatcherTree(SegmentMatcher s) {
        segMatcher = s;
        branches = new ArrayList<>();
        handler = null;
        incomingPipeline = null;
        outcomingPipeline = null;
    }

    /**
     * Find equal tree node by SegmentMatcher
     * Used for filling tree.
     * @param s SegmentMatcher for which looking for equal matcher
     * @return  Tree node with equal matcher
     */
    private MatcherTree getEqualNode(SegmentMatcher s) {
        for (MatcherTree t : branches) {
            if (t.segMatcher.equals(s)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Register handlers by path
     * @param l Register path template as SegmentMatcher's
     * @param h Registered handlers
     */
     void registerHandler(ArrayList<SegmentMatcher> l, Handler h) {
        SegmentMatcher s = l.remove(0);
        if (s == null) {
            handler = h.getHttpHandler();
            incomingPipeline = h.getInPipeline();
            outcomingPipeline = h.getOutPipeline();
            return;
        }
        MatcherTree equal = getEqualNode(s);
        if (equal == null) {
            branches.add(new MatcherTree(s));
            branches.get(branches.size() - 1).registerHandler(l, h);
        } else {
            getActualDescription(s, equal);
            equal.registerHandler(l, h);
        }
    }

    /**
     * Replace not actual description in SegmentMatcher, if in the new list of SegmentMatcher's came actual information
     * @param s Matcher with actual description
     * @param equal Redacted tree node
     */
    private void getActualDescription(SegmentMatcher s, MatcherTree equal) {
        String newDescr = s.getDescription();
        SegmentMatcher oldMatcher = equal.getMatcher();
        if (!oldMatcher
                .getDescription()
                .equals(newDescr)) {
            oldMatcher.setDescr(newDescr);
        }
    }

    /**
     * Get handlers from tree by path
     * @param pathArr Path
     * @param info Map for path information
     * @return  handlers
     * @throws NotFound
     * Throws :
     *  *    if came incorrect path (longer, than tree branch)
     *  *    if handler not registered
     *  *    if registered null-handler
     */
    public Handler getHandler(ArrayList<String> pathArr, Map<String, String> info) throws NotFound {
        String segment = pathArr.remove(0);
        if (segment == null) {
            if (handler == null) {    //  if found handler for directory, or someone register null handler
                throw new NotFound();
            } else {
                return new Handler(handler, incomingPipeline, outcomingPipeline);
            }
        }   //  segment != null
        if (branches.isEmpty()) {   //  if handler for this remainder of this path is absent
            throw new NotFound();
        }
        MatcherTree candidate = null;
        for (MatcherTree t : branches) {
            SegmentMatcher m = t.getMatcher();
            if (m.match(segment, info)) {
                if (m.getPriority() == 0) {
                    candidate = t;
                    continue;
                }
                return t.getHandler(pathArr, info);
            }
        }
        if (candidate != null) {
            return candidate.getHandler(pathArr, info);
        }
        throw new NotFound();   // if no suitable handler
    }

    private SegmentMatcher getMatcher() {
        return segMatcher;
    }
}
