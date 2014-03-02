package dso.event.handler.client;

import dso.event.*;
import dso.event.api.DSOEventHandler;
import dso.event.error.DSOUnhandledEventException;
import dso.event.handler.DSOEventHandlerResolver;
import dso.event.handler.server.DSONoopEventHandler;
import dso.thread.DSOClient;

import java.util.HashMap;

public class DSOClientEventHandlerResolver implements DSOEventHandlerResolver {

    private final HashMap<Class<? extends DSOEvent>, DSOEventHandler> clientEventHandlers = new HashMap<Class<? extends DSOEvent>, DSOEventHandler>();

    public DSOClientEventHandlerResolver(DSOClient dsoClient) {
        clientEventHandlers.put(DSOLockEvent.class, new DSOClientLockEventHandler(dsoClient));
        clientEventHandlers.put(DSOJoinEvent.class, new DSOCLientJoinEventHandler(dsoClient));
        clientEventHandlers.put(DSONoopEvent.class, new DSONoopEventHandler());
        clientEventHandlers.put(DSOShareObjectEvent.class, new DSOClientShareObjectEventHandler(dsoClient));
    }

    @Override
    public DSOEventHandler resolve(DSOEvent event) throws DSOUnhandledEventException {
        DSOEventHandler handler = clientEventHandlers.get(event.getClass());
        if (null == handler) {
            throw new DSOUnhandledEventException("Unable to resolve envent handler for " + event.getClass());
        }
        return handler;
    }
}
