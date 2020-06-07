package com.cheng.logger;

import com.cheng.context.ScheduleContext;
import org.slf4j.Logger;
import org.slf4j.Marker;


public class BusinessLogger implements Logger {

	public static String TRACE_ID = "#traceId$";
	public String  moduleName="DEFAULT";
	private Logger logger = null;

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public BusinessLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String s) {
		logger.trace(addTraceInfo(s));
	}

	@Override
	public void trace(String s, Object o) {
		logger.trace(addTraceInfo(s), o);
	}

	@Override
	public void trace(String s, Object o, Object o1) {
		logger.trace(addTraceInfo(s), o1);

	}

	@Override
	public void trace(String s, Object... objects) {
		logger.trace(addTraceInfo(s), objects);
	}

	@Override
	public void trace(String s, Throwable throwable) {
		logger.trace(addTraceInfo(s), throwable);

	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return false;
	}

	@Override
	public void trace(Marker marker, String s) {
		logger.trace(marker, addTraceInfo(s));

	}

	@Override
	public void trace(Marker marker, String s, Object o) {
		logger.trace(marker, addTraceInfo(s), o);

	}

	@Override
	public void trace(Marker marker, String s, Object o, Object o1) {
		logger.trace(marker, addTraceInfo(s), o, o1);

	}

	@Override
	public void trace(Marker marker, String s, Object... objects) {
		logger.trace(marker, addTraceInfo(s), objects);

	}

	@Override
	public void trace(Marker marker, String s, Throwable throwable) {
		logger.trace(marker, addTraceInfo(s), throwable);
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void debug(String s) {
		logger.debug(addTraceInfo(s));

	}

	@Override
	public void debug(String s, Object o) {
		logger.debug(addTraceInfo(s), o);
	}

	@Override
	public void debug(String s, Object o, Object o1) {
		logger.debug(addTraceInfo(s), o, o1);
	}

	@Override
	public void debug(String s, Object... objects) {
		logger.debug(addTraceInfo(s), objects);
	}

	@Override
	public void debug(String s, Throwable throwable) {
		logger.debug(addTraceInfo(s), throwable);

	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return false;
	}

	@Override
	public void debug(Marker marker, String s) {
		logger.debug(marker, addTraceInfo(s));
	}

	@Override
	public void debug(Marker marker, String s, Object o) {
		logger.debug(marker, addTraceInfo(s), o);
	}

	@Override
	public void debug(Marker marker, String s, Object o, Object o1) {
		logger.debug(marker, addTraceInfo(s), o, o1);

	}

	@Override
	public void debug(Marker marker, String s, Object... objects) {
		logger.debug(marker, addTraceInfo(s), objects);
	}

	@Override
	public void debug(Marker marker, String s, Throwable throwable) {
		logger.debug(marker, addTraceInfo(s), throwable);
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void info(String s) {
		logger.info(addTraceInfo(s));
	}

	@Override
	public void info(String s, Object o) {
		logger.info(addTraceInfo(s), o);

	}

	@Override
	public void info(String s, Object o, Object o1) {
		logger.info(addTraceInfo(s), o, o1);
	}

	@Override
	public void info(String s, Object... objects) {
		logger.info(addTraceInfo(s), objects);
	}

	@Override
	public void info(String s, Throwable throwable) {
		logger.info(addTraceInfo(s), throwable);

	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return false;
	}

	@Override
	public void info(Marker marker, String s) {
		logger.info(marker, addTraceInfo(s));
	}

	@Override
	public void info(Marker marker, String s, Object o) {
		logger.info(marker, addTraceInfo(s), o);
	}

	@Override
	public void info(Marker marker, String s, Object o, Object o1) {
		logger.info(marker, addTraceInfo(s), o1);
	}

	@Override
	public void info(Marker marker, String s, Object... objects) {
		logger.info(marker, addTraceInfo(s), objects);
	}

	@Override
	public void info(Marker marker, String s, Throwable throwable) {
		logger.info(marker, addTraceInfo(s), throwable);

	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public void warn(String s) {
		logger.warn(addTraceInfo(s));

	}

	@Override
	public void warn(String s, Object o) {
		logger.warn(addTraceInfo(s), o);
	}

	@Override
	public void warn(String s, Object... objects) {
		logger.warn(addTraceInfo(s), objects);
	}

	@Override
	public void warn(String s, Object o, Object o1) {
		logger.warn(addTraceInfo(s), o, o1);
	}

	@Override
	public void warn(String s, Throwable throwable) {
		logger.warn(addTraceInfo(s), throwable);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return false;
	}

	@Override
	public void warn(Marker marker, String s) {
		logger.warn(marker, addTraceInfo(s));

	}

	@Override
	public void warn(Marker marker, String s, Object o) {
		logger.warn(marker, addTraceInfo(s), o);
	}

	@Override
	public void warn(Marker marker, String s, Object o, Object o1) {
		logger.warn(marker, addTraceInfo(s), o, o1);
	}

	@Override
	public void warn(Marker marker, String s, Object... objects) {
		logger.warn(marker, addTraceInfo(s), objects);
	}

	@Override
	public void warn(Marker marker, String s, Throwable throwable) {
		logger.warn(marker, addTraceInfo(s), throwable);

	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void error(String s) {
		logger.error(addTraceInfo(s));
	}

	@Override
	public void error(String s, Object o) {
		logger.error(addTraceInfo(s), o, o);
	}

	@Override
	public void error(String s, Object o, Object o1) {
		logger.error(addTraceInfo(s), o, o1);
	}

	@Override
	public void error(String s, Object... objects) {
		logger.error(addTraceInfo(s), objects);

	}

	@Override
	public void error(String s, Throwable throwable) {
		logger.error(addTraceInfo(s), throwable);

	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return false;
	}

	@Override
	public void error(Marker marker, String s) {
		logger.error(marker, addTraceInfo(s));

	}

	@Override
	public void error(Marker marker, String s, Object o) {
		logger.error(marker, addTraceInfo(s), o);
	}

	@Override
	public void error(Marker marker, String s, Object o, Object o1) {
		logger.error(marker, addTraceInfo(s), o, o1);
	}

	@Override
	public void error(Marker marker, String s, Object... objects) {
		logger.error(marker, addTraceInfo(s), objects);
	}

	@Override
	public void error(Marker marker, String s, Throwable throwable) {
		logger.error(marker, addTraceInfo(s), throwable);
	}

	private String addTraceInfo(String s) {
	    StringBuilder stringBuilder = new StringBuilder();
//		if (moduleName!= null && moduleName.length() > 0) {
//			stringBuilder.append("[").append(moduleName).append("] - ");
//		}
		String traceId = ScheduleContext.get(TRACE_ID);
		if (traceId != null && traceId.length() > 0) {
			stringBuilder.append(" - ").append(traceId).append(" - ");
		}
		stringBuilder.append(s);
		return stringBuilder.toString();
	}
}
