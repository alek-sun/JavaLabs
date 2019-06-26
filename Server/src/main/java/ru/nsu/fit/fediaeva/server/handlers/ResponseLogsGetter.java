package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

import java.util.ArrayList;

public class ResponseLogsGetter implements HttpHandler {
    private MetricsPack metricsPack;

    public ResponseLogsGetter(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public Response getResponse(Request req) {
        ArrayList<String> b = metricsPack.getResponseBodies();
        StringBuilder result = new StringBuilder();
        for (String body : b) {
            result.append(body).append("\r\n");
        }
        return HttpHandler.createResp(result.toString().getBytes(), "text/html", 200);
    }
}
