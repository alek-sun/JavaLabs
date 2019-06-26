package ru.nsu.fit.fediaeva.server;

import ru.nsu.fit.fediaeva.server.exception.ProgramException;

public class Main {
    //private static AtomicInteger s = new AtomicInteger(0);

    public static void main(String[] args) {
        //JarInputStream jarInputStream = new JarInputStream(new FileInputStream("HandlersTree.jar"));

       /* ThreadPool threadPool = new ThreadPool(5);
        Runnable MyTask = new MyTask();
        int i;
        for (i = 0; i < 900; i++) {
            threadPool.execute(MyTask);
        }
        threadPool.stop();
        System.out.println("STOPPED блин!!!");*/

        ServerContext ctx = new ServerContext(8080);
        try {
            ctx.startServer();
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }

    /*public static class MyTask implements Runnable {
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                s.incrementAndGet();
            }
            System.out.println("result : " + s);
        }
    }*/
}

/*      //TESTS FOR 4 LAB
        MatcherTree tree = new MatcherTree();
        ArrayList<SegmentMatcher> l1 = new ArrayList<>();
        l1.add(new StringMatcher("abc", "dir"));
        l1.add(new IntMatcher("id"));
        l1.add(new AnyMatcher("something"));

        ArrayList<SegmentMatcher> l2 = new ArrayList<>();
        l2.add(new StringMatcher("abc", "dir"));
        l2.add(new IntMatcher("id"));
        l2.add(new StringMatcher("docs.txt", null));

        tree.register(l1, new FileGetter());
        tree.register(l2, new FileGetter());

        Server server = new Server();
        server.setPort(8080);
        try {
            server.start(tree);
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }*/
        /*ArrayList<SegmentMatcher> l2 = new ArrayList<>();
        l2.add(new StringMatcher("api", "dir"));
        l2.add(new StringMatcher("admin", "Who"));
        l2.add(new IntMatcher("bin"));
        l2.add(new StringMatcher("info", "about"));
        l2.add(new AnyMatcher("name"));

        tree.register(l2, new FileGetter());
        Map<String, String> map = new LinkedHashMap<>();
        try {
            handlers h = tree.getHandler(new ArrayList<>(Arrays.asList("api", "admin", "12", "info", "98uj")), map);
            System.out.println(h.getClass());
            for (Map.Entry<String, String> e: map.entrySet()) {
                System.out.println(e);
            }
        } catch (NotFound notFound) {
            System.out.println("Not foundddd");
        }*/