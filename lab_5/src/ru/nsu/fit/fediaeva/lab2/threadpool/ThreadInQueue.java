package ru.nsu.fit.fediaeva.lab2.threadpool;

import java.util.ArrayList;

public class ThreadInQueue extends Thread {
    private ArrayList<Runnable> execQueue;
    private boolean isStopped;

    ThreadInQueue(ArrayList<Runnable> q) {
        execQueue = q;
        isStopped = false;
    }

    void setStop() {
        isStopped = true;
    }

    @Override
    public void run() {
        synchronized (execQueue) {
            while (!execQueue.isEmpty() || !isStopped) {

                while (execQueue.isEmpty() && !isStopped) {
                    try {
                        execQueue.wait(100);
                    } catch (InterruptedException ignored) {
                    }
                }

                if (!execQueue.isEmpty()) {     //  if stopped, but have task
                    Runnable task = execQueue.remove(0);
                    task.run();
                    System.out.println(getId());
                    try {
                        execQueue.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


