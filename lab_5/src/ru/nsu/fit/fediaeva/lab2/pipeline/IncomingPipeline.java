package ru.nsu.fit.fediaeva.lab2.pipeline;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.pipeline.interceptors.InInterceptor;

import java.util.ArrayList;

public class IncomingPipeline extends Pipeline {
    private ArrayList<InStage> stages;

    synchronized public void addFirst(InInterceptor interceptor, String stageName) {
        stages.add(0, new InStage(interceptor, stageName));
    }

    synchronized public void addLast(InInterceptor interceptor, String stageName) {
        stages.add(new InStage(interceptor, stageName));
    }

    synchronized public void addAfter(String afterStageName, InInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(afterStageName)) {
                stages.add(i + 1, new InStage(interceptor, stageName));
            }
        }
    }

    synchronized public void addBefore(String beforeStageName, InInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(beforeStageName)) {
                if (i == 0) {
                    i++;
                }
                stages.add(i - 1, new InStage(interceptor, stageName));
            }
        }
    }

    synchronized Response interceptRequest(RequestConstructor req) {
        Response response;
        for (InStage stage : stages) {
            if ((response = stage.intercept(req)) != null) {
                return response;
            }
        }
        return null;
    }
}

