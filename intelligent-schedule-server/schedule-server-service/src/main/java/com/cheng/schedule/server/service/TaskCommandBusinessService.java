package com.cheng.schedule.server.service;

import com.cheng.core.enums.Command;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.OperationLogDO;
import com.cheng.schedule.server.entity.TaskCommandDO;
import com.cheng.schedule.server.repository.OperationLogService;
import com.cheng.schedule.server.repository.TaskCommandService;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.enums.TaskCommandState;
import com.cheng.util.InetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.cheng.schedule.server.enums.OperationType.ABORT_COMMAND;
import static com.cheng.schedule.server.enums.OperationType.COPY_COMMAND;

@Component
public class TaskCommandBusinessService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskCommandBusinessService.class);
    @Autowired
    TaskCommandService taskCommandService;
    @Autowired
    UserPermissionService userPermissionService;
    @Autowired
    private OperationLogService operationLogService;


    @Transactional
    public AiResponse<Boolean> abortCommand(String userId, Long commandId) {
        //服务端下发，立即停止调度
        AiResponse<Boolean> addResult = addCommand(userId, commandId, Command.SCHEDULE_STOP);
        taskCommandService.abortCommand(commandId);
        operationLogService.addOpLog(OperationLogDO.instance(ABORT_COMMAND, commandId, userId,
                "abort result:" + (addResult.isSuccess() && addResult.getData())));
        return addResult;
    }



    public AiResponse<Boolean> copyCommand(String userId, Long commandId) {
        AiResponse<Boolean> addResult = addCommand(userId, commandId, Command.SCHEDULE);
        operationLogService.addOpLog(OperationLogDO.instance(COPY_COMMAND, commandId, userId,
                "abort result:" + (addResult.isSuccess() && addResult.getData())));
        return addResult;
    }


    /**
     *
     * @param userId
     * @param commandId
     * @param command
     * @return
     */
    private AiResponse<Boolean> addCommand(String userId, Long commandId, Command command) {
        if (StringUtils.isEmpty(userId)) {
            String msg = String.format("the userId can't be null");
            logger.warn(msg);
            return AiResponse.fail(msg);
        }
        //根据commandId（主键），获取command记录
        TaskCommandDO taskCommandDO = taskCommandService.getCommandById(commandId);
        if (taskCommandDO == null) {
            String msg = String.format("the commandId [%s] not exist", commandId);
            logger.warn(msg);
            return AiResponse.fail(msg);
        }
        //校验是否有权限
        boolean permission = userPermissionService.isPermission(userId, taskCommandDO.getGroupId());
        if (!permission) {
            String msg = String.format("the user [%s] have no permission to operation this command [%s]", userId,
                    taskCommandDO.getId());
            logger.warn(msg);
            return AiResponse.fail(msg);
        }
        TaskCommandDO newCommand = new TaskCommandDO();
        newCommand.setCommand(command.name());
        newCommand.setPublishTime(new Date(System.currentTimeMillis()));
        newCommand.setScheduleHost(InetUtils.getSelfIp());
        newCommand.setProcessor(taskCommandDO.getProcessor());
        newCommand.setContext(taskCommandDO.getContext());
        newCommand.setGroupId(taskCommandDO.getGroupId());
        newCommand.setTaskId(taskCommandDO.getTaskId());
        newCommand.setScheduleId(taskCommandDO.getScheduleId());
        newCommand.setState(TaskCommandState.INIT.name());
        newCommand.setTargetHost(newCommand.getTargetHost());
        newCommand.setIsDeleted(false);
        newCommand.setPriority(taskCommandDO.getPriority());
        newCommand.setParallel(taskCommandDO.getParallel());
        newCommand.setRefCommandId(commandId);
        boolean b = taskCommandService.addCommand(newCommand);
        return AiResponse.success(b);
    }

}
