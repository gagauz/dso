package com.gagauz.dso.thread;

public interface DSOProcessor {
	void noop();

    void share(Object object);

    void lock(Object object, String name);

    void unlock(Object object, String name);
}
