package ru.nsu.fit.fediaeva.lab2.pipeline.interceptors;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.exception.InternalServerError;
import ru.nsu.fit.fediaeva.lab2.exception.MethodNotAllowed;
import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.handlers.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;

public class HeadersInterceptor implements InInterceptor {
    @Override
    public Response intercept(RequestConstructor req) throws ProgramException {
        try{
            getParams(req);
            getHeaders(req);
            checkRequest(req);
            return null;
        } catch (InternalServerError internalServerError) {
            return HttpHandler.createErrResp(500);
        } catch (MethodNotAllowed methodNotAllowed) {
            return HttpHandler.createErrResp(405);
        } catch (NotFound notFound) {
            return HttpHandler.createErrResp(404);
        }
    }

    /**
     * Get method, path and version from 1st line
     *
     * @throws InternalServerError if first request line is incorrect or reading line error
     */
    private void getParams(RequestConstructor req) throws InternalServerError, ProgramException {
        String curStr;
        curStr = readBufferedLine(req);
        String[] titleList = curStr.split(" ");
        if (titleList.length != 3) {
            throw new InternalServerError();
        }
        String path = titleList[1];
        path = path.replaceFirst("/", "");
        req.putHeader("Path", path);
        req.putHeader("Method", titleList[0]);
        req.putHeader("Version", titleList[2]);
    }

    /**
     * Read 1 line from BufferedInputStream
     *
     * @return read line
     * @throws InternalServerError reading error
     */
    private String readBufferedLine(RequestConstructor req) throws InternalServerError, ProgramException {
        ArrayList<Byte> l = new ArrayList<>();
        int c;
        while (true) {
            try {
                c = req.getInStream().read();
            } catch (IOException e) {
                throw new InternalServerError();
            }
            if (c < 0) {
                throw new ProgramException();
            }
            byte b = (byte) c;
            l.add(b);
            if (b == '\n') {
                StringBuilder res = new StringBuilder();
                l.remove(l.size() - 1);
                if (l.get(l.size() - 1) == '\r') {
                    l.remove(l.size() - 1);
                }
                for (Byte j : l) {
                    res.append((char) (byte) j);
                }
                return res.toString();
            }
        }
    }

    /**
     * Get request body
     *
     * @return body in byte array
     * @throws InternalServerError reading error
     */
    public byte[] getBody(RequestConstructor req) throws InternalServerError {
        byte[] body = req.getBody();
        if (body != null) {
            return body;
        }
        int len = 0;
        if (req.getHeaders().containsKey("Content-length")) {
            len = Integer.parseInt(req.getHeader("Content-length"));
        }
        byte[] b = new byte[len];
        try {
            req.getInStream().read(b, 0, len);
        } catch (IOException e) {
            throw new InternalServerError();
        }
        req.setBody(b);
        return b;
    }

    /**
     * Get and save request headers
     *
     * @throws InternalServerError reading line error
     */
    private void getHeaders(RequestConstructor req) throws InternalServerError, ProgramException {
        while (true) {
            String curStr = readBufferedLine(req);
            if (curStr.equals("")) {
                break;
            }
            String[] keyVal = curStr.split(": ");
            req.putHeader(keyVal[0], keyVal[1]);
        }
    }

    /**
     * Check correctness of request
     *
     * @param req checked request
     * @throws MethodNotAllowed incorrect method
     * @throws NotFound         incorrect path
     */
    private void checkRequest(RequestConstructor req) throws MethodNotAllowed, NotFound {
        if (!req.getHeader("Method").equals("GET") ||
                !req.getHeader("Version").equals("HTTP/1.1")) {
            throw new MethodNotAllowed();
        }
        if (req.getHeader("Path").endsWith("/")) {
            throw new NotFound();
        }
    }

}
