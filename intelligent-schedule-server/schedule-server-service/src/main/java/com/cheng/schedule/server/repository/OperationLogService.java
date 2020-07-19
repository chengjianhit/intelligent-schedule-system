package com.cheng.schedule.server.repository;

import com.cheng.schedule.server.dao.domain.OperationLog;
import com.cheng.schedule.server.dao.mapper.OperationLogDao;
import com.cheng.schedule.server.entity.OperationLogDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OperationLogService {

	@Autowired
	private OperationLogDao operationLogDao;

	public boolean addOpLog(OperationLogDO operationLogDO) {
		OperationLog operationLog1 = new OperationLog();
		BeanUtils.copyProperties(operationLogDO, operationLog1);
		int i = operationLogDao.insertSelective(operationLog1);
		return i > 0;
	}
}
