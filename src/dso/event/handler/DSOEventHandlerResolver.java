package dso.event.handler;

import dso.event.DSOEvent;
import dso.event.api.DSOEventHandler;
import dso.event.error.DSOUnhandledEventException;

public interface DSOEventHandlerResolver {

    DSOEventHandler resolve(DSOEvent event) throws DSOUnhandledEventException;
}
