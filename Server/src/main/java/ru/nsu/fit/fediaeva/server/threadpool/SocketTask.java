package ru.nsu.fit.fediaeva.server.threadpool;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.Handler;

import java.io.OutputStream;

public class SocketTask implements Runnable {
    private Handler handler;
    private OutputStream outStream;
    private Request request;

    public SocketTask(Request req, Handler h, OutputStream stream) {
        handler = h;
        outStream = stream;
        request = req;
    }

    @Override
    public void run() {
        try {
            Response resp = handler.start(request);
            resp.send(outStream);
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }
}
