package ru.nsu.fit.fediaeva.server.pipeline.interceptors;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.constructors.Response;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

/**
 * Attention!
 * If Request intercepted, return null.
 * Else create error response by static methods of HttpHandler and return it.
 * <p>
 * ! Position format:
 * First
 * Last
 * After {stageName} : 1 word!
 * Before {stageName} : 1 word!
 */
public interface InInterceptor {
    String getPosition();

    String getName();

    Response intercept(Request req) throws ProgramException;
}
