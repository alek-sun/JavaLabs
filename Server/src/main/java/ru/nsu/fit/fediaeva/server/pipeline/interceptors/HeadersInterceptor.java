package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.MethodNotAllowed;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.HttpHandler;

public class HeadersInterceptor implements InInterceptor {
    @Override
    public String getPosition() {
        return "After Time";
    }

    @Override
    public String getName() {
        return "HeadersChecking";
    }

    @Override
    public Response intercept(Request req) throws ProgramException {
        try {
            checkRequest(req);
            return null;
        } catch (MethodNotAllowed methodNotAllowed) {
            return HttpHandler.createErrResp(405);
        } catch (NotFound notFound) {
            return HttpHandler.createErrResp(404);
        }
    }

    /**
     * Check correctness of request
     *
     * @param req checked request
     * @throws MethodNotAllowed incorrect method
     * @throws NotFound         incorrect path
     */
    private void checkRequest(Request req) throws MethodNotAllowed, NotFound {
        if (!req.getHeader("Method").equals("GET") ||
                !req.getHeader("Version").equals("HTTP/1.1")) {
            throw new MethodNotAllowed();
        }
        if (req.getHeader("Path").endsWith("/")) {
            throw new NotFound();
        }
    }

}
