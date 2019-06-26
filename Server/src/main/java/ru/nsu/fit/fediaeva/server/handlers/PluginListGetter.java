package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public class PluginListGetter implements HttpHandler {
    ServerContext context;

    public PluginListGetter(ServerContext context) {
        this.context = context;
    }

    @Override
    public Response getResponse(Request req) throws ProgramException {
        Response resp = new Response();
        resp.setTitle("HTTP/1.1 200 OK\r\n");
        StringBuilder body = new StringBuilder();
        for (String name : context.getPluginsNames()) {
            body.append(name).append("\r\n");
        }
        resp.setBody(body.toString().getBytes());
        resp.setHeader("Content-Length", ((Integer) body.length()).toString());
        return resp;
    }
}
