package com.gagauz.dso.test;

import java.util.ArrayDeque;

public class TestThread {
    private static int n = 2;

    public static void main(String[] args) {

        MasterThread master = new MasterThread();
        master.run();

        MasterThread[] threads = new MasterThread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = create(master);
        }

        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        while (true) {
            try {

                Thread.sleep(2000);
                master.release().resume();
            } catch (Exception e) {
            }
        }
    }

    public static MasterThread create(final MasterThread master0) {
        return new MasterThread() {
            final MasterThread master;
            {
                this.master = master0;
                setDaemon(true);
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        //Put in queue
                        System.out.println(getName() + " " + " Put in queue");
                        master.put(this);
                        //Wait for permission
                        System.out.println(getName() + " " + " Wait for permission");
                        this.suspend();
                        //Do action
                        System.out.println(getName() + " " + " Do action");
                    } catch (Exception e) {
                    }
                }
            }
        };
    }
}

class MasterThread extends Thread {
    ArrayDeque<Thread> slaves = new ArrayDeque<Thread>();

    public void put(Thread th) {
        slaves.addLast(th);
    }

    public Thread release() {
        System.out.println("release");
        if (!slaves.isEmpty()) {
            return slaves.removeFirst();
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Aaaaaaaaa " + getName());
        super.finalize();
    }

}
