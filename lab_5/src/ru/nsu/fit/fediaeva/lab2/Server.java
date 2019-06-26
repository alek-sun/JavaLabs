package ru.nsu.fit.fediaeva.lab2;

import ru.nsu.fit.fediaeva.lab2.constructors.RequestConstructor;
import ru.nsu.fit.fediaeva.lab2.exception.NotFound;
import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.pipeline.Handler;
import ru.nsu.fit.fediaeva.lab2.handlers.HttpHandler;
import ru.nsu.fit.fediaeva.lab2.matchers.MatcherTree;
import ru.nsu.fit.fediaeva.lab2.threadpool.SocketTask;
import ru.nsu.fit.fediaeva.lab2.threadpool.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public void setPort(int p) {
        port = p;
    }

    /**
     * Start of server, waiting for socket and their processing by response processor (proc)
     *
     * @throws ProgramException throws if response can't be received
     */
    public void start(MatcherTree tree) throws ProgramException, IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket s;
        ThreadPool threadPool = new ThreadPool(5);
        while (true) {
            try {
                s = serverSocket.accept();
            } catch (IOException e) {
                continue;
            }
            Handler handler;
            RequestConstructor request;
            try {
                request = new RequestConstructor(s.getInputStream(), tree);
                handler = request.findHandler();
            } catch (NotFound notFound) {   //  handler not found
                ResponseSender sender;
                sender = new ResponseSender(s.getOutputStream());
                sender.sendResponse(HttpHandler.createErrResp(404));
                return;
            }
            SocketTask socketTask = new SocketTask(request, handler, s.getOutputStream());
            threadPool.execute(socketTask);
        }
    }
}
