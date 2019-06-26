package ru.nsu.fit.fediaeva.lab2.handlers;

import ru.nsu.fit.fediaeva.lab2.exception.MethodNotAllowed;
import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;

import java.io.UnsupportedEncodingException;

public interface HttpHandler {
    /**
     * Get response by request
     *
     * @param req request
     * @return response
     */
    Response getResponse(RequestConstructor req) throws ProgramException;

    /**
     * Creates error response
     *
     * @param code error code
     * @return Error response
     * @throws ProgramException If unsupported encoding
     */
    static Response createErrResp(Integer code) throws ProgramException {
        try {
            byte[] errBody = ("<b><center style=\"font-size:40px\">"
                    + code + " " +
                    Response.getDescription(code) +
                    "</center></b><br><hr>")
                    .getBytes("Cp1252");
            return createResp(errBody, "text/html", code);
        } catch (UnsupportedEncodingException e) {
            throw new ProgramException();
        }
    }

    /**
     * Create all responses
     *
     * @param data Response body
     * @param mime Data type
     * @param code Response code
     * @return Response
     */
    static Response createResp(byte[] data, String mime, Integer code) {
        Response resp = new Response();
        resp.setCode(code);
        Integer len = data.length;
        resp.setHeader("Content-length", len.toString());
        resp.setHeader("Content-type", mime);
        resp.setHeader("Connection", "close");
        resp.setBody(data);
        return resp;
    }

}
