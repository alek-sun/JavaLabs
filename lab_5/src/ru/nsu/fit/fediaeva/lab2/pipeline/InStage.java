package ru.nsu.fit.fediaeva.lab2.pipeline;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.pipeline.interceptors.InInterceptor;

class InStage{
    private String name;
    private InInterceptor interceptor;

    InStage(){
        interceptor = null;
        name = "";
    }

    InStage(InInterceptor i){
        interceptor = i;
        name = "";
    }

    InStage(InInterceptor i, String n){
        interceptor = i;
        name = n;
    }

    String getName(){
        return name;
    }

    Response intercept(RequestConstructor request){
        return interceptor.intercept(request);  //  null if all ok
    }
}
