package ru.nsu.fit.fediaeva.server.constructors;

import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Constructs request from input stream and contains request data
 */
public class Request {
    private Map<String, String> headers;
    private byte[] body;
    private String title;   //  first line
    private Map<String, String> pathInfo;

    public Request(InputStream in) throws ProgramException {
        headers = new HashMap<>();
        BufferedInputStream bufIn = new BufferedInputStream(in);
        getFirstLine(bufIn);
        getHeaders(bufIn);
        getBody(bufIn);
        pathInfo = new HashMap<>();
    }

    /**
     * Get and save request headers
     */
    private void getHeaders(BufferedInputStream in) throws ProgramException {
        while (true) {
            String curStr = readBufferedLine(in);
            if (curStr.equals("")) {
                break;
            }
            String[] keyVal = curStr.split(": ");
            if (keyVal.length != 2) {
                continue;
            }
            putHeader(keyVal[0], keyVal[1]);
        }
    }

    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    public void putHeader(String key, String val) {
        headers.put(key, val);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    /**
     * Read 1 line from BufferedInputStream
     *
     * @return read line
     */
    private String readBufferedLine(BufferedInputStream in) throws ProgramException {
        ArrayList<Byte> l = new ArrayList<>();
        int c = 0;
        while (true) {
            try {
                c = in.read();
            } catch (IOException e) {
                throw new ProgramException();
            }
            if (c < 0) {
                return "";
            }
            byte b = (byte) c;
            try {
                l.add(b);
            } catch (OutOfMemoryError e) {
                System.out.println(l.size());
            }
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
     * Get method, path and version from 1st line
     */
    private void getFirstLine(BufferedInputStream in) throws ProgramException {
        title = readBufferedLine(in);
        String[] titleList = title.split(" ", -1);
        if (titleList.length < 3) {
            throw new ProgramException();
        }
        String path = titleList[1];
        path = path.replaceFirst("/", "");
        putHeader("Path", path);
        putHeader("Method", titleList[0]);
        putHeader("Version", titleList[2]);
    }

    /**
     * Get request body
     *
     * @return body in byte array
     * @throws ProgramException reading error
     */
    private byte[] getBody(BufferedInputStream in) throws ProgramException {
        if (body != null) {
            return body;
        }
        int len = 0;
        if (headers.containsKey("Content-Length")) {
            len = Integer.parseInt(getHeader("Content-Length"));
        }
        byte[] b = new byte[len];
        try {
            in.read(b, 0, len);
        } catch (IOException e) {
            throw new ProgramException();
        }
        body = b;
        return b;
    }

    public ArrayList<String> getPathArray() {
        String path = getHeader("Path");
        return new ArrayList<>(Arrays.asList(path.split("[\\\\|/]")));
    }

    public String getBodyString() {
        String str = "";
        try {
            str = new String(body, "Cp1252");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Charset error!");
            e.printStackTrace();
        }
        return str;
    }

    public byte[] getBody() {
        return body;
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

    public String getTitle() {
        return title + "\r\n";
    }
}
