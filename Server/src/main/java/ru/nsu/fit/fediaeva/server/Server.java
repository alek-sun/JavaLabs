package ru.nsu.fit.fediaeva.server;

import ru.nsu.fit.fediaeva.server.constructors.Request;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.handlers.HttpHandler;
import ru.nsu.fit.fediaeva.server.matchers.MatcherTree;
import ru.nsu.fit.fediaeva.server.pipeline.Handler;
import ru.nsu.fit.fediaeva.server.threadpool.SocketTask;
import ru.nsu.fit.fediaeva.server.threadpool.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private int port;
    private boolean isStopped;
    private ThreadPool threadPool;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void stop() {
        isStopped = true;
        threadPool.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Can't close socket");
            e.printStackTrace();
        }
    }

    /**
     * Start of server, waiting for socket and their processing by response processor (proc)
     *
     * @throws ProgramException throws if response can't be received
     */
    public void start(MatcherTree tree) throws ProgramException, IOException {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10);
        } catch (IOException e) {
            System.out.println("Cannot start ServerSocket");
            throw new ProgramException();
        }
        Socket s;
        isStopped = false;
        threadPool = new ThreadPool(3);
        while (!isStopped) {
            try {
                s = serverSocket.accept();
            } catch (SocketTimeoutException e) {
                if (isStopped) {
                    threadPool.stop();
                    break;
                }
                continue;
            } catch (IOException e) {
                continue;
            }
            Handler handler;
            Request request = null;
            try {
                request = new Request(s.getInputStream());
                handler = tree.getHandler(request.getPathArray(), request.getPathInfo());
            } catch (NotFound notFound) {   //  handler not found
                HttpHandler.createErrResp(404).send(s.getOutputStream());
                continue;
            } catch (ProgramException e) {
                continue;
            }
            SocketTask socketTask = new SocketTask(request, handler, s.getOutputStream());
            threadPool.execute(socketTask);
        }
    }
}
