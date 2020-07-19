package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.GroupPermission;
import com.cheng.schedule.server.dao.domain.GroupPermissionExample;
import org.springframework.stereotype.Repository;

/**
 * GroupPermissionDao继承基类
 */
@Repository
public interface GroupPermissionDao extends MyBatisBaseDao<GroupPermission, Long, GroupPermissionExample> {
}