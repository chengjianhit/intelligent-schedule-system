package com.cheng.schedule.server.repository;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.TaskConfig;
import com.cheng.schedule.server.dao.domain.TaskConfigExample;
import com.cheng.schedule.server.dao.mapper.TaskConfigDao;
import com.cheng.schedule.server.entity.PageInfo;
import com.cheng.schedule.server.entity.TaskConfigDO;
import com.cheng.shedule.server.enums.TaskState;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskConfigService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskConfigService.class);

    @Autowired
    public TaskConfigDao taskConfigDao;

    /**
     * 添加task调度任务（console端配置）
     * @param taskConfigDO
     * @return
     */
    public boolean addTaskConfig(TaskConfigDO taskConfigDO) {
        TaskConfig taskConfig = new TaskConfig();
        BeanUtils.copyProperties(taskConfigDO, taskConfig);
        int insert = taskConfigDao.insertSelective(taskConfig);
        taskConfigDO.setId(taskConfig.getId());
        return insert >0;
    }


    /**
     * update task config
     * @param taskConfigDO
     * @return
     */
    public boolean modifyTaskConfig(TaskConfigDO taskConfigDO) {
        if (taskConfigDO.getId() == null) {
            logger.error("modify task config must has taskId ");
            return false;
        }
        TaskConfig taskConfig = new TaskConfig();
        BeanUtils.copyProperties(taskConfigDO, taskConfig);
        taskConfig.setUpdateTime(new Date(System.currentTimeMillis()));
        int updateResult = taskConfigDao.updateByPrimaryKeySelective(taskConfig);
        return updateResult > 0;
    }

    /**
     * get all task by taskId
     * @param taskId
     * @return
     */
    public TaskConfigDO queryTaskById(Long taskId) {
        if (taskId == null) {
            logger.error("query task config must has taskId ");
            return null;
        }
        TaskConfig taskConfig = taskConfigDao.selectByPrimaryKey(taskId);
        if(taskConfig == null || taskConfig.getIsDeleted()) {
            return null;
        }

        TaskConfigDO taskConfigDO = new TaskConfigDO();
        BeanUtils.copyProperties(taskConfig, taskConfigDO);
        return taskConfigDO;
    }

    /**
     * query TaskConfig List By GroupId
     * @param groupId
     * @return
     */
    public List<TaskConfigDO> queryTaskByGroupId(Long groupId)
    {
        if(groupId == null) {
            logger.error("groupId must not null");
        }
        TaskConfigExample taskConfigExample = new TaskConfigExample();
        TaskConfigExample.Criteria criteria = taskConfigExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andIsDeletedEqualTo(false);
        List<TaskConfig> taskConfigs = taskConfigDao.selectByExample(taskConfigExample);
        if(CollectionUtils.isEmpty(taskConfigs)) {
            return Lists.newArrayList();
        }
        List<TaskConfigDO> collect = taskConfigs.stream().map(s -> {
            TaskConfigDO taskConfigDO = new TaskConfigDO();
            BeanUtils.copyProperties(s, taskConfigDO);
            return taskConfigDO;
        }).collect(Collectors.toList());
        return collect;
    }
    /**
     * del task
     * @param taskId
     * @return
     */
    public boolean delTask(Long taskId) {
        if (taskId == null) {
            logger.error("del task config must has taskId ");
            return false;
        }
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.setUpdateTime(new Date(System.currentTimeMillis()));
        taskConfig.setId(taskId);
        taskConfig.setIsDeleted(true);
        int i = taskConfigDao.updateByPrimaryKeySelective(taskConfig);
        return i > 0;
    }

    /**
     * pause schedule
     * @param taskId
     * @return
     */
    public boolean pauseSchedule(Long taskId)
    {
        if (taskId == null) {
            logger.error("pause task config must has taskId ");
            return false;
        }
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.setUpdateTime(new Date(System.currentTimeMillis()));
        taskConfig.setId(taskId);
        taskConfig.setTaskState(TaskState.PAUSE.name());
        int i = taskConfigDao.updateByPrimaryKeySelective(taskConfig);
        return i > 0;
    }
    /**
     * resume schedule
     * @param taskId
     * @return
     */
    public boolean resumeSchedule(Long taskId)
    {
        if (taskId == null) {
            logger.error("resume task config must has taskId ");
            return false;
        }
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.setUpdateTime(new Date(System.currentTimeMillis()));
        taskConfig.setId(taskId);
        taskConfig.setTaskState(TaskState.NORMAL.name());
        int i = taskConfigDao.updateByPrimaryKeySelective(taskConfig);
        return i > 0;
    }
    /**
     * disable schedule
     * @param taskId
     * @return
     */
    public boolean disableSchedule(Long taskId)
    {
        if (taskId == null) {
            logger.error("disable task config must has taskId ");
            return false;
        }
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.setUpdateTime(new Date(System.currentTimeMillis()));
        taskConfig.setId(taskId);
        taskConfig.setTaskState(TaskState.DISABLE.name());
        int i = taskConfigDao.updateByPrimaryKeySelective(taskConfig);
        return i > 0;
    }


    /**
     * query the task list
     * @param taskConfigDO
     * @param pageInfo
     * @return
     */
    public PageInfo<TaskConfigDO> queryTaskList(TaskConfigDO taskConfigDO, PageInfo pageInfo) {
        TaskConfigExample taskConfigExample = new TaskConfigExample();
        TaskConfigExample.Criteria criteria = taskConfigExample.createCriteria();
        criteria.andIsDeletedEqualTo(false);
        if (taskConfigDO.getGroupId() != null) {
            criteria.andGroupIdEqualTo(taskConfigDO.getGroupId());
        }
        if (StringUtils.isNotEmpty(taskConfigDO.getTaskName())) {
            criteria.andTaskNameLike("%"+taskConfigDO.getTaskName()+"%");
        } if (StringUtils.isNotEmpty(taskConfigDO.getTaskState())) {
            criteria.andTaskStateEqualTo(taskConfigDO.getTaskState());
        }
        long total = taskConfigDao.countByExample(taskConfigExample);
        taskConfigExample.setOrderByClause("id");
        taskConfigExample.setOffset((pageInfo.getPageNum()-1) * pageInfo.getPageSize());
        taskConfigExample.setLimit(pageInfo.getPageSize());
        List<TaskConfig> taskConfigs = taskConfigDao.selectByExample(taskConfigExample);
        PageInfo<TaskConfigDO> instance = PageInfo.instance(pageInfo.getPageSize(), pageInfo.getPageNum(), total);
        if(CollectionUtils.isNotEmpty(taskConfigs)) {
            List<TaskConfigDO> collect = taskConfigs.stream().map(s -> {
                TaskConfigDO taskConfigDO1 = new TaskConfigDO();
                BeanUtils.copyProperties(s,taskConfigDO1);
                return taskConfigDO1;
            }).collect(Collectors.toList());
            instance.setDataList(collect);
        }
        return instance;
    }

}
