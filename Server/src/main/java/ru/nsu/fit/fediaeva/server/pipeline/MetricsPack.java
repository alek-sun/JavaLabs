package ru.nsu.fit.fediaeva.server.pipeline;

import java.util.ArrayList;

public class MetricsPack {
    private ArrayList<Long> times;
    private ArrayList<String> requestBodies;
    private ArrayList<String> responseBodies;
    private long timeBuf;

    public MetricsPack() {
        times = new ArrayList<>();
        requestBodies = new ArrayList<>();
        responseBodies = new ArrayList<>();
        timeBuf = 0;
    }

    public void addReqBody(String body) {
        requestBodies.add(body);
    }

    public void addRespBody(String body) {
        responseBodies.add(body);
    }

    public void addTime(Long time) {
        times.add(time);
    }

    public long getTimeBuf() {
        return timeBuf;
    }

    public void setTimeBuf(long timeBuf) {
        this.timeBuf = timeBuf;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public ArrayList<String> getRequestBodies() {
        return requestBodies;
    }

    public ArrayList<String> getResponseBodies() {
        return responseBodies;
    }

    //getters

}
