package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.HttpHandler;

import java.util.HashMap;
import java.util.Map;

public class AuthoriseInterceptor implements InInterceptor {
    private ServerContext serverContext;
    private Map<String, String> loginMap = new HashMap<>() {{
        put("admin", "12345");
        put("user1", "password");
    }};

    public AuthoriseInterceptor(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public String getPosition() {
        return "Add first";
    }

    @Override
    public String getName() {
        return "Authorisation";
    }

    @Override
    public Response intercept(Request req) throws ProgramException {
        String login = req.getHeader("Login");
        String password = req.getHeader("Password");
        for (Map.Entry<String, String> e : loginMap.entrySet()) {
            if (e.getKey().equals(login) && e.getValue().equals(password)) {
                return null;
            }
        }
        Response r = HttpHandler.createErrResp(404);
        r.setHeader("Active", "no");
        return r;
    }
}
