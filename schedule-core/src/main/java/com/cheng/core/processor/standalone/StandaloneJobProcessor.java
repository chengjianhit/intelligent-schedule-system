package com.cheng.core.processor.standalone;

import com.alibaba.fastjson.JSON;
import com.cheng.core.context.TaskContextUtils;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.enums.Command;
import com.cheng.core.processor.JobProcessor;
import com.cheng.core.store.TaskExecRecordService;
import com.cheng.core.util.ScheduleContextUtils;
import com.cheng.core.util.StepTraceIdUtils;
import com.cheng.core.util.TaskNodeUtil;
import com.cheng.logger.BusinessLoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;

import java.util.List;

public abstract class StandaloneJobProcessor implements JobProcessor {

    protected Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", getClass());


    /**
     * singleTask 处理时，dispatchData(接收数据) 和 process data都在本机完成
     * @param scheduleTaskCommand
     * @param taskData
     * @return
     */
    @Override
    public int dispatchData(ScheduleTaskCommand scheduleTaskCommand, List<Object> taskData) throws Exception {
        if (CollectionUtils.isNotEmpty(taskData)){
            logger.error("dispatch data should not empty");
            return 0;
        }
        //判断任务是否可以运行
        if (!TaskContextUtils.stateNormal(scheduleTaskCommand)){
            logger.info("suspend the task [{}] ", JSON.toJSONString(scheduleTaskCommand));
            return 0;
        }

        TaskExecRecordService taskExecRecordService = ScheduleContextUtils.getBean(TaskExecRecordService.class);
        taskExecRecordService.addBatchToTotal(scheduleTaskCommand.getCommandId(), taskData.size());

        ScheduleTaskCommand<Object> taskMsg = new ScheduleTaskCommand<>();
        BeanUtils.copyProperties(scheduleTaskCommand, taskMsg);
        taskMsg.setCommand(Command.RUN);
        taskMsg.setStepTraceId(StepTraceIdUtils.traceId());
        taskMsg.setTaskDataList(taskData);
        taskMsg.setCurrentNodeName(TaskNodeUtil.nextLevel(scheduleTaskCommand.getCurrentNodeName()));

        Integer fail = CollectionUtils.size(taskData);
        int suc = 0;
        try{
            suc = this.processData(scheduleTaskCommand);
            fail = fail - suc;
        }catch (Exception e){
            logger.error("process data error [{}] " + JSON.toJSONString(scheduleTaskCommand), e);

        }

        taskExecRecordService.updateRunResult(scheduleTaskCommand.getCommandId(), suc, fail);
        return CollectionUtils.size(taskData);
    }
}
