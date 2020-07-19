package com.cheng.schedule.server.provider;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.service.TaskCommandBusinessService;
import com.cheng.shedule.server.api.TaskCommandRemoteService;
import com.cheng.shedule.server.common.AiResponse;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author jiancheng
 */
@Service(version = "${dubbo.service.version}", group = "${dubbo.service.group}")
public class TaskCommandRemoteServiceImpl implements TaskCommandRemoteService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskCommandRemoteServiceImpl.class);

    @Autowired
    private TaskCommandBusinessService taskCommandBusinessService;



    @Override
    public AiResponse<Boolean> abortCommand(String userId, Long commandId) {
        try{
            return taskCommandBusinessService.abortCommand(userId,commandId);
        }catch (Throwable e)
        {
            logger.error("abort command error ",e);
            return AiResponse.fail("abort command error ");
        }
    }

    @Override
    public AiResponse<Boolean> copyCommand(String userId, Long commandId) {
        try{
            return taskCommandBusinessService.copyCommand(userId,commandId);
        }catch (Throwable e)
        {
            logger.error("copy command error ",e);
            return AiResponse.fail("copy command error ");
        }
    }
}
