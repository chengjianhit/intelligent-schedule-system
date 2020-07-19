package com.cheng.schedule.server.service;

import com.cheng.schedule.server.entity.GroupPermissionDO;
import com.cheng.schedule.server.repository.GroupPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserPermissionService {

    @Autowired
    GroupPermissionService groupPermissionService;

    /**
     * verify if the user has the permission to operation this group
     *
     * @param userId
     * @param groupId
     * @return
     */
    public boolean isPermission(String userId, Long groupId) {
        if(StringUtils.isEmpty(userId) ) {
            return false;
        }
        if(groupId == null)
		{
			return true;
		}
        GroupPermissionDO groupPermissionDO = groupPermissionService.queryGroupPermission(userId, groupId);
        if (groupPermissionDO != null) {
            return true;
        }
        return false;
    }
}
