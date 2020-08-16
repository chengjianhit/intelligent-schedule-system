package com.cheng.schedule.server.provider;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.service.TaskScheduleBusinessService;
import com.cheng.shedule.server.api.TaskScheduleRemoteService;
import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskScheduleDTO;
import com.cheng.shedule.server.query.TaskScheduleQueryParam;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "${dubbo.service.version}", group = "${dubbo.service.group}")
public class TaskScheduleRemoteServiceImpl implements TaskScheduleRemoteService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskScheduleRemoteServiceImpl.class);

    @Autowired
    private TaskScheduleBusinessService taskScheduleBusinessService;

    @Override
    public AiResponse<Boolean> pauseSchedule(String userId, Long taskId) {
        return null;
    }

    @Override
    public AiResponse<Boolean> resumeSchedule(String userId, Long taskId) {
        return null;
    }

    @Override
    public AiPageResponse<TaskScheduleDTO> queryTaskSchedule(String userId, TaskScheduleQueryParam taskScheduleQueryParam) {
        return null;
    }

    @Override
    public AiResponse<TaskScheduleDTO> queryTaskProcess(String userId, Long commandId) {
        return null;
    }

    @Override
    public AiResponse<Boolean> cleanRunningLock(String userId, Long taskId) {
        return null;
    }
}
