package com.cheng.schedule.server.repository;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.mapper.TaskScheduleDao;
import com.cheng.schedule.server.entity.TaskScheduleDO;
import com.cheng.util.InetUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TaskScheduleService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskScheduleService.class);

    @Value("${task.schedule.generateTimeRange:30}")
    private int lockRange;

    @Autowired
    private TaskScheduleDao taskScheduleDao;

    /**
     * 获取需要调度的任务、并锁定
     * @return
     */
    public List<TaskScheduleDO> lockGetForSchedule(){
        List<TaskScheduleDO> taskScheduleDOList = Lists.newArrayList();
        //每次按照本机IP进行锁定10条记录
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("scheduleHost", InetUtils.getSelfIp());
        paramsMap.put("size", 10);
        paramsMap.put("lockRange", lockRange);

        int i = taskScheduleDao.lockWaitForSchedule(paramsMap);
        logger.info("batch schedule lock {}", i);
        //每次拉起20条记录，防止之前没有被执行的
        List<Map<String, Object>> taskScheduleList = taskScheduleDao.getWaitForSchedule(paramsMap);
        if (CollectionUtils.isNotEmpty(taskScheduleList)){
            taskScheduleDOList = taskScheduleList.stream().map(task ->{
                TaskScheduleDO taskScheduleDO = new TaskScheduleDO();
                PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(taskScheduleDO.getClass());
                //PropertyDescriptor类,通过反射进行赋值
                for (PropertyDescriptor targetPd: propertyDescriptors){
                    //获取某个字段的Set(Write)方法并调用
                    Method writeMethod = targetPd.getWriteMethod();
                    if (writeMethod != null){
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())){
                            writeMethod.setAccessible(true);
                        }
                        try {
                            writeMethod.invoke(taskScheduleDO, task.get(targetPd.getName()));
                        } catch (Throwable e) {
                            logger.error("add value to task schedule fail ", e);
                        }
                    }
                }

                return taskScheduleDO;
            }).collect(Collectors.toList());
        }
        logger.info("batch schedule get {}", taskScheduleDOList.size());
        return taskScheduleDOList;
    }
}
