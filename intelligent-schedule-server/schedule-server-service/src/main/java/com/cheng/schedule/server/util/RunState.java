package com.cheng.schedule.server.util;

import java.util.concurrent.atomic.AtomicBoolean;


public class RunState {
	static AtomicBoolean runState = new AtomicBoolean(true);

	/**
	 * must know,this method is only for shutdown,if this invoke ,the task generate and command publish will stop
	 */
	public static void stop() {
		runState.set(false);
	}

	/**
	 *
	 */
	public static void resume() {
		runState.set(true);
	}

	public static boolean canRun() {
		return runState.get();
	}
}
