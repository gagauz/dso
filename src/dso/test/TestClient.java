package dso.test;

import dso.DSO;

public class TestClient {

    public static void main(String[] args) {
        BigData b = Test.setup();
        while (true) {
            try {
                for (int i = 0; i < 10; i++) {
                    long t = System.currentTimeMillis();
                    DSO.share(b);
                    t = t - System.currentTimeMillis();
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
