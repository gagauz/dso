package com.gagauz.dso.event.handler.client;

import com.gagauz.dso.DSO;
import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.DSOShareObjectEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOClient;


public class DSOClientShareObjectEventHandler implements DSOEventHandler {

	public DSOClientShareObjectEventHandler(DSOClient dsoClient) {
	}

	@Override
	public void handleEvent(DSOEvent event) {
		DSOShareObjectEvent shareEvent = (DSOShareObjectEvent) event;
		DSO.updateLocal(shareEvent.getDelta());
	}

}
