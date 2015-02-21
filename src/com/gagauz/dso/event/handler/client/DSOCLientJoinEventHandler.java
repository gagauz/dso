package com.gagauz.dso.event.handler.client;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOClient;

import java.util.logging.Logger;


public class DSOCLientJoinEventHandler implements DSOEventHandler {
	
	private static final Logger log = Logger.getLogger("DSOServer");

	public DSOCLientJoinEventHandler(DSOClient dsoClient) {
	}

	@Override
	public void handleEvent(DSOEvent event) {
		log.info("Client >> I'm just joined to the server");
	}

}
