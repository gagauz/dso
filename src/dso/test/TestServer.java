package dso.test;

import dso.thread.DSOServer;

public class TestServer {

    public static void main(String[] args) {
		final DSOServer srv = new DSOServer();
		srv.startServer();
    }

}
