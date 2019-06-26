package ru.nsu.fit.fediaeva.server.pipeline;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.InInterceptor;

import java.util.ArrayList;
import java.util.List;

public class IncomingPipeline {
    private ArrayList<InInterceptor> stages;

    public IncomingPipeline() {
        stages = new ArrayList<>();
    }

    public IncomingPipeline(List<InInterceptor> inPipe) {
        stages = (ArrayList<InInterceptor>) inPipe;
    }

    synchronized public void addFirst(InInterceptor interceptor) {
        stages.add(0, interceptor);
    }

    synchronized public void addLast(InInterceptor interceptor) {
        stages.add(interceptor);
    }

    synchronized public void addAfter(String afterStageName, InInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(afterStageName)) {
                stages.add(i + 1, interceptor);
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
                stages.add(i - 1, interceptor);
            }
        }
    }

    synchronized public void add(InInterceptor inInterceptor) throws ProgramException {
        String[] posArr = inInterceptor.getPosition().split(" ");
        if (posArr.length == 2) {
            if (posArr[0].equals("Before")) {
                addBefore(posArr[1], inInterceptor, inInterceptor.getName());
            }
            if (posArr[0].equals("After")) {
                addAfter(posArr[1], inInterceptor, inInterceptor.getName());
            }
        } else if (posArr.length == 1) {
            if (posArr[0].equals("First")) {
                addFirst(inInterceptor);
            }

            if (posArr[0].equals("Last")) {
                addLast(inInterceptor);
            }
        } else {
            System.out.println("Bad stage name!");
            throw new ProgramException();
        }
    }

    synchronized Response interceptRequest(Request req) throws ProgramException {
        Response response;
        if (stages == null) {
            return null;
        }
        for (InInterceptor stage : stages) {
            if ((response = stage.intercept(req)) != null) {
                return response;
            }
        }
        return null;
    }

    ArrayList<InInterceptor> getStages() {
        return stages;
    }
}

