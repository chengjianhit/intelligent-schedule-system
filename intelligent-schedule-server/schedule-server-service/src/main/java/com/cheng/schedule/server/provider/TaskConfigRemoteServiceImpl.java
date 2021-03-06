package com.cheng.schedule.server.provider;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.service.TaskConfigBusinessService;
import com.cheng.schedule.server.service.UserPermissionService;
import com.cheng.shedule.server.api.TaskConfigRemoteService;
import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskConfigDTO;
import com.cheng.shedule.server.query.TaskConfigQueryParam;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jiancheng
 */
@Service(version = "${dubbo.service.version}",group = "${dubbo.service.group}")
public class TaskConfigRemoteServiceImpl implements TaskConfigRemoteService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskConfigRemoteServiceImpl.class);

    @Autowired
    TaskConfigBusinessService taskConfigBusinessService;


    @Override
    public AiResponse<Boolean> addTask(String userId, TaskConfigDTO taskConfigDTO) {
        try{
            return taskConfigBusinessService.addTask(userId,taskConfigDTO);
        }catch (Throwable e)
        {
            logger.error("add task error ",e);
            return AiResponse.fail("add task error ");
        }
    }

    @Override
    public AiPageResponse<TaskConfigDTO> queryTask(String userId, TaskConfigQueryParam taskConfigQueryParam) {
        try{
            return taskConfigBusinessService.queryTask(userId, taskConfigQueryParam);
        }catch (Throwable e){
            logger.error("query task error ",e);
            return AiPageResponse.fail("query task error ");
        }
    }

    @Override
    public AiResponse<Boolean> modifyTask(String userId, TaskConfigDTO taskConfigDTO) {
        try{
            return taskConfigBusinessService.modifyTask(userId,taskConfigDTO);
        }catch (Throwable e)
        {
            logger.error("modify task error ",e);
            return AiResponse.fail("modify task error ");
        }
    }

    @Override
    public AiResponse<Boolean> delTask(String userId, Long taskId) {
        try {
            return taskConfigBusinessService.delTask(userId, taskId);
        } catch (Throwable e) {
            logger.error("del task error ", e);
            return AiResponse.fail("del task error ");
        }
    }
}
