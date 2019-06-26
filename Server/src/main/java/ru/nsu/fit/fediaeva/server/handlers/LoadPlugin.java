package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import java.nio.file.Paths;

public class LoadPlugin implements HttpHandler {
    ServerContext serverContext;

    public LoadPlugin(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Response getResponse(Request req) throws ProgramException {
        String plPath = req.getHeader("JarPath");
        if (plPath == null) {
            return HttpHandler.createErrResp(500);
        }
        serverContext.registerPlugin(Paths.get(plPath));
        String result = "Plugins will be loaded";
        return HttpHandler.createResp(result.getBytes(), "text/html", 200);
    }
}
