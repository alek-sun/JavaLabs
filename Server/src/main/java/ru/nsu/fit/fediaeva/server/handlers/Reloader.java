package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public class Reloader implements HttpHandler {
    ServerContext serverContext;

    public Reloader(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Response getResponse(Request req) throws ProgramException {
        serverContext.restartServer();
        Response resp = new Response();
        resp.setTitle("HTTP/1.1 200 OK\r\n");
        String body = "Server reloaded\r\n";
        resp.setBody(body.getBytes());
        resp.setHeader("Content-Length", ((Integer) body.length()).toString());
        return resp;
    }
}
