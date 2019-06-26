package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

public class TimeOutInterceptor implements OutInterceptor {
    private MetricsPack metricsPack;

    public TimeOutInterceptor(MetricsPack metricsPack) {
        this.metricsPack = metricsPack;
    }

    @Override
    public Response intercept(Response response) throws ProgramException {
        metricsPack.addTime(System.currentTimeMillis() - metricsPack.getTimeBuf());
        return null;
    }

    @Override
    public String getName() {
        return "Time";
    }

    @Override
    public String getPosition() {
        return "Last";
    }
}
