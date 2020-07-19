package com.cheng.schedule.server.repository;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.dao.domain.GroupPermission;
import com.cheng.schedule.server.dao.domain.GroupPermissionExample;
import com.cheng.schedule.server.dao.mapper.GroupPermissionDao;
import com.cheng.schedule.server.entity.GroupPermissionDO;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class GroupPermissionService {
	private static Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",GroupPermissionService.class);

    @Autowired
	GroupPermissionDao groupPermissionDao;

    public GroupPermissionDO queryGroupPermission(String userId, Long groupId) {
        if (StringUtils.isEmpty(userId) || groupId == null) {
            throw new RuntimeException("the userId and groupId must not null");
        }
        GroupPermissionExample groupPermission = new GroupPermissionExample();
        GroupPermissionExample.Criteria criteria = groupPermission.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andUserIdEqualTo(userId);
        criteria.andIsDeletedEqualTo(false);
        List<GroupPermission> groupPermissions = groupPermissionDao.selectByExample(groupPermission);
        if (CollectionUtils.isEmpty(groupPermissions)) {
            return null;
        }
        GroupPermission groupPermission1 = groupPermissions.get(0);
        GroupPermissionDO groupPermissionDO = new GroupPermissionDO();
        BeanUtils.copyProperties(groupPermission1, groupPermissionDO);
        return groupPermissionDO;
    }

    public Boolean addGroupPermission(GroupPermissionDO groupPermissionDO)
    {
		if(StringUtils.isEmpty(groupPermissionDO.getUserId()) || groupPermissionDO.getGroupId() == null) {
			logger.error("add group permission use id and group id must not null");
		    return false;
		}
        GroupPermission groupPermission = new GroupPermission();
        BeanUtils.copyProperties(groupPermissionDO,groupPermission);
        int i = groupPermissionDao.insertSelective(groupPermission);
        groupPermissionDO.setId(groupPermission.getId());
        return i > 0;
    }

    public boolean deleteGroup(GroupPermissionDO groupPermissionDO) {
		if(StringUtils.isEmpty(groupPermissionDO.getUserId()) || groupPermissionDO.getGroupId() == null) {
			logger.error("del group permission use id and group id must not null");
			return false;
		}
		GroupPermissionExample groupPermissionExample = new GroupPermissionExample();
		GroupPermissionExample.Criteria criteria = groupPermissionExample.createCriteria();
		criteria.andGroupIdEqualTo(groupPermissionDO.getGroupId());
		criteria.andUserIdEqualTo(groupPermissionDO.getUserId());
		int i = groupPermissionDao.softDeleteByExample(groupPermissionExample);
        return i> 0;
    }

	/**
	 * query the userGroup List;
	 * @param userId
	 * @return
	 */
	public List<Long> queryUserGroupList(String userId) {
		if(StringUtils.isEmpty(userId)) {
			return Lists.newArrayList();
		}
		GroupPermissionExample groupPermissionExample = new GroupPermissionExample();
		GroupPermissionExample.Criteria criteria = groupPermissionExample.createCriteria();
		criteria.andIsDeletedEqualTo(false);
		criteria.andUserIdEqualTo(userId);
		List<GroupPermission> groupPermissionList = groupPermissionDao.selectByExample(groupPermissionExample);

		if(CollectionUtils.isNotEmpty(groupPermissionList))
		{
			List<Long> collect = groupPermissionList.stream().map(s -> s.getGroupId()).collect(Collectors.toList());
			return collect;
		}
		return Lists.newArrayList();
	}

	/**
	 * determine if the user is admin for this group
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean isAdmin(String userId,Long groupId)
	{
		if(StringUtils.isEmpty(userId) || groupId == null || groupId < 0) {
			return false;
		}
		GroupPermissionExample groupPermissionExample = new GroupPermissionExample();
		GroupPermissionExample.Criteria criteria = groupPermissionExample.createCriteria();
		criteria.andIsDeletedEqualTo(false);
		criteria.andUserIdEqualTo(userId);
		criteria.andGroupIdEqualTo(groupId);
		criteria.andIsAdminEqualTo(true);
		List<GroupPermission> groupPermissionList = groupPermissionDao.selectByExample(groupPermissionExample);
		return CollectionUtils.isNotEmpty(groupPermissionList);
	}
	/**
	 * 查看当前分组下的所有用户
	 * @param groupId
	 * @return
	 */
	public List<String> queryGroupUser(String groupId) {
		if(StringUtils.isEmpty(groupId)) {
			return Lists.newArrayList();
		}
		GroupPermissionExample groupPermissionExample = new GroupPermissionExample();
		GroupPermissionExample.Criteria criteria = groupPermissionExample.createCriteria();
		criteria.andIsDeletedEqualTo(false);
		criteria.andGroupIdEqualTo(Long.parseLong(groupId));
		List<GroupPermission> groupPermissionList = groupPermissionDao.selectByExample(groupPermissionExample);
		if(CollectionUtils.isNotEmpty(groupPermissionList)) {
			List<String> collect = groupPermissionList.stream().map(s -> s.getUserId()).collect(Collectors.toList());
			return collect;
		}
		return Lists.newArrayList();
	}
}

