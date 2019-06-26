package ru.nsu.fit.fediaeva.server.constructors;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains response data
 */
public class Response {
    private static Map<Integer, String> respCodes = new HashMap<>() {{
        put(200, "OK");
        put(404, "Not found");
        put(405, "Method not allowed");
        put(500, "Internal server error");
    }};
    private Integer code;
    private String title;
    private Map<String, String> headers;
    private byte[] body;

    public Response() {
        body = new byte[0];
        headers = new HashMap<>();
    }

    public static String getDescription(Integer code) {
        return respCodes.get(code);
    }

    public void send(OutputStream os) {
        if (title == null) {
            title = "HTTP/1.1 200 OK\r\n";
        }
        if (body == null) {
            body = new byte[0];
        }
        if (!headers.containsKey("Content-Length")) {
            headers.put("Content-Length", String.valueOf(body.length));
        }
        try {
            os.write(title.getBytes());
            os.write(getHeadersString().getBytes());
            os.write(body);
            os.flush();
        } catch (IOException ignored) {
        }
    }

    public void setHeader(String key, String val) {
        headers.put(key, val);
    }

    public void setCode(Integer errCode) {
        code = errCode;
    }

    public String getDescription() {
        return code.toString() + " " + respCodes.get(code);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] b) {
        body = b;
    }

    public String getHeadersString() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> e : headers.entrySet()) {
            String cur = e.getKey() + ": " + e.getValue() + "\r\n";
            res.append(cur);
        }
        res.append("\r\n"); // end of headers
        return res.toString();
    }
}
