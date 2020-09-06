package com.cheng.schedule.server.repository;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.TaskCommand;
import com.cheng.schedule.server.dao.domain.TaskCommandDispatchLog;
import com.cheng.schedule.server.dao.mapper.TaskCommandDao;
import com.cheng.schedule.server.entity.TaskCommandDO;
import com.cheng.shedule.server.enums.TaskCommandState;
import com.cheng.util.InetUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class TaskCommandService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskCommandService.class);

    @Autowired
    private TaskCommandDao taskCommandDao;

    @Value("${task.schedule.dispatchTimeRange:5}")
    private int lockRange;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<TaskCommandDO> lockGetTaskCommand() {
        List<TaskCommandDO> taskCommandDOList = Lists.newArrayList();
        try {
            Map<String, Object> paramsMap = Maps.newHashMap();
            paramsMap.put("publishHost", InetUtils.getSelfIp());
            paramsMap.put("size", 10);
            paramsMap.put("lockRange", lockRange);
            //使用本机IP先锁定task_command表里面的10条数据
            int i = taskCommandDao.lockTaskCommand(paramsMap);
            //如果死机被重新拉起，可以把之前锁定的执行一遍,所以取20条数据
            if (i > 0) {
                paramsMap.put("size", 10 * 2);
                List<TaskCommand> taskCommandList = taskCommandDao.getWaitForCommand(paramsMap);
                logger.info("batch command get {}", taskCommandList.size());
                if (CollectionUtils.isNotEmpty(taskCommandList)) {
                    taskCommandList.stream().forEach(command -> {
                        TaskCommandDO taskCommandDO = new TaskCommandDO();
                        BeanUtils.copyProperties(command, taskCommandDO);
                        taskCommandDOList.add(taskCommandDO);
                    });
                }
            }
        } catch (Exception e) {
            logger.error("batch lock command error ", e);
        }

        return taskCommandDOList;
    }

    /**
     * add new record into task_command
     * @param taskCommandDO
     * @return
     */
    public boolean addCommand(TaskCommandDO taskCommandDO){
        TaskCommand taskCommand = new TaskCommand();
        BeanUtils.copyProperties(taskCommandDO,taskCommand);
        int insert = taskCommandDao.insertSelective(taskCommand);
        taskCommandDO.setId(taskCommand.getId());
        return insert > 0;
    }

    /**
     * 将TaskCommand状态恢复为 INIT出事状态
     * @param taskCommandDO
     */
    public void resetLockInfo(TaskCommandDO taskCommandDO) {
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("id", taskCommandDO.getId());
        taskCommandDao.resetLockInfo(paramsMap);

    }

    /**
     * @param taskCommandDO
     * @param remoteServer
     * @return
     */
    public boolean dispatchSuccess(TaskCommandDO taskCommandDO, String remoteServer) {
        TaskCommand taskCommand = new TaskCommand();
        taskCommand.setId(taskCommandDO.getId());
        taskCommand.setTargetHost(remoteServer);
        taskCommand.setState(TaskCommandState.RUN.name());
        int i = taskCommandDao.updateByPrimaryKeySelective(taskCommand);
        return i>0;
    }

    /**
     * update the task_command record PUBLISH_FAIL
     * @param taskCommandDO
     * @return
     */
    public boolean dispatchFailture(TaskCommandDO taskCommandDO) {
        TaskCommand taskCommand = new TaskCommand();
        taskCommand.setId(taskCommandDO.getId());
        taskCommand.setState(TaskCommandState.PUBLISH_FAIL.name());
        int i = taskCommandDao.updateByPrimaryKeySelective(taskCommand);
        return i > 0;
    }

    public TaskCommandDO getCommandById(Long commandId) {
        TaskCommand taskCommand = taskCommandDao.selectByPrimaryKey(commandId);
        if (taskCommand != null) {
            TaskCommandDO taskCommandDO = new TaskCommandDO();
            BeanUtils.copyProperties(taskCommand, taskCommandDO);
            return taskCommandDO;
        }
        return null;
    }

    public boolean abortCommand(Long commandId) {
        TaskCommand taskCommand = new TaskCommand();
        taskCommand.setId(commandId);
        taskCommand.setState(TaskCommandState.ABORT.name());
        int i = taskCommandDao.updateByPrimaryKeySelective(taskCommand);
        return i > 0;
    }

    /**
     *
     * @param param
     * @return
     */
    public List<TaskCommandDispatchLog> queryCommandDispatchState(Map<String, Object> param) {
        return taskCommandDao.queryCommandDispatchState(param);
    }
}
