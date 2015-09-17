package dso.test;

import dso.DSO;

public class TestClient {

    public static void main(String[] args) {
        BigData b = Test.setup();

        while (true) {
            try {
                long t = System.currentTimeMillis();
                // DSO.share(b);
                t = t - System.currentTimeMillis();
                Thread.sleep(30);
                DSO.lock(b, null);
                Thread.sleep(1333);
                DSO.unlock(b, null);
                Thread.sleep(333);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
