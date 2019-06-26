package ru.nsu.fit.fediaeva.lab2.pipeline;

import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.pipeline.interceptors.OutInterceptor;

import java.util.ArrayList;

public class OutcomingPipeline {
    private ArrayList<OutStage> stages;

    synchronized public void addFirst(OutInterceptor interceptor, String stageName) {
        stages.add(0, new OutStage(interceptor, stageName));
    }

    synchronized public void addLast(OutInterceptor interceptor, String stageName) {
        stages.add(new OutStage(interceptor, stageName));
    }

    synchronized public void addAfter(String afterStageName, OutInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(afterStageName)) {
                stages.add(i + 1, new OutStage(interceptor, stageName));
            }
        }
    }

    synchronized public void addBefore(String beforeStageName, OutInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(beforeStageName)) {
                if (i == 0) {
                    i++;
                }
                stages.add(i - 1, new OutStage(interceptor, stageName));
            }
        }
    }

    synchronized Response interceptResponse(Response response) {
        for (OutStage stage : stages) {
            if ((response = stage.intercept(response)) != null) {
                return response;
            }
        }
        return response;
    }
}
