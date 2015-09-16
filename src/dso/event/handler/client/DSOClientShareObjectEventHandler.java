package dso.event.handler.client;

import dso.DSO;
import dso.event.DSOEvent;
import dso.event.DSOShareObjectEvent;
import dso.event.api.DSOEventHandler;
import dso.thread.DSOClient;


public class DSOClientShareObjectEventHandler implements DSOEventHandler {

	public DSOClientShareObjectEventHandler(DSOClient dsoClient) {
	}

	@Override
	public void handleEvent(DSOEvent event) {
		DSOShareObjectEvent shareEvent = (DSOShareObjectEvent) event;
		DSO.updateLocal(shareEvent.getDelta());
	}

}
