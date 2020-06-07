package com.cheng.core.processor.distribute;

import com.alibaba.fastjson.JSON;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.context.TaskContextUtils;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.core.processor.JobProcessor;
import com.cheng.core.service.ConnectionManagerService;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.core.util.StepTraceIdUtils;
import com.cheng.core.util.TaskNodeUtil;
import com.cheng.logger.BusinessLoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

public abstract class DistributedJobProcessor implements JobProcessor {

    protected Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", getClass());

    @Override
    public int dispatchData(ScheduleTaskCommand scheduleTaskMsg, List<Object> taskData) throws Exception {
        if (CollectionUtils.isEmpty(taskData)) {
            logger.error("dispatch data should not empty");
            return 0;
        }
        if (!TaskContextUtils.stateNormal(scheduleTaskMsg)) {
            logger.info("suspend the task [{}] ", JSON.toJSONString(scheduleTaskMsg));
            return 0;
        }
        if (!StringUtils.equals(scheduleTaskMsg.getCurrentNodeName(), "ROOT")) {
            logger.error("the first level must root {} ", JSON.toJSONString(scheduleTaskMsg));
            return 0;
        }
        ScheduleTaskCommand instanceTasmKsg = new ScheduleTaskCommand<>();
        instanceTasmKsg.setCommand(Command.RUN);
        instanceTasmKsg.setCommandId(scheduleTaskMsg.getCommandId());
        instanceTasmKsg.setProcessor(scheduleTaskMsg.getProcessor());
        instanceTasmKsg.setCurrentNodeName(TaskNodeUtil.nextLevel(scheduleTaskMsg.getCurrentNodeName()));
        instanceTasmKsg.setStartDate(scheduleTaskMsg.getStartDate());
        instanceTasmKsg.setStartHost(scheduleTaskMsg.getStartHost());
        instanceTasmKsg.setStepHost(scheduleTaskMsg.getStartHost());
        instanceTasmKsg.setTaskContext(scheduleTaskMsg.getTaskContext());
        instanceTasmKsg.setTaskDataList(taskData);
        instanceTasmKsg.setTaskId(scheduleTaskMsg.getTaskId());
        instanceTasmKsg.setScheduleTraceId(scheduleTaskMsg.getScheduleTraceId());
        instanceTasmKsg.setStepTraceId(StepTraceIdUtils.traceId());

        //获取connectionInstance连接
        ConnectionManagerService connectionManagerService = ScheduleContextUtils.getBean(ConnectionManagerService.class);
        ConnectionInstance connectionInstance = connectionManagerService.selectInstance(scheduleTaskMsg.getGroupCode());
        if (connectionInstance == null) {
            logger.error("not found instance for group {} ", scheduleTaskMsg.getGroupCode());
            return 0;
        }
        //默认策略重试三次
        for (int i=0; i<3; i++){
            try {
                boolean b = connectionInstance.sendData(instanceTasmKsg);
                logger.info("dispatch data to server [{}] ,result [{}]", connectionInstance.getRemoteServer(), b);
                if (b) {
                    return CollectionUtils.size(taskData);
                }
            } catch (Exception e) {
                logger.error("dispatch data to server " + connectionInstance.getRemoteServer() + " error", e);
            }

        }

        return 0;
    }
}
