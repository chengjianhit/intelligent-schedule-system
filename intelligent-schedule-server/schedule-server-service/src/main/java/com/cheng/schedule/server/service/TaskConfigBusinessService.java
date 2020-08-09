package com.cheng.schedule.server.service;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.*;
import com.cheng.schedule.server.enums.OperationType;
import com.cheng.schedule.server.repository.OperationLogService;
import com.cheng.schedule.server.repository.TaskConfigService;
import com.cheng.schedule.server.repository.TaskGroupService;
import com.cheng.schedule.server.repository.TaskScheduleService;
import com.cheng.schedule.server.trigger.cron.CronExpression;
import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.TaskConfigDTO;
import com.cheng.shedule.server.query.TaskConfigQueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cheng.schedule.server.enums.OperationType.*;

@Component
public class TaskConfigBusinessService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskConfigBusinessService.class);

    @Autowired
    TaskConfigService taskConfigService;

    @Autowired
    TaskScheduleService taskScheduleService;

    @Autowired
    UserPermissionService userPermissionService;

    @Autowired
    OperationLogService operationLogService;

    @Autowired
    TaskGroupService taskGroupService;


    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    public AiResponse<Boolean> addTask(String userId, TaskConfigDTO taskConfigDTO) {
        //校验是否有权限
        boolean permission = userPermissionService.isPermission(userId, taskConfigDTO.getGroupId());
        if (!permission) {
            String msg = String.format("the user [%s] have no permission to operation this command [%s]", userId,
                    taskConfigDTO.getId());
            logger.warn(msg);
            return AiResponse.fail(msg);
        }
        boolean valdiateResult = validateExpress(taskConfigDTO);
        if (!valdiateResult) {
            logger.error("cron express validate fail " + taskConfigDTO.getScheduleExpress());
            return AiResponse.fail("cron express " + taskConfigDTO.getScheduleExpress() + " not validate");
        }
        TaskConfigDO taskConfigDO = new TaskConfigDO();
        BeanUtils.copyProperties(taskConfigDTO, taskConfigDO);
        boolean addResult = taskConfigService.addTaskConfig(taskConfigDO);
        operationLogService.addOpLog(OperationLogDO.instance(OperationType.ADD_TASK, taskConfigDO.getId(),
                userId, "add task result : " + addResult));
        if (addResult){
            TaskScheduleDO taskScheduleDO = buildTaskSchedule(taskConfigDO);
            boolean b = taskScheduleService.addTaskSchedule(taskScheduleDO);
            operationLogService
                    .addOpLog(OperationLogDO.instance(OperationType.ADD_SCHEDULE, taskScheduleDO.getId(), userId, "add schedule result : " + b));
            return AiResponse.success(b);
        }
        return AiResponse.fail("add task config fail ");


    }

    /**
     * build task schedule instance
     * @param taskConfigDO
     * @return
     */
    private TaskScheduleDO buildTaskSchedule(TaskConfigDO taskConfigDO) {
        TaskScheduleDO taskScheduleDO = new TaskScheduleDO();
        taskScheduleDO.setTaskId(taskConfigDO.getId());
        return taskScheduleDO;
    }

    private boolean validateExpress(TaskConfigDTO taskConfigDTO) {
        try {
            CronExpression cronExpression = new CronExpression(taskConfigDTO.getScheduleExpress());
            Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(new Date());
            return true;
        } catch (Exception e){
        }
        return false;
    }

    /**
     * query task list
     *
     * @param userId
     * @param taskConfigQueryParam
     * @return
     */
    public AiPageResponse<TaskConfigDTO> queryTask(String userId, TaskConfigQueryParam taskConfigQueryParam) {
        boolean permission = userPermissionService.isPermission(userId, taskConfigQueryParam.getGroupId());
        if (!permission) {
            String errorMsg = String.format("the user [{}] has no permission for query", userId);
            logger.error(errorMsg);
            return AiPageResponse.fail(errorMsg);
        }
        TaskConfigDO taskConfigDO = new TaskConfigDO();
        BeanUtils.copyProperties(taskConfigQueryParam, taskConfigDO);
        PageInfo instance = PageInfo
                .instance(taskConfigQueryParam.getPageSize(), taskConfigQueryParam.getPageNum(), -1L);
        PageInfo<TaskConfigDO> taskConfigDOPageInfo = taskConfigService.queryTaskList(taskConfigDO, instance);
        List<TaskConfigDO> dataList = taskConfigDOPageInfo.getDataList();
        AiPageResponse<TaskConfigDTO> success = AiPageResponse.emptySucess();
        success.withPageInfo(taskConfigDOPageInfo.getTotalPage(), taskConfigDOPageInfo.getPageNum(),
                taskConfigDOPageInfo.getPageSize());
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<TaskConfigDTO> collect = dataList.stream().map(s -> {
                TaskConfigDTO taskConfigDTO = new TaskConfigDTO();
                BeanUtils.copyProperties(s, taskConfigDTO);
                TaskGroupDO byId = taskGroupService.getById(s.getGroupId());
                if (byId != null) {
                    taskConfigDTO.setGroupCode(byId.getGroupCode());
                }
                Long runningCmdId = taskScheduleService.getRunningCmdId(s.getId());
                if(runningCmdId != null) {
                    taskConfigDTO.setRunCmdId(runningCmdId);
                }
                return taskConfigDTO;
            }).collect(Collectors.toList());
            success.withBusinessData(collect);
        }
        return success;
    }

    public AiResponse<Boolean> modifyTask(String userId, TaskConfigDTO taskConfigDTO) {

        boolean permission = userPermissionService.isPermission(userId, taskConfigDTO.getGroupId());
        if (!permission) {
            String errorMsg = String.format("the user [{}] has no permission for update task", userId);
            logger.error(errorMsg);
            return AiResponse.fail(errorMsg);
        }
        boolean valdiateResult = validateExpress(taskConfigDTO);
        if (!valdiateResult) {
            logger.error("cron express validate fail " + taskConfigDTO.getScheduleExpress());
            return AiResponse.fail("cron express " + taskConfigDTO.getScheduleExpress() + " not validate");
        }
        TaskConfigDO taskConfigDO = new TaskConfigDO();
        BeanUtils.copyProperties(taskConfigDTO, taskConfigDO);
        boolean b = taskConfigService.modifyTaskConfig(taskConfigDO);
        operationLogService.addOpLog(
                OperationLogDO.instance(MODIFY_TASK, taskConfigDO.getId(), userId, "modify task result : " + b));
        return AiResponse.success(b);
    }

    /**
     * 删除配置的任务
     * @param userId
     * @param taskId
     * @return
     */
    @Transactional
    public AiResponse<Boolean> delTask(String userId, Long taskId) {
        TaskConfigDO taskConfigDO = taskConfigService.queryTaskById(taskId);
        boolean permission = userPermissionService.isPermission(userId, taskConfigDO.getGroupId());
        if (!permission) {
            String errorMsg = String.format("the user [{}] has no permission for delete task", userId);
            logger.error(errorMsg);
            return AiResponse.fail(errorMsg);
        }
        boolean b = taskConfigService.delTask(taskId);
        operationLogService
                .addOpLog(OperationLogDO.instance(DEL_TASK, taskConfigDO.getId(), userId, "del task result : " + b));
        if (b) {
            //update the schedule task
            boolean b1 = taskScheduleService.delSchedule(taskId);
            operationLogService.addOpLog(OperationLogDO
                    .instance(DEL_SCHEDULE, taskConfigDO.getId(), userId, "del schedule result : " + b1));
            if (!b1) {
                return AiResponse.fail("del schedule fail");
            }
            return AiResponse.success(true);
        }
        return AiResponse.fail("del task fail");


    }
}
