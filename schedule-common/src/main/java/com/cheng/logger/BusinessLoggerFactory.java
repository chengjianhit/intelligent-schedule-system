package com.cheng.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *@Description TODO
 *@Author beedoorwei
 *@Date 2010/04/23
 *@Version 1.0.0
 */
public class BusinessLoggerFactory {

	private static Map<String, BusinessLogger> loggerMap = new ConcurrentHashMap<>();

	/**
	 *
	 * @param className
	 * @return
	 */
	public static <T> Logger getBusinessLogger(Class className) {
		String name = className.getName();
		if (!loggerMap.containsKey(name)) {
			Logger logger = LoggerFactory.getLogger(name);
			synchronized (BusinessLoggerFactory.class) {
				BusinessLogger businessLogger = new BusinessLogger(logger);
				loggerMap.put(name, businessLogger);
			}
		}
		return loggerMap.get(name);
	}
	/**
	 * @param  moduleName
	 * @param className
	 * @return
	 */
	public static <T> Logger getBusinessLogger(String moduleName,Class className) {
		String name = className.getName();
		if (!loggerMap.containsKey(name)) {
			Logger logger = LoggerFactory.getLogger(name);
			synchronized (BusinessLoggerFactory.class) {
				BusinessLogger businessLogger = new BusinessLogger(logger);
				businessLogger.setModuleName(moduleName);
				loggerMap.put(name, businessLogger);
			}
		}
		return loggerMap.get(name);
	}

	public static void main(String[] args) {
		System.out.println(BusinessLogger.class.getName());
	}
}
