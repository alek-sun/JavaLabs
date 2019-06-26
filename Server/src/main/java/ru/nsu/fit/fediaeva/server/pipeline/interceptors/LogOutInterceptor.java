package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

import java.io.UnsupportedEncodingException;

public class LogOutInterceptor implements OutInterceptor {
    private MetricsPack metricsPack;

    public LogOutInterceptor(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public Response intercept(Response response) {
        String body = "";
        try {
            body = new String(response.getBody(), "Cp1252");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Charset error");
            e.printStackTrace();
        }
        String fullBody = response.getTitle() + response.getHeadersString() + body;
        metricsPack.addRespBody(fullBody);
        return null;
    }

    @Override
    public String getName() {
        return "ResponseLogs";
    }

    @Override
    public String getPosition() {
        return "Before Time";
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