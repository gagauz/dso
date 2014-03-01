package dso.event.handler.client;

import java.util.logging.Logger;

import dso.event.DSOEvent;
import dso.event.handler.server.DSOEventHandler;
import dso.thread.DSOClient;

public class DSOCLientJoinEventHandler implements DSOEventHandler {
	
	private static final Logger log = Logger.getLogger("DSOServer");

	public DSOCLientJoinEventHandler(DSOClient dsoClient) {
	}

	@Override
	public void handleEvent(DSOEvent event) {
		log.info("Client >> I'm just joined to the server");
	}

}
