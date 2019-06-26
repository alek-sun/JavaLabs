package ru.nsu.fit.fediaeva.lab2.pipeline.interceptors;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;

/**
 * Attention!
 * If Request intercepted, return null.
 * Else create error response by static methods of HttpHandler and return it.
 */
public interface InInterceptor{
    Response intercept(RequestConstructor req) throws ProgramException;
}
