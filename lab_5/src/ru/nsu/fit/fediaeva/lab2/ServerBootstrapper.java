package ru.nsu.fit.fediaeva.lab2;

import com.sun.source.util.Plugin;

public interface ServerBootstrapper {
    void start();
    void restart();
    void stop();
    void registerPlugin(Plugin p);
}
