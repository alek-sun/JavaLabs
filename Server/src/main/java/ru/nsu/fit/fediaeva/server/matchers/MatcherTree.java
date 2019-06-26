package ru.nsu.fit.fediaeva.server.matchers;

import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.HttpHandler;
import ru.nsu.fit.fediaeva.server.pipeline.Handler;
import ru.nsu.fit.fediaeva.server.pipeline.IncomingPipeline;
import ru.nsu.fit.fediaeva.server.pipeline.OutcomingPipeline;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.InInterceptor;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.OutInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tree which nodes are SegmentMatcher's and handlers's.
 * handlers's is equal with null everywhere besides tree leaves.
 * Each node has a list of branches
 */
public class MatcherTree {
    private SegmentMatcher segMatcher;
    private ArrayList<MatcherTree> branches;
    private Handler handler;

    public MatcherTree() {
        segMatcher = new StringMatcher("root", null);
        branches = new ArrayList<>();
        handler = new Handler();
    }

    private MatcherTree(SegmentMatcher s) {
        segMatcher = s;
        branches = new ArrayList<>();
        handler = new Handler();
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * Find equal tree node by SegmentMatcher
     * Used for filling tree.
     *
     * @param s SegmentMatcher for which looking for equal matcher
     * @return Tree node with equal matcher
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
     * Put into a tree list by path some InInterceptors
     */
    public void registerInInterceptors(List<SegmentMatcher> path, List<InInterceptor> stages) {
        if (path.isEmpty()) {
            handler.getInPipeline().addAll(stages);
            return;
        }
        if (!(path instanceof ArrayList)) {
            path = new ArrayList<>(path);
        }
        SegmentMatcher s = path.remove(0);
        MatcherTree equal = getEqualNode(s);
        if (equal == null) {
            MatcherTree newNode = new MatcherTree(s);
            branches.add(newNode);
            newNode.registerInInterceptors(path, stages);
        } else {
            equal.registerInInterceptors(path, stages);
        }
    }

    public void registerOutInterceptors(List<SegmentMatcher> path, List<OutInterceptor> stages) {
        if (path.isEmpty()) {
            handler.getOutPipeline().addAll(stages);
            return;
        }
        if (!(path instanceof ArrayList)) {
            path = new ArrayList<>(path);
        }
        SegmentMatcher s = path.remove(0);
        MatcherTree equal = getEqualNode(s);
        if (equal == null) {
            MatcherTree newNode = new MatcherTree(s);
            branches.add(newNode);
            newNode.registerOutInterceptors(path, stages);
        } else {
            equal.registerOutInterceptors(path, stages);
        }
    }

    /**
     * Register HttpHandler by path
     *
     * @param l       Register path template as SegmentMatcher's
     * @param h       Registered handler
     * @param inPipe  buffer of incoming interceptors for incoming pipeline
     * @param outPipe buffer of outcoming interceptors for outcoming pipeline
     */
    public void registerHandler(List<SegmentMatcher> l, HttpHandler h,
                                List<InInterceptor> inPipe, List<OutInterceptor> outPipe)
            throws ProgramException {
        if (l.isEmpty()) {
            handler = new Handler(h, new IncomingPipeline(inPipe), new OutcomingPipeline(outPipe));
            return;
        }
        if (!(l instanceof ArrayList)) {
            l = new ArrayList<>(l);
        }
        SegmentMatcher s = l.remove(0);
        MatcherTree equal = getEqualNode(s);
        if (!(inPipe instanceof ArrayList)) {
            inPipe = new ArrayList<>(inPipe);
        }
        if (!(outPipe instanceof ArrayList)) {
            outPipe = new ArrayList<>(outPipe);
        }
        inPipe.addAll(getHandler().getInPipeline());
        outPipe.addAll(getHandler().getOutPipeline());
        if (equal == null) {
            MatcherTree newNode = new MatcherTree(s);
            branches.add(newNode);
            newNode.registerHandler(l, h, inPipe, outPipe);
        } else {
            //getActualDescription(s, equal);
            equal.registerHandler(l, h, inPipe, outPipe);   //  дай Бог, заработает
        }
    }


   /* *
     * Replace not actual description in SegmentMatcher, if in the new list of SegmentMatcher's came actual information
     * @param s Matcher with actual description
     * @param equal Redacted tree node

    private void getActualDescription(SegmentMatcher s, MatcherTree equal) {
        String newDescr = s.getDescription();
        if (newDescr == null){
            return;
        }
        SegmentMatcher oldMatcher = equal.getMatcher();
        if (!oldMatcher
                .getDescription()
                .equals(newDescr)) {
            oldMatcher.setDescr(newDescr);
        }
    }*/

    /**
     * Get handlers from tree by path
     *
     * @param pathArr Path
     * @param info    Map for path information
     * @return handler with pipeline
     * @throws NotFound Throws :
     *                  *    if came incorrect path (longer, than tree branch)
     *                  *    if handler not registered
     *                  *    if registered null-handler
     */
    public Handler getHandler(ArrayList<String> pathArr, Map<String, String> info) throws NotFound {
        if (pathArr.isEmpty()) {
            if (handler == null) {    //  if found handler for directory, or someone register null handler
                throw new NotFound();
            } else {
                return new Handler(handler.getHttpHandler(), handler.getIn(), handler.getOut());
            }
        }   //  segment != null
        String segment = pathArr.remove(0);
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
