package dso.event.handler.client;

import dso.event.DSOEvent;
import dso.event.DSOLockEvent;
import dso.event.handler.server.DSOEventHandler;
import dso.thread.DSOClient;

public class DSOClientLockEventHandler implements DSOEventHandler {

	private final DSOClient dsoClient;
	public DSOClientLockEventHandler(DSOClient dsoClient) {
		this.dsoClient = dsoClient;
	}

	@Override
	public void handleEvent(DSOEvent event) {
		dsoClient.grantLock((DSOLockEvent) event);
	}

}
