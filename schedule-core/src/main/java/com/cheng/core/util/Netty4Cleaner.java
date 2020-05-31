package com.cheng.core.util;

import com.cheng.logger.BusinessLoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Netty4Cleaner {


    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",Netty4Cleaner.class);

    private static AtomicBoolean isCleaning = new AtomicBoolean(false);

    public static boolean clean(int second){
        synchronized (Netty4Cleaner.class){
            if (isCleaning.get()){
                return true;
            }
            isCleaning.set(true);
        }
        final AtomicInteger timeWait = new AtomicInteger(second);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (timeWait.get() > 0){
                logger.info("wait for schedule application clean [{}]", timeWait.getAndDecrement());

            }
            if (timeWait.get() == 0){
                countDownLatch.countDown();
            }
        }, 0, 1, TimeUnit.SECONDS);


         try{
             countDownLatch.wait();
         }catch (Throwable e){
             logger.error("clean exception ", e);

         }

         return  true;
    }
}
