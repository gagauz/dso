package dso.test;

import dso.event.DSOShareObjectEvent;
import dso.thread.DSOClient;
import dso.thread.DSOServer;

public class Test {

    static int s = 0;

    public static BigData setup() {
        BigData p = new BigData();
        for (int i = 0; i < 20; i++) {
            BigData c = add(p);
            for (int j = 0; j < 20; j++) {
                add(c);
            }
        }
        return p;
    }

    public static BigData add(BigData parent) {
        BigData b = new BigData();
        b.setName("child " + s++);
        b.setParent(parent);
        return b;
    }

    public static void main(String[] args) {

        final BigData b = setup();

        final DSOServer srv = new DSOServer();

        Thread t1 = new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                srv.startServer();
            };
        };

        Thread t3 = new Thread() {
            {
                setName("Push-thread");
                setDaemon(true);
            }

            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Push 4s to cl");
                srv.propagate(null, new DSOShareObjectEvent(b));
            };
        };

        t1.start();
        t3.start();

        Thread t2 = new Thread() {
            {
                setName("Client-thread-1");
                setDaemon(true);
            }

            @Override
            public void run() {

                try {
                    sleep(1000);
                    DSOClient cl1 = new DSOClient();
                    //					sleep(1000);
                    //					final BigData b = setup();
                    //					sleep(1000);
                    //					b.__synchronize();
                    //                    b.__synchronize();
                    // cl1.disconnect();
                    System.out.println("Push 1s to srv");
                    cl1.share(b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };

        t2.start();

        while (true) {
        }
    }
}
