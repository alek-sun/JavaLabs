package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import java.io.UnsupportedEncodingException;

public interface HttpHandler {
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
        resp.setTitle("HTTP/1.1 " + resp.getDescription() + "\r\n");
        resp.setHeader("Content-Length", len.toString());
        resp.setHeader("Content-Type", mime);
        resp.setHeader("Connection", "close");
        resp.setBody(data);
        return resp;
    }

    /**
     * Get response by request
     *
     * @param req request
     * @return response
     */
    Response getResponse(Request req) throws ProgramException;

}
