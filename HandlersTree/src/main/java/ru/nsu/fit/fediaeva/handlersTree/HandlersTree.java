package ru.nsu.fit.fediaeva.handlersTree;

import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.FileGetter;
import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.ServerPlugin;
import ru.nsu.fit.fediaeva.server.matchers.*;

import java.util.ArrayList;

public class HandlersTree implements ServerPlugin {
    @Override
    public void initialize(ServerContext serverContext) throws ProgramException {
        MatcherTree tree = new MatcherTree();
        ArrayList<SegmentMatcher> l = new ArrayList<>();
        l.add(new StringMatcher("users", "users interface"));
        l.add(new StringMatcher("files", "files for response"));
        l.add(new IntMatcher("id"));
        l.add(new AnyMatcher("file name"));
        tree.registerHandler(l, new FileGetter(), new ArrayList<>(), new ArrayList<>());
        //??
        serverContext.setMatcherTree(tree);
    }

    @Override
    public String getName() {
        return "Handlers tree";
    }
}
