package ru.nsu.fit.fediaeva.client.clientRequestConstructors;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class RequestSender {
    private Socket socket;
    private String hostName;
    private int port;

    public RequestSender(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    Request sendRequest(Response clientRequest) throws ProgramException, NotFound, IOException {
        InetAddress adr;
        try {
            adr = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            throw new NotFound();
        }

        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(adr, port), 1000);
        } catch (SocketTimeoutException ignored) {
        }
        if (!socket.isConnected()) {
            throw new NotFound();
        }

        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            throw new ProgramException();
        }

        OutputStream os;
        BufferedInputStream is;
        try {
            os = socket.getOutputStream();
            is = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ProgramException();
        }
        clientRequest.send(os);
        return new Request(is);
    }

    public boolean authoriseConnect(String login, String password) throws NotFound, ProgramException, IOException {
        Response request = new Response();
        request.setTitle("GET /login HTTP/1.1\r\n");
        request.setHeader("Login", login);
        request.setHeader("Password", password);
        request.setHeader("Hostname", hostName);
        Request response = sendRequest(request);
        return response.getHeader("Active").equals("yes");
    }

    public String sendTimeRequest() throws NotFound, ProgramException, IOException {
        Response request = new Response();
        request.setTitle("GET /admin/time HTTP/1.1\r\n");
        Request response = sendRequest(request);
        return response.getBodyString();
    }

    public String sendLogsRequest(String requestResponse) throws NotFound, ProgramException, IOException {
        Response request = new Response();
        request.setTitle("GET /admin/logs/" + requestResponse + " HTTP/1.1\r\n");
        Request response = sendRequest(request);
        String body = "";
        try {
            body = new String(response.getBody(), "Cp1252");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Charset error");
            e.printStackTrace();
        }
        return body;
    }

    public String sendReloadRequest() throws IOException, ProgramException, NotFound {
        Response request = new Response();
        request.setTitle("GET /user/reload HTTP/1.1\r\n");
        Request response = sendRequest(request);
        return new String(response.getBody(), "Cp1252");
    }

    public String sendPluginListRequest() throws IOException, ProgramException, NotFound {
        Response request = new Response();
        request.setTitle("GET /user/plugin HTTP/1.1\r\n");
        Request response = sendRequest(request);
        return new String(response.getBody(), "Cp1252");
    }

    public String sendLoadPluginRequest(String jarPath) throws IOException, ProgramException, NotFound {
        Response request = new Response();
        request.setTitle("GET /user/loadplugin HTTP/1.1\n");
        request.setHeader("JarPath", jarPath);
        Request response = sendRequest(request);
        return new String(response.getBody(), "Cp1252");
    }
}
