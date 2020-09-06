package com.cheng.schedule.server.service;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.TaskCommandDispatchLog;
import com.cheng.schedule.server.entity.OperationLogDO;
import com.cheng.schedule.server.entity.TaskConfigDO;
import com.cheng.schedule.server.entity.TaskGroupDO;
import com.cheng.schedule.server.repository.*;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskScheduleDTO;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.cheng.schedule.server.enums.OperationType.*;

@Component
public class TaskScheduleBusinessService {


    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskScheduleBusinessService.class);

    @Autowired
    TaskCommandService taskCommandService;
    @Autowired
    private TaskGroupService taskGroupService;
    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private TaskConfigService taskConfigService;
    @Autowired
    UserPermissionService userPermissionService;


    /**
     *
     * @param userId
     * @param taskId
     * @return
     */
    @Transactional
    public AiResponse<Boolean> pauseSchedule(String userId, Long taskId) {
        TaskConfigDO taskConfigDO = taskConfigService.queryTaskById(taskId);
        boolean permission = userPermissionService.isPermission(userId, taskConfigDO.getGroupId());
        if (!permission) {
            String errorMsg = String.format("the user [{}] has no permission for pause schedule", userId);
            logger.error(errorMsg);
            return AiResponse.fail(errorMsg);
        }
        boolean b = taskConfigService.pauseSchedule(taskId);
        operationLogService.addOpLog(
                OperationLogDO.instance(PAUSE_SCHEDULE, taskConfigDO.getId(), userId, "pause schedule result : " + b));
        return AiResponse.success(b);
    }

    /**
     *
     * @param userId
     * @param taskId
     * @return
     */
    public AiResponse<Boolean> resumeSchedule(String userId, Long taskId) {
        TaskConfigDO taskConfigDO = taskConfigService.queryTaskById(taskId);
        boolean permission = userPermissionService.isPermission(userId, taskConfigDO.getGroupId());
        if (!permission) {
            String errorMsg = String.format("the user [{}] has no permission for pause schedule", userId);
            logger.error(errorMsg);
            return AiResponse.fail(errorMsg);
        }

        boolean b = taskConfigService.resumeSchedule(taskId);
        operationLogService.addOpLog(OperationLogDO
                .instance(RESUME_SCHEDULE, taskConfigDO.getId(), userId, "resume schedule result : " + b));
        return AiResponse.success(b);
    }

    /**
     * clean lock for task
     * @param userId
     * @param taskId
     * @return
     */
    public AiResponse<Boolean> cleanRunningLock(String userId, Long taskId) {
        boolean b = taskScheduleService.cleanRunningLock(taskId);
        operationLogService.addOpLog(OperationLogDO
                .instance(CLEAN_LOCK, taskId, userId, "clean schedule lock result : " + b));
        return AiResponse.success(b);
    }

    /**
     *
     * @param userId
     * @param commandId
     * @return
     */
    public AiResponse<TaskScheduleDTO> queryTaskProcess(String userId, Long commandId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("commandId", commandId);
        param.put("startIdx", 0);
        param.put("pageSize", 10);

        List<TaskCommandDispatchLog> taskCommandDispatchLogList = taskCommandService
                .queryCommandDispatchState(param);
        if (CollectionUtils.isNotEmpty(taskCommandDispatchLogList)) {
            TaskCommandDispatchLog taskCommandDispatchLog = taskCommandDispatchLogList.get(0);
            TaskScheduleDTO taskScheduleDTO = new TaskScheduleDTO();
            BeanUtils.copyProperties(taskCommandDispatchLog, taskScheduleDTO);
            TaskGroupDO byId = taskGroupService.getById(taskCommandDispatchLog.getGroupId());
            if (byId != null) {
                taskScheduleDTO.setGroupCode(byId.getGroupCode());
            }
            return AiResponse.success(taskScheduleDTO);
        }
        return AiResponse.fail("not found data");

    }
}
