package com.gagauz.dso.event.handler.server;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOServerThread;

public class DSOJoinEventHandler implements DSOEventHandler {

    private DSOServerThread dsoServerThread;

    public DSOJoinEventHandler(DSOServerThread dsoServerThread) {
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        System.out.println("HANDLE JOIN EVENT / " + dsoServerThread);
    }

}
