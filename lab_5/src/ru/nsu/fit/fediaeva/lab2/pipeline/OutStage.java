package ru.nsu.fit.fediaeva.lab2.pipeline;

import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.pipeline.interceptors.OutInterceptor;

class OutStage {
    private String name;
    private OutInterceptor interceptor;

    OutStage(){
        interceptor = null;
        name = "";
    }

    OutStage(OutInterceptor i){
        interceptor = i;
        name = "";
    }

    OutStage(OutInterceptor i, String n){
        interceptor = i;
        name = n;
    }

    String getName(){
        return name;
    }

    Response intercept(Response response){
        return interceptor.intercept(response); //  null if all ok
    }
}
