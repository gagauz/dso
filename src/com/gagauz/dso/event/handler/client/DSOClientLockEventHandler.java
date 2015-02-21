package com.gagauz.dso.event.handler.client;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.DSOLockEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOClient;


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
