package ru.nsu.fit.fediaeva.lab2.constructors;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains response data
 */
public class Response {
    private Integer code;
    private Map<String, String> headers;
    private static Map<Integer, String> respCodes = new HashMap<>() {{
        put(200, "OK");
        put(404, "Not found");
        put(405, "Method not allowed");
        put(500, "Internal server error");
    }};
    private byte[] body;

    public Response() {
        body = new byte[0];
        headers = new HashMap<>();
    }

    public void setHeader(String key, String val) {
        headers.put(key, val);
    }

    public void setBody(byte[] b) {
        body = b;
    }

    public void setCode(Integer errCode) {
        code = errCode;
    }

    public static String getDescription(Integer code) {
        return respCodes.get(code);
    }

    public String getDescription() {
        return code.toString() + " " + respCodes.get(code);
    }

    public Integer getCode() {
        return code;
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

    public byte[] getBody() {
        return body;
    }
}
