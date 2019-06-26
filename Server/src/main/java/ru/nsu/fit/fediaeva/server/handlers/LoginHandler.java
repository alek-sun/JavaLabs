package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public class LoginHandler implements HttpHandler {
    private ServerContext serverContext;

    public LoginHandler(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Response getResponse(Request req) throws ProgramException {
        Response r = HttpHandler.createResp(new byte[0], "text/html", 200);
        r.setHeader("Active", "yes");
        return r;
    }
}
