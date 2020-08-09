package com.cheng.schedule.server.provider;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.repository.TaskGroupBusinessService;
import com.cheng.shedule.server.api.TaskGroupRemoteService;
import com.cheng.shedule.server.common.AiPageResponse;
import com.cheng.shedule.server.common.AiResponse;
import com.cheng.shedule.server.dto.PermissionDTO;
import com.cheng.shedule.server.dto.TaskGroupDTO;
import com.cheng.shedule.server.query.TaskGroupQueryParam;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(version = "${dubbo.service.version}",group = "${dubbo.service.group}")
public class TaskGroupRemoteServiceImpl implements TaskGroupRemoteService {
    private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskGroupRemoteServiceImpl.class);
    @Autowired
    private TaskGroupBusinessService taskGroupBusinessService;

    @Override
    @Transactional
    public AiResponse<Boolean> addGroup(String userId, TaskGroupDTO taskGroupDTO) {
        try{
            return taskGroupBusinessService.addGroup(userId,taskGroupDTO);
        }catch (Throwable e)
        {
            logger.error("add group error ",e);
            return AiResponse.fail("add group error ");
        }
    }

    @Override
    public AiPageResponse<TaskGroupDTO> queryGroup(String userId, TaskGroupQueryParam taskGroupQueryParam) {
        try{
            return taskGroupBusinessService.queryGroup(userId,taskGroupQueryParam);
        }catch (Throwable e)
        {
            logger.error("query group error ",e);
            return AiPageResponse.fail("query group error ");
        }
    }

    @Override
    public AiResponse<Boolean> modifyGroup(String userId, TaskGroupDTO taskGroupDTO) {
        try{
            return taskGroupBusinessService.modifyGroup(userId,taskGroupDTO);
        }catch (Throwable e)
        {
            logger.error("modify group error ",e);
            return AiResponse.fail("modify group error ");
        }
    }

    @Override
    public AiResponse<Boolean> delGroup(String userId, Long groupId) {
        try{
            return taskGroupBusinessService.delGroup(userId,groupId);
        }catch (Throwable e)
        {
            logger.error("del group error ",e);
            return AiResponse.fail("del group error ");
        }
    }

    @Override
    public AiPageResponse<String> queryGroupUser(String userId, Long groupId) {
        try{
            return taskGroupBusinessService.queryGroupUser(userId,groupId);
        }catch (Throwable e)
        {
            logger.error("query groupUser error ",e);
            return AiPageResponse.fail("query groupUser error ");
        }
    }

    @Override
    public AiResponse<Boolean> addGroupUser(String userId, PermissionDTO permissionDTO) {
        try{
            return taskGroupBusinessService.addGroupUser(userId,permissionDTO);
        }catch (Throwable e)
        {
            logger.error("add GroupUser error ",e);
            return AiResponse.fail("add GroupUser error ");
        }
    }

    @Override
    public AiResponse<Boolean> delGroupUser(String userId, PermissionDTO permissionDTO) {
        try{
            return taskGroupBusinessService.delGroupUser(userId,permissionDTO);
        }catch (Throwable e)
        {
            logger.error("del GroupUser error ",e);
            return AiResponse.fail("del GroupUser error ");
        }
    }
}
