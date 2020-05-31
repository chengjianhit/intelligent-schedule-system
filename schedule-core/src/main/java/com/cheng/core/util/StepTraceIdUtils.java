package com.cheng.core.util;


import java.util.UUID;

/**
 * 单点追踪ID生成器
 */
public class StepTraceIdUtils {


	/**
	 * traceId
	 * @return
	 */
	public static final String traceId() {
		return UUID.randomUUID().toString();
	}
}
