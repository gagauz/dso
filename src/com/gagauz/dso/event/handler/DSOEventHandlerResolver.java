package com.gagauz.dso.event.handler;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.event.error.DSOUnhandledEventException;

public interface DSOEventHandlerResolver {

    DSOEventHandler resolve(DSOEvent event) throws DSOUnhandledEventException;
}
