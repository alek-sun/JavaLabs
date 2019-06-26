package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

import java.util.ArrayList;

public class TimeGetter implements HttpHandler {
    private MetricsPack metricsPack;

    public TimeGetter(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public Response getResponse(Request req) throws ProgramException {
        Response resp = new Response();
        resp.setTitle("HTTP/1.1 200 OK\r\n");
        ArrayList<Long> times = metricsPack.getTimes();
        StringBuilder res = new StringBuilder();
        Integer i = 0;
        for (Long time : times) {
            res.append("Time ")
                    .append(i)
                    .append(" : ")
                    .append(time.toString())
                    .append("\r\n");
            i++;
        }
        byte[] bytes = res.toString().getBytes();
        resp.setBody(bytes);
        resp.setHeader("Content-Length", ((Integer) bytes.length).toString());
        return resp;
    }
}
