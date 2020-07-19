package com.cheng.schedule.server.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * operation_log
 * @author 
 */
public class OperationLog implements Serializable {
    private Long id;

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

    /**
     *  创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}