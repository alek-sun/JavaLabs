package ru.nsu.fit.fediaeva.server.pipeline;

import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.pipeline.interceptors.OutInterceptor;

import java.util.ArrayList;
import java.util.List;

public class OutcomingPipeline {
    private ArrayList<OutInterceptor> stages;

    public OutcomingPipeline() {
        stages = new ArrayList<>();
    }

    public OutcomingPipeline(List<OutInterceptor> outPipe) {
        stages = (ArrayList<OutInterceptor>) outPipe;
    }

    synchronized public void addFirst(OutInterceptor interceptor, String stageName) {
        stages.add(0, interceptor);
    }

    synchronized public void addLast(OutInterceptor interceptor, String stageName) {
        stages.add(interceptor);
    }

    synchronized public void addAfter(String afterStageName, OutInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages
                    .get(i)
                    .getName()
                    .equals(afterStageName)) {
                stages.add(i + 1, interceptor);
            }
        }
    }

    synchronized public void addBefore(String beforeStageName, OutInterceptor interceptor, String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (beforeStageName.equals(stages
                    .get(i)
                    .getName())) {
                if (i == 0) {
                    i++;
                }
                stages.add(i - 1, interceptor);
            }
        }
    }

    synchronized public void add(OutInterceptor outInterceptor) throws ProgramException {
        String[] posArr = outInterceptor.getPosition().split(" ");
        if (posArr.length == 1) {
            if (posArr[0].equals("First")) {
                addFirst(outInterceptor, outInterceptor.getName());
            }

            if (posArr[0].equals("Last")) {
                addLast(outInterceptor, outInterceptor.getName());
            }
        } else if (posArr.length == 2) {
            if (posArr[0].equals("Before")) {
                addBefore(posArr[1], outInterceptor, outInterceptor.getName());
            }
            if (posArr[0].equals("After")) {
                addAfter(posArr[1], outInterceptor, outInterceptor.getName());
            }
        } else {
            throw new ProgramException();
        }
    }

    synchronized Response interceptResponse(Response resp) throws ProgramException {
        Response response;
        if (stages == null) {
            return resp;
        }
        for (OutInterceptor stage : stages) {
            if ((response = stage.intercept(resp)) != null) {
                return response;
            }
        }
        return resp;
    }

    public ArrayList<OutInterceptor> getStages() {
        return stages;
    }
}

