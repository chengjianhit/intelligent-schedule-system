package com.cheng.util;

import com.cheng.logger.BusinessLoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TimeUtils.class);


    public static void sleep(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (Throwable e) {
            logger.error("sleep time error when task generate", e);
        }
    }
}
