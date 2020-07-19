package com.cheng.schedule.server.provider;

import com.cheng.logger.BusinessLoggerFactory;
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

//    @Autowired
//    TaskConfigBusinessService taskConfigBusinessService;

    @Autowired
    UserPermissionService userPermissionService;


    @Override
    public AiResponse<Boolean> addTask(String userId, TaskConfigDTO taskConfigDTO) {
        return null;
    }

    @Override
    public AiPageResponse<TaskConfigDTO> queryTask(String userId, TaskConfigQueryParam taskConfigQueryParam) {
        return null;
    }

    @Override
    public AiResponse<Boolean> modifyTask(String userId, TaskConfigDTO taskConfigDTO) {
        return null;
    }

    @Override
    public AiResponse<Boolean> delTask(String userId, Long taskId) {
        return null;
    }
}
