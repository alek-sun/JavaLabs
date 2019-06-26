package ru.nsu.fit.fediaeva.server.threadpool;

import java.util.ArrayList;

public class ThreadPool {
    private ArrayList<Runnable> execQueue;
    private ArrayList<ThreadInQueue> threads;

    public ThreadPool(int count) {
        execQueue = new ArrayList<>();
        threads = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            threads.add(new ThreadInQueue(execQueue));
        }
        for (ThreadInQueue t : threads) {
            t.start();
        }
    }

    synchronized public void execute(Runnable r) {
        execQueue.add(r);
    }


    synchronized public void stop() {
        int countTreads = threads.size();
        for (int i = 0; i < countTreads; i++) {
            threads.get(0).setStop();
            try {
                threads.get(0).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threads.remove(0);
        }
    }
}