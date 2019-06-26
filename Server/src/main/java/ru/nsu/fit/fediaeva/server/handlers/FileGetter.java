package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.InternalServerError;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public class FileGetter implements HttpHandler {
    @Override
    public Response getResponse(Request req) throws ProgramException {
        String path = req.getHeader("Path");
        FileServer fileServer = new FileServer(path);
        byte[] data;
        try {
            data = fileServer.getData();
        } catch (NotFound nf) {
            return HttpHandler.createErrResp(404);
        } catch (InternalServerError ie) {
            return HttpHandler.createErrResp(500);
        }
        String mime = fileServer.getMimeType();
        return HttpHandler.createResp(data, mime, 200);
    }
}
