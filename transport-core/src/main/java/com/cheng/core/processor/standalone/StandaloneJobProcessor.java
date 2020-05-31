package com.cheng.core.processor.standalone;

import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.processor.JobProcessor;
import com.cheng.logger.BusinessLoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

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
        return 0;
    }
}
