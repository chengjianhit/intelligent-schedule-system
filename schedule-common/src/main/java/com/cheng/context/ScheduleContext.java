package com.cheng.context;

import java.util.HashMap;
import java.util.Map;


public class ScheduleContext {

	private static ThreadLocal<Map> localContext = new ThreadLocal();

	public static void addParam(String key, Object v) {
		if (localContext.get() == null) {
			localContext.set(new HashMap());
		}
		Map map = localContext.get();

		map.put(key, v);
	}

	public static <T> T get(String key) {
		if (localContext.get() == null) {
			return null;
		}
		Map map = localContext.get();
		return (T) map.get(key);
	}
}
