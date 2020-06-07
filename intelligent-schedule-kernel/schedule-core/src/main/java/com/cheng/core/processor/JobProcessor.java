package com.cheng.core.processor;

import com.cheng.core.entity.ScheduleTaskCommand;

import java.util.List;

public interface JobProcessor {


    /**
     * get the data.when taskCommand is SCHEDULE,then invoke this method
     * user should implements this method
     * @param scheduleTaskCommand
     * @return
     */
    default Long fetchData(ScheduleTaskCommand scheduleTaskCommand){
        return 0L;
    }

    /**
     * dispatch data
     *
     * @param scheduleTaskCommand
     * @param taskData
     */
    int dispatchData(ScheduleTaskCommand scheduleTaskCommand, List<Object> taskData) throws Exception;


    /**
     * the logic of task
     *
     * @return return the success process count
     */
    int processData(ScheduleTaskCommand scheduleTaskCommand);

}
