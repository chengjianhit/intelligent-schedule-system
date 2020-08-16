package com.cheng.schedule.server.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.TaskGroup;
import com.cheng.schedule.server.dao.domain.TaskGroupExample;
import com.cheng.schedule.server.dao.mapper.TaskGroupDao;
import com.cheng.schedule.server.entity.PageInfo;
import com.cheng.schedule.server.entity.TaskGroupDO;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * query taskGroup list
     *
     * @param taskGroupDO
     * @param userGroupList
     * @param pageInfo
     * @return
     */
    public PageInfo<TaskGroupDO> queryGroupList(TaskGroupDO taskGroupDO, List<Long> userGroupList, PageInfo pageInfo) {
        TaskGroupExample taskGroupExample = new TaskGroupExample();
        TaskGroupExample.Criteria criteria = taskGroupExample.createCriteria();
        criteria.andIsDeletedEqualTo(false);
        if (taskGroupDO.getId() != null) {
            criteria.andIdEqualTo(taskGroupDO.getId());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(taskGroupDO.getGroupCode())) {
            criteria.andGroupCodeLike("%" + taskGroupDO.getGroupCode() + "%");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(taskGroupDO.getGroupName())) {
            criteria.andGroupNameLike("%" + taskGroupDO.getGroupName() + "%");
        }
        if (CollectionUtils.isNotEmpty(userGroupList)) {
            criteria.andIdIn(userGroupList);
        }
        long total = taskGroupDao.countByExample(taskGroupExample);
        taskGroupExample.setOrderByClause("id");
        taskGroupExample.setOffset((pageInfo.getPageNum() - 1) * pageInfo.getPageSize());
        List<TaskGroup> taskGroups = taskGroupDao.selectByExample(taskGroupExample);
        PageInfo<TaskGroupDO> instance = PageInfo.instance(pageInfo.getPageSize(), pageInfo.getPageNum(), total);
        if (CollectionUtils.isNotEmpty(taskGroups)) {
            List<TaskGroupDO> collect = taskGroups.stream().map(s -> {
                TaskGroupDO taskGroupDO1 = new TaskGroupDO();
                BeanUtils.copyProperties(s, taskGroupDO1);
                return taskGroupDO1;
            }).collect(Collectors.toList());
            instance.setDataList(collect);
        }
        return instance;
    }

    /**
     * update task group
     *
     * @param taskGroupDO
     * @return
     */
    public boolean modifyTaskGroup(TaskGroupDO taskGroupDO) {
        if (taskGroupDO.getId() == null) {
            logger.error("modify task group ,param id must not null {}", JSON.toJSONString(taskGroupDO));
            return false;
        }
        TaskGroup taskGroup = new TaskGroup();
        BeanUtils.copyProperties(taskGroupDO, taskGroup);
        int i = taskGroupDao.updateByPrimaryKeySelective(taskGroup);
        return i > 0;
    }


    /**
     * delete group
     *
     * @param groupId
     * @return
     */
    public boolean deleteGroup(Long groupId) {
        if(groupId == null){
            logger.error("del task group ,param id must not null ");
            return false;
        }
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setId(groupId);
        taskGroup.setIsDeleted(true);
        int i = taskGroupDao.updateByPrimaryKeySelective(taskGroup);
        return i > 0;
    }
}
