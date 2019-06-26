package ru.nsu.fit.fediaeva.lab2.handlers;

import ru.nsu.fit.fediaeva.lab2.exception.InternalServerError;
import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;

import java.io.UnsupportedEncodingException;

public class FileGetter implements HttpHandler {
    @Override
    public Response getResponse(RequestConstructor req) throws ProgramException {
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
