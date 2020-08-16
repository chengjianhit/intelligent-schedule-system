package com.cheng.schedule.server.service;

import com.alibaba.fastjson.JSON;
import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.entity.*;
import com.cheng.schedule.server.repository.GroupPermissionService;
import com.cheng.schedule.server.repository.OperationLogService;
import com.cheng.schedule.server.repository.TaskConfigService;
import com.cheng.schedule.server.repository.TaskGroupService;
import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.PermissionDTO;
import com.cheng.shedule.server.dto.TaskGroupDTO;
import com.cheng.shedule.server.query.TaskGroupQueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.cheng.schedule.server.enums.OperationType.*;

@Repository
public class TaskGroupBusinessService {

    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER", TaskGroupBusinessService.class);
    @Autowired
    private TaskGroupService taskGroupService;
    @Autowired
    private GroupPermissionService groupPermissionService;
    @Autowired
    private UserPermissionService userPermissionService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private TaskConfigService taskConfigService;

    @Transactional
    public AiResponse<Boolean> addGroup(String userId, TaskGroupDTO taskGroupDTO) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiResponse.fail("user id must not null");
        }
        TaskGroupDO taskGroupDO = new TaskGroupDO();
        BeanUtils.copyProperties(taskGroupDTO, taskGroupDO);
        boolean b = taskGroupService.addGroup(taskGroupDO);
        operationLogService.addOpLog(OperationLogDO.instance(ADD_GROUP, taskGroupDO.getId(), userId, "add group :" + b));
        if (b) {
            List<GroupPermissionDO> groupPermissionDOList = buildGroupUser(taskGroupDO.getId(), userId);
            groupPermissionDOList.stream().forEach(groupPermissionDO -> {
                Boolean groupPermission = groupPermissionService.addGroupPermission(groupPermissionDO);
                operationLogService.addOpLog(OperationLogDO.instance(ADD_GROUP_USER, groupPermissionDO.getId(), userId, "add group user :" + b));
            });
            return AiResponse.success(true);
        }
        return AiResponse.fail("add group fail ");
    }

    /**
     * build group user
     *
     * @param id
     * @param userId
     * @return
     */
    private List<GroupPermissionDO> buildGroupUser(Long id, String userId) {
        List<GroupPermissionDO> collect = Arrays.asList(userId, "jiancheng", "sunwu", "hello").stream().map(s -> {
            GroupPermissionDO groupPermissionDO = new GroupPermissionDO();
            groupPermissionDO.setGroupId(id);
            groupPermissionDO.setUserId(userId);
            groupPermissionDO.setUserName(userId);
            groupPermissionDO.setIsDeleted(false);
            groupPermissionDO.setIsAdmin(true);
            return groupPermissionDO;
        }).collect(Collectors.toList());
        return collect;
    }

    public AiPageResponse<TaskGroupDTO> queryGroup(String userId, TaskGroupQueryParam taskGroupQueryParam) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiPageResponse.fail("user id must not null");
        }
        List<Long> userGroupList = groupPermissionService.queryUserGroupList(userId);
        if (CollectionUtils.isEmpty(userGroupList)) {
            logger.error("user id [{}] has no permission to view group ", userId);
            return AiPageResponse.fail("the user has no permission to view group ");

        }
        TaskGroupDO taskGroupDO = new TaskGroupDO();
        BeanUtils.copyProperties(taskGroupQueryParam, taskGroupDO);
        PageInfo instance = PageInfo.instance(taskGroupQueryParam.getPageSize(), taskGroupQueryParam.getPageNum(), -1L);
        PageInfo<TaskGroupDO> taskGroupDOPageInfo = taskGroupService
                .queryGroupList(taskGroupDO, userGroupList, instance);
        List<TaskGroupDO> dataList = taskGroupDOPageInfo.getDataList();
        AiPageResponse<TaskGroupDTO> success = AiPageResponse.emptySucess();
        success.withPageInfo(taskGroupDOPageInfo.getTotalPage(), taskGroupDOPageInfo.getPageNum(),
                taskGroupDOPageInfo.getPageSize());
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<TaskGroupDTO> collect = dataList.stream().map(s -> {
                TaskGroupDTO taskGroupDTO = new TaskGroupDTO();
                BeanUtils.copyProperties(s, taskGroupDTO);
                return taskGroupDTO;
            }).collect(Collectors.toList());
            success.withBusinessData(collect);
        }
        return success;
    }

    @Transactional
    public AiResponse<Boolean> modifyGroup(String userId, TaskGroupDTO taskGroupDTO) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiResponse.fail("user id must not null");
        }
        boolean permission = userPermissionService.isPermission(userId, taskGroupDTO.getId());
        if (!permission) {
            String msg = "no permission to update the group for user " + userId;
            logger.error(msg);
            return AiResponse.fail(msg);
        }
        if (!StringUtils.isEmpty(taskGroupDTO.getGroupCode())) {
            TaskGroupDO base = taskGroupService.getById(taskGroupDTO.getId());
            if (base == null || !StringUtils.equals(taskGroupDTO.getGroupCode(), base.getGroupCode())) {
                String msg = "can't update group code " + userId;
                logger.error(msg);
                return AiResponse.fail(msg);
            }
        }

        TaskGroupDO taskGroupDO = new TaskGroupDO();
        BeanUtils.copyProperties(taskGroupDTO, taskGroupDO);
        boolean b = taskGroupService.modifyTaskGroup(taskGroupDO);
        operationLogService.addOpLog(
                OperationLogDO.instance(MODIFY_GROUP, taskGroupDTO.getId(), userId, "modify group result: " + b));
        return AiResponse.success(b);
    }

    @Transactional
    public AiResponse<Boolean> delGroup(String userId, Long groupId) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiResponse.fail("user id must not null");
        }
        boolean permission = userPermissionService.isPermission(userId, groupId);
        if (!permission) {
            String msg = "no permission to update the group for user " + userId;
            logger.error(msg);
            return AiResponse.fail(msg);
        }

        boolean b = taskGroupService.deleteGroup(groupId);
        operationLogService.addOpLog(OperationLogDO.instance(DEL_GROUP, groupId, userId, "del result : " + b));
        //all task belong this group must del
        List<TaskConfigDO> taskConfigDOList = taskConfigService.queryTaskByGroupId(groupId);
        if (CollectionUtils.isNotEmpty(taskConfigDOList)) {
            taskConfigDOList.stream().forEach(s -> {
                boolean b1 = taskConfigService.delTask(s.getId());
                operationLogService.addOpLog(OperationLogDO.instance(DEL_TASK, groupId, userId, "group del result task del result : " + b1));
            });
        }
        return AiResponse.success(b);
    }

    /**
     * query group user
     *
     * @param userId
     * @param groupId
     * @return
     */
    public AiPageResponse<String> queryGroupUser(String userId, Long groupId) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiPageResponse.fail("user id must not null");
        }
        List<Long> groupList = groupPermissionService.queryUserGroupList(userId);
        if (CollectionUtils.isEmpty(groupList) || !groupList.contains(groupId)) {
            logger.error("user id [{}] has no permission to view this group [{}]", userId, groupId);
            return AiPageResponse.fail("user has no permission to view this group ");
        }

        List<String> userGroupList = groupPermissionService.queryGroupUser(groupId.toString());
        if (CollectionUtils.isEmpty(userGroupList)) {
            logger.error("user id [{}] has no permission to view group ", userId);
            return AiPageResponse.fail("the user has no permission to view group ");

        }
        AiPageResponse<String> success = AiPageResponse.success(userGroupList);
        success.withPageInfo(1L * userGroupList.size(), 1L, userGroupList.size());
        return success;
    }

    public AiResponse<Boolean> addGroupUser(String userId, PermissionDTO permissionDTO) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(permissionDTO.getUserId())) {
            logger.error("user id must not null");
            return AiResponse.fail("user id must not null");
        }
        boolean admin = groupPermissionService.isAdmin(userId, permissionDTO.getGroupId());
        if (!admin) {
            logger.error("user id [{}] has no permission to operation this group [{}]", userId,
                    permissionDTO.getGroupId());
            return AiResponse.fail("user has no permission to operation this group ");
        }

        GroupPermissionDO groupPermissionDO = new GroupPermissionDO();
        BeanUtils.copyProperties(permissionDTO, groupPermissionDO);
        groupPermissionDO.setUserName(permissionDTO.getUserId());
        Boolean addResult = groupPermissionService.addGroupPermission(groupPermissionDO);
        operationLogService.addOpLog(
                OperationLogDO.instance(ADD_GROUP_USER, groupPermissionDO.getId(), userId, "add group user result: " + addResult));
        return AiResponse.success(addResult);
    }


    public AiResponse<Boolean> delGroupUser(String userId, PermissionDTO permissionDTO) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("user id must not null");
            return AiResponse.fail("user id must not null");
        }
        if (permissionDTO.getGroupId() == null || StringUtils.isEmpty(permissionDTO.getUserId())) {
            logger.error("userId and groupId must not null");
            return AiResponse.fail("userId and groupId must not null");
        }
        boolean admin = groupPermissionService.isAdmin(userId, permissionDTO.getGroupId());
        if (!admin) {
            logger.error("user id [{}] has no permission to operation this group [{}]", userId,
                    permissionDTO.getGroupId());
            return AiResponse.fail("user has no permission to operation this group ");
        }

        GroupPermissionDO groupPermissionDO = groupPermissionService.queryGroupPermission(permissionDTO.getUserId(), permissionDTO.getGroupId());
        boolean b = groupPermissionService.deleteGroup(groupPermissionDO);
        operationLogService.addOpLog(
                OperationLogDO.instance(DEL_GROUP_USER, groupPermissionDO.getId(), userId, "del group user" + JSON.toJSONString(permissionDTO) + " result: " + b));
        return AiResponse.success(b);
    }
}
