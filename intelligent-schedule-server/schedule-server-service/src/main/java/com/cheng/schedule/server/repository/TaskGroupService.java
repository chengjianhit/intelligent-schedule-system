package com.cheng.schedule.server.repository;

import com.alibaba.nacos.client.utils.StringUtils;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.TaskGroup;
import com.cheng.schedule.server.dao.mapper.TaskGroupDao;
import com.cheng.schedule.server.entity.TaskGroupDO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TaskGroupService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskGroupService.class);

    @Autowired
    private TaskGroupDao taskGroupDao;

    Map<Long, TaskGroupDO> groupCache = Maps.newHashMap();


    public TaskGroupDO getById(Long groupId) {
        if (!groupCache.containsKey(groupId)) {
            TaskGroup taskGroup = taskGroupDao.selectByPrimaryKey(groupId);
            if (taskGroup != null) {
                TaskGroupDO taskGroupDO = new TaskGroupDO();
                BeanUtils.copyProperties(taskGroup, taskGroupDO);
                groupCache.put(groupId, taskGroupDO);
            }
        }
        return groupCache.get(groupId);
    }

    /**
     * 新增一条Group记录
     * @param taskGroupDO
     * @return
     */
    public boolean addGroup(TaskGroupDO taskGroupDO){
        TaskGroup taskGroup = new TaskGroup();
        BeanUtils.copyProperties(taskGroupDO, taskGroup);
        String lastestGroupCode = taskGroupDao.getLastestGroupCode();
        taskGroup.setGroupCode(TaskGroup.START_GROUP_CODE);
        if (StringUtils.isNotBlank(lastestGroupCode)){
            long newGroupCode = Long.parseLong(lastestGroupCode) + 1;
            taskGroup.setGroupCode(String.valueOf(newGroupCode));
        }
        int i = taskGroupDao.insertSelective(taskGroup);
        taskGroupDO.setId(taskGroup.getId());
        return i > 0;
    }
}
