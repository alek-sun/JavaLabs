package ru.nsu.fit.fediaeva.lab2.pipeline.interceptors;

import ru.nsu.fit.fediaeva.lab2.constructors.Response;

/**
 * Attention!
 * If Response intercepted, return null.
 * Else create error response by static methods of HttpHandler and return it.
 */
public interface OutInterceptor{
    Response intercept(Response response);
}
