package ru.nsu.fit.fediaeva.server;

import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public interface ServerPlugin {
    void initialize(ServerContext context) throws ProgramException;

    String getName();
}
