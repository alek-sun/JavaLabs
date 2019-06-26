package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

import java.io.UnsupportedEncodingException;

public class LogInInterceptor implements InInterceptor {
    private MetricsPack metricsPack;

    public LogInInterceptor(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public String getPosition() {
        return "Last";
    }

    @Override
    public String getName() {
        return "RequestLogs";
    }

    @Override
    public Response intercept(Request req) {
        String body = "";
        try {
            body = new String(req.getBody(), "Cp1252");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Charset error");
            e.printStackTrace();
        }
        String fullBody = req.getTitle() + req.getHeadersString() + body;
        metricsPack.addReqBody(fullBody);
        return null;
    }
}
/*byte[] body = response.getBody();
        int bodyLen = body.length;
        if (bodyLen > 100){
            bodyLen = 100;
        }
        int fullLength = bodyLen + headers.length;
        byte[] b = new byte[fullLength];
        System.arraycopy(headers, 0, b, 0, headers.length);
        System.arraycopy(body, 0, b, headers.length, bodyLen);*/