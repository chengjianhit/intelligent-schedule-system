package com.cheng.schedule.server.dao.mapper;

import com.cheng.schedule.server.dao.domain.OperationLog;
import com.cheng.schedule.server.dao.domain.OperationLogExample;
import org.springframework.stereotype.Repository;

/**
 * OperationLogDao继承基类
 */
@Repository
public interface OperationLogDao extends MyBatisBaseDao<OperationLog, Long, OperationLogExample> {
}