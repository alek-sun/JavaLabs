package ru.nsu.fit.fediaeva.lab2.threadpool;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.pipeline.Handler;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;

import java.io.OutputStream;

public class SocketTask implements Runnable {
    private Handler handler;
    private OutputStream outStream;
    private RequestConstructor request;

    public SocketTask(RequestConstructor req, Handler h, OutputStream stream){
        handler = h;
        outStream = stream;
        request = req;
    }

    @Override
    public void run() {
        handler.setOutStream(outStream);
        handler.setRequest(request);
        try {
            handler.start();
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }
}
