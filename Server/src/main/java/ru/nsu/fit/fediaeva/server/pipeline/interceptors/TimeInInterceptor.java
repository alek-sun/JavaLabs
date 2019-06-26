package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

public class TimeInInterceptor implements InInterceptor {
    private MetricsPack metricsPack;

    public TimeInInterceptor(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public String getPosition() {
        return "First";
    }

    @Override
    public String getName() {
        return "Time";
    }

    @Override
    public Response intercept(Request req) throws ProgramException {
        metricsPack.setTimeBuf(System.currentTimeMillis());
        return null;
    }
}
