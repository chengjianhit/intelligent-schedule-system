package com.cheng.core.service;

import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.core.transport.ServerTransportService;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.config.ScheduleServerConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ScheduleClientService {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",ScheduleClientService.class);

    @Autowired
    ScheduleServerConfig scheduleServerConfig;

    @Autowired
    private ConnectionManagerService connectionManagerService;


    /**
     * send data
     * @param scheduleTaskMsg
     * @param dataList
     * @param <T>
     * @return
     */
    public <T> boolean sendData(ScheduleTaskCommand scheduleTaskMsg, List<T> dataList)throws Exception {
        String groupCode= scheduleTaskMsg.getGroupCode();
        scheduleTaskMsg.setTaskDataList(dataList);
        ConnectionInstance connectionInstance = connectionManagerService.selectInstance(groupCode);
        if(connectionInstance == null) {
            throw new RuntimeException("there is no connection instance for groupCode" + groupCode);
        }
        return connectionInstance.sendData(scheduleTaskMsg);
    }
}
