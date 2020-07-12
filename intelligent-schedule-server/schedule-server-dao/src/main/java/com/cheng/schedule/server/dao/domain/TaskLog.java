package com.cheng.schedule.server.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * task_log
 * @author 
 */
public class TaskLog implements Serializable {
    private Long id;

    /**
     * 任务配置ID
     */
    private Long taskId;

    /**
     * 指令ID
     */
    private Long commandId;

    /**
     * 任务分组号
     */
    private String groupCode;

    private Long total;

    private Long success;

    private Long fail;

    /**
     * 任务全局追踪ID
     */
    private String traceId;

    /**
     * 当前步骤编码（预留给多级任务使用）
     */
    private String currentStepCode;

    /**
     * 如果是多级任务，表示父任务ID（预留给多级任务使用）
     */
    private Long parentTaskId;

    /**
     * 调度中心ip
     */
    private String scheduleServer;

    /**
     * 当前步骤是否结束1是，0否
     */
    private Byte isFinished;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public Long getFail() {
        return fail;
    }

    public void setFail(Long fail) {
        this.fail = fail;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCurrentStepCode() {
        return currentStepCode;
    }

    public void setCurrentStepCode(String currentStepCode) {
        this.currentStepCode = currentStepCode;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getScheduleServer() {
        return scheduleServer;
    }

    public void setScheduleServer(String scheduleServer) {
        this.scheduleServer = scheduleServer;
    }

    public Byte getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Byte isFinished) {
        this.isFinished = isFinished;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TaskLog other = (TaskLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getCommandId() == null ? other.getCommandId() == null : this.getCommandId().equals(other.getCommandId()))
            && (this.getGroupCode() == null ? other.getGroupCode() == null : this.getGroupCode().equals(other.getGroupCode()))
            && (this.getTotal() == null ? other.getTotal() == null : this.getTotal().equals(other.getTotal()))
            && (this.getSuccess() == null ? other.getSuccess() == null : this.getSuccess().equals(other.getSuccess()))
            && (this.getFail() == null ? other.getFail() == null : this.getFail().equals(other.getFail()))
            && (this.getTraceId() == null ? other.getTraceId() == null : this.getTraceId().equals(other.getTraceId()))
            && (this.getCurrentStepCode() == null ? other.getCurrentStepCode() == null : this.getCurrentStepCode().equals(other.getCurrentStepCode()))
            && (this.getParentTaskId() == null ? other.getParentTaskId() == null : this.getParentTaskId().equals(other.getParentTaskId()))
            && (this.getScheduleServer() == null ? other.getScheduleServer() == null : this.getScheduleServer().equals(other.getScheduleServer()))
            && (this.getIsFinished() == null ? other.getIsFinished() == null : this.getIsFinished().equals(other.getIsFinished()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getCommandId() == null) ? 0 : getCommandId().hashCode());
        result = prime * result + ((getGroupCode() == null) ? 0 : getGroupCode().hashCode());
        result = prime * result + ((getTotal() == null) ? 0 : getTotal().hashCode());
        result = prime * result + ((getSuccess() == null) ? 0 : getSuccess().hashCode());
        result = prime * result + ((getFail() == null) ? 0 : getFail().hashCode());
        result = prime * result + ((getTraceId() == null) ? 0 : getTraceId().hashCode());
        result = prime * result + ((getCurrentStepCode() == null) ? 0 : getCurrentStepCode().hashCode());
        result = prime * result + ((getParentTaskId() == null) ? 0 : getParentTaskId().hashCode());
        result = prime * result + ((getScheduleServer() == null) ? 0 : getScheduleServer().hashCode());
        result = prime * result + ((getIsFinished() == null) ? 0 : getIsFinished().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", commandId=").append(commandId);
        sb.append(", groupCode=").append(groupCode);
        sb.append(", total=").append(total);
        sb.append(", success=").append(success);
        sb.append(", fail=").append(fail);
        sb.append(", traceId=").append(traceId);
        sb.append(", currentStepCode=").append(currentStepCode);
        sb.append(", parentTaskId=").append(parentTaskId);
        sb.append(", scheduleServer=").append(scheduleServer);
        sb.append(", isFinished=").append(isFinished);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}