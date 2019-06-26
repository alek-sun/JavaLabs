package ru.nsu.fit.fediaeva.lab2.pipeline;

import ru.nsu.fit.fediaeva.lab2.ResponseSender;
import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.handlers.HttpHandler;

import java.io.OutputStream;

public class Handler {
    private HttpHandler httpHandler;
    private IncomingPipeline inPipeline;
    private OutcomingPipeline outPipeline;
    private OutputStream outStream;
    private RequestConstructor request;

    public Handler(HttpHandler h, IncomingPipeline inPipe, OutcomingPipeline outPipe){
        httpHandler = h;
        inPipeline = inPipe;
        outPipeline = outPipe;
        outStream = null;
        request = null;
    }

    public void start() throws ProgramException {
        if (outStream == null || request == null){
            throw new ProgramException();
        }
        ResponseSender sender;
        Response response;
        sender = new ResponseSender(outStream);
        response = inPipeline.interceptRequest(request);
        if (response != null) {
            sender.sendResponse(response);
            return;
        }
        response = httpHandler.getResponse(request);
        response = outPipeline.interceptResponse(response);
        sender.sendResponse(response);
    }


    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    public void setRequest(RequestConstructor request) {
        this.request = request;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void setInPipeline(IncomingPipeline inPipeline) {
        this.inPipeline = inPipeline;
    }

    public void setOutPipeline(OutcomingPipeline outPipeline) {
        this.outPipeline = outPipeline;
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public IncomingPipeline getInPipeline(){
        return inPipeline;
    }

    public OutcomingPipeline getOutPipeline(){
        return outPipeline;
    }
}
