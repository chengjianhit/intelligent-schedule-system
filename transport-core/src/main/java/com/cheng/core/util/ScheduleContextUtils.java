package com.cheng.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ScheduleContextUtils implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ScheduleContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName){
        return  (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class clz){
        return  (T) applicationContext.getBean(clz);
    }
}
