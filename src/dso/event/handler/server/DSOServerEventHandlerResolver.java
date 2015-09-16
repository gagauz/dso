package dso.event.handler.server;

import dso.event.*;
import dso.event.api.DSOEventHandler;
import dso.event.error.DSOUnhandledEventException;
import dso.event.handler.DSOEventHandlerResolver;
import dso.thread.DSOServerThread;

import java.util.HashMap;

public class DSOServerEventHandlerResolver implements DSOEventHandlerResolver {

    private final HashMap<Class<? extends DSOEvent>, DSOEventHandler> serverEventHandlers = new HashMap<Class<? extends DSOEvent>, DSOEventHandler>();

    public DSOServerEventHandlerResolver(DSOServerThread dsoServerThread) {
        serverEventHandlers.put(DSOLockEvent.class, new DSOLockEventHandler(dsoServerThread));
        serverEventHandlers.put(DSOUnlockEvent.class, new DSOUnlockEventHandler(dsoServerThread));
        serverEventHandlers.put(DSOJoinEvent.class, new DSOJoinEventHandler(dsoServerThread));
        serverEventHandlers.put(DSONoopEvent.class, new DSONoopEventHandler());
        serverEventHandlers.put(DSOShareObjectEvent.class, new DSOShareObjectEventHandler(dsoServerThread));
    }

    @Override
    public DSOEventHandler resolve(DSOEvent event) throws DSOUnhandledEventException {
        DSOEventHandler handler = serverEventHandlers.get(event.getClass());
        if (null == handler) {
            throw new DSOUnhandledEventException("Unable to resolve envent handler for " + event.getClass());
        }
        return handler;
    }
}
