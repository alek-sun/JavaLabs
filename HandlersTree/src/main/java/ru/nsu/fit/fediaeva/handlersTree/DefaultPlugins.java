package ru.nsu.fit.fediaeva.handlersTree;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.ServerPlugin;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.*;
import ru.nsu.fit.fediaeva.server.matchers.MatcherTree;
import ru.nsu.fit.fediaeva.server.matchers.StringMatcher;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.*;

import java.util.ArrayList;
import java.util.Arrays;

public class DefaultPlugins implements ServerPlugin {
    @Override
    public void initialize(ServerContext serverContext) throws ProgramException {
        //root incoming interceptors
        ArrayList<InInterceptor> defInInterceptors = new ArrayList<>();
        defInInterceptors.add(new HeadersInterceptor());
        defInInterceptors.add(new LogInInterceptor(serverContext.getPack()));
        defInInterceptors.add(new TimeInInterceptor(serverContext.getPack()));

        //root outcoming interceptors
        ArrayList<OutInterceptor> defOutInterceptors = new ArrayList<>();
        defOutInterceptors.add(new LogOutInterceptor(serverContext.getPack()));
        defOutInterceptors.add(new TimeOutInterceptor(serverContext.getPack()));

        //register root interceptors
        MatcherTree tree = serverContext.getMatcherTree();
        tree.registerInInterceptors(new ArrayList<>(), defInInterceptors);
        tree.registerOutInterceptors(new ArrayList<>(), defOutInterceptors);

        //register authorisation interceptors:
        tree.registerHandler(Arrays.asList(new StringMatcher("login",  "authorisation")),
                new LoginHandler(serverContext), new ArrayList<>(), new ArrayList<>());
        tree.registerInInterceptors(Arrays.asList(new StringMatcher("login",  "authorisation")),
                Arrays.asList(new AuthoriseInterceptor(serverContext)));

        //register handlers for admin requests
        tree.registerHandler(Arrays.asList(
                new StringMatcher("admin", "admin interface"),
                new StringMatcher("logs", null),
                new StringMatcher("request", "request log")),
                new RequestLogsGetter(serverContext.getPack()),
                new ArrayList<>(), new ArrayList<>());
        tree.registerHandler(Arrays.asList(
                new StringMatcher("admin", "admin interface"),
                new StringMatcher("logs", null),
                new StringMatcher("response", "response log")),
                new ResponseLogsGetter(serverContext.getPack()),
                new ArrayList<>(), new ArrayList<>());
        tree.registerHandler(Arrays.asList(
                new StringMatcher("admin", "admin interface"),
                new StringMatcher("time", "time")),
                new TimeGetter(serverContext.getPack()),
                new ArrayList<>(), new ArrayList<>());

        //handlers for user
        tree.registerHandler(Arrays.asList(
                new StringMatcher("user", "user interface"),
                new StringMatcher("loadplugin", "load plugins by path")),
                new LoadPlugin(serverContext),
                new ArrayList<>(), new ArrayList<>());
        tree.registerHandler(Arrays.asList(
                new StringMatcher("user", "user interface"),
                new StringMatcher("plugin", "show plugin list")),
                new PluginListGetter(serverContext),
                new ArrayList<>(), new ArrayList<>());
        tree.registerHandler(Arrays.asList(
                new StringMatcher("user", "user interface"),
                new StringMatcher("reload", "reload server")),
                new Reloader(serverContext),
                new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String getName() {
        return "Default Plugin";
    }
}
