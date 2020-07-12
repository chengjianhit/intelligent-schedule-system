package com.cheng.core.service;

import com.alibaba.fastjson.JSON;
import com.cheng.core.client.ConnectionInstance;
import com.cheng.core.entity.ScheduleTaskCommand;
import com.cheng.logger.BusinessLoggerFactory;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduleCommandService {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE", ScheduleCommandService.class);

    @Autowired
    private ConnectionManagerService connectionManagerService;

    /**
     * command with random remote server
     *
     * @param scheduleTaskMsg
     * @return
     * @throws Exception
     */
    public Pair<Boolean, String> command(ScheduleTaskCommand scheduleTaskMsg) throws Exception {
        return command(scheduleTaskMsg, null);
    }

    /**
     * send command
     *
     * @param scheduleTaskMsg
     * @return
     */
    public Pair<Boolean, String> command(ScheduleTaskCommand scheduleTaskMsg, String server) throws Exception {
        //此处只需要发送调度命令的meta信息，需要处理的数据，在客户端本地fetch
        scheduleTaskMsg.setTaskDataList(Lists.newArrayList());
        final ConnectionInstance connectionInstance = connectionManagerService.selectInstance(scheduleTaskMsg.getGroupCode(), server);
        if (connectionInstance == null) {
            throw new RuntimeException("there is no connection instance " + JSON.toJSONString(scheduleTaskMsg));
        }
        //retry 3 times to one server ,if the it fail
        for (int i = 0; i < 3; i++) {
            boolean b = connectionInstance.sendCommand(scheduleTaskMsg);
            logger.info("send command [{}] to client [{}],result is [{}]", JSON.toJSONString(scheduleTaskMsg), connectionInstance.getRemoteServer(), b);
            if (b) {
                return Pair.of(true, connectionInstance.getRemoteServer());
            } else {
                logger.warn("[NOTIFY] send command error [{}] context is [{}]", connectionInstance.getRemoteServer(), JSON.toJSONString(scheduleTaskMsg));
            }
        }
        logger.warn("[NOTIFY] send command [{}] to server [{}] try 3 times error ", JSON.toJSONString(scheduleTaskMsg), connectionInstance.getRemoteServer());
        return Pair.of(false, null);
    }

}
