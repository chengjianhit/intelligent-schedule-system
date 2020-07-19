package com.cheng.schedule.server.entity;

import com.cheng.schedule.server.enums.OperationType;
import lombok.Data;


@Data
public class OperationLogDO {
	/**
	 * 操作类型
	 */
	private String operationType;

	/**
	 * 操作Id
	 */
	private Long operationId;

	/**
	 * 操作人
	 */
	private String operationUser;

	/**
	 * 操作描述
	 */
	private String operationDesc;

	public static OperationLogDO instance(OperationType operationLogType, Long operationId, String operationUser, String operationDesc) {
		OperationLogDO operationLogDO = new OperationLogDO();
		operationLogDO.operationDesc = operationDesc;
		operationLogDO.operationId = operationId;
		operationLogDO.operationType = operationLogType.name();
		operationLogDO.operationUser = operationUser;
		return operationLogDO;
	}
}
