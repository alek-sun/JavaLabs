package ru.nsu.fit.fediaeva.lab2.constructors;

import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.pipeline.Handler;
import ru.nsu.fit.fediaeva.lab2.matchers.MatcherTree;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Constructs request from input stream and contains request data
 */
public class RequestConstructor {
    private Map<String, String> headers;
    private byte[] body;
    private BufferedInputStream in;
    private Map<String, String> pathInfo;
    private MatcherTree tree;

    public RequestConstructor(){
        headers = new LinkedHashMap<>();
        pathInfo = new HashMap<>();
    }

    public RequestConstructor(InputStream sin, MatcherTree handlersTree) {
        in = new BufferedInputStream(sin);
        headers = new LinkedHashMap<>();
        tree = handlersTree;
        pathInfo = new HashMap<>();
    }


    public byte[] getBody() {
        return body;
    }

    public BufferedInputStream getInStream() {
        return in;
    }

    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    public void setTree(MatcherTree matcherTree){
        tree = matcherTree;
    }

    public Handler findHandler() throws NotFound {
        String path = getHeader("Path");
        ArrayList<String> pathArr = new ArrayList<>(Arrays.asList(path.split("[\\\\|/]")));
        return tree.getHandler(pathArr, pathInfo);
    }

   public void putHeader(String key, String val){
        headers.put(key, val);
   }

   public void setBody(byte[] body){
        this.body = body;
   }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

}
