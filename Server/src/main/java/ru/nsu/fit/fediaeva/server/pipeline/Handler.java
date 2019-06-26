package ru.nsu.fit.fediaeva.server.pipeline;

import ru.nsu.fit.fediaeva.server.ServerContext;
import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.HttpHandler;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.InInterceptor;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.OutInterceptor;

import java.util.ArrayList;

public class Handler {
    private HttpHandler httpHandler;
    private IncomingPipeline inPipeline;
    private OutcomingPipeline outPipeline;

    public Handler() {
        httpHandler = null;
        inPipeline = new IncomingPipeline();
        outPipeline = new OutcomingPipeline();
    }

    public Handler(HttpHandler h, IncomingPipeline inPipe, OutcomingPipeline outPipe) {
        httpHandler = h;
        inPipeline = inPipe;
        outPipeline = outPipe;
    }

    public Handler(HttpHandler httpHandler, ServerContext serverContext) {
        this.httpHandler = httpHandler;
        inPipeline = new IncomingPipeline();
        outPipeline = new OutcomingPipeline();
    }

    public Response start(Request request) throws ProgramException {
        if (request == null) {
            throw new ProgramException();
        }
        if (httpHandler == null) {
            return HttpHandler.createErrResp(404);
        }
        Response response;
        if (inPipeline != null && outPipeline != null) {
            response = inPipeline.interceptRequest(request);
            if (response != null) {
                return outPipeline.interceptResponse(response);
            }
            response = httpHandler.getResponse(request);
            return outPipeline.interceptResponse(response);
        }

        if (inPipeline == null && outPipeline != null) {
            response = httpHandler.getResponse(request);
            return outPipeline.interceptResponse(response);
        }

        if (inPipeline != null) {
            response = inPipeline.interceptRequest(request);
            if (response != null) {
                return outPipeline.interceptResponse(response);
            }
            return httpHandler.getResponse(request);
        }
        return httpHandler.getResponse(request);
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public IncomingPipeline getIn() {
        return inPipeline;
    }

    public OutcomingPipeline getOut() {
        return outPipeline;
    }

    public ArrayList<InInterceptor> getInPipeline() {
        return inPipeline.getStages();
    }

    public void setInPipeline(IncomingPipeline inPipeline) {
        this.inPipeline = inPipeline;
    }

    public ArrayList<OutInterceptor> getOutPipeline() {
        return outPipeline.getStages();
    }

    public void setOutPipeline(OutcomingPipeline outPipeline) {
        this.outPipeline = outPipeline;
    }
}
