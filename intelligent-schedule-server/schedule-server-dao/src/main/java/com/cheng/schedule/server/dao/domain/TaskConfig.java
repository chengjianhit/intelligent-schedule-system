package com.cheng.schedule.server.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * task_config
 * @author 
 */
public class TaskConfig implements Serializable {
    private Long id;

    /**
     * 任务所属分组ID
     */
    private Long groupId;

    /**
     * 任务编码
     */
    private String taskName;

    /**
     * 任务上下文
     */
    private String taskContext;

    /**
     * 任务状态
     */
    private String taskState;

    /**
     * 任务执行器
     */
    private String processor;

    /**
     * 调度时间类型
     */
    private String scheduleType;

    /**
     * 调度时区
     */
    private String timeZone;

    /**
     * 任务调度表达式
     */
    private String scheduleExpress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 任务优先级(1-10)
     */
    private Byte priority;

    /**
     * 是否允许并发执行
     */
    private Byte parallel;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(String taskContext) {
        this.taskContext = taskContext;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getScheduleExpress() {
        return scheduleExpress;
    }

    public void setScheduleExpress(String scheduleExpress) {
        this.scheduleExpress = scheduleExpress;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getPriority() {
        return priority;
    }

    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    public Byte getParallel() {
        return parallel;
    }

    public void setParallel(Byte parallel) {
        this.parallel = parallel;
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
        TaskConfig other = (TaskConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getTaskName() == null ? other.getTaskName() == null : this.getTaskName().equals(other.getTaskName()))
            && (this.getTaskContext() == null ? other.getTaskContext() == null : this.getTaskContext().equals(other.getTaskContext()))
            && (this.getTaskState() == null ? other.getTaskState() == null : this.getTaskState().equals(other.getTaskState()))
            && (this.getProcessor() == null ? other.getProcessor() == null : this.getProcessor().equals(other.getProcessor()))
            && (this.getScheduleType() == null ? other.getScheduleType() == null : this.getScheduleType().equals(other.getScheduleType()))
            && (this.getTimeZone() == null ? other.getTimeZone() == null : this.getTimeZone().equals(other.getTimeZone()))
            && (this.getScheduleExpress() == null ? other.getScheduleExpress() == null : this.getScheduleExpress().equals(other.getScheduleExpress()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getParallel() == null ? other.getParallel() == null : this.getParallel().equals(other.getParallel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getTaskName() == null) ? 0 : getTaskName().hashCode());
        result = prime * result + ((getTaskContext() == null) ? 0 : getTaskContext().hashCode());
        result = prime * result + ((getTaskState() == null) ? 0 : getTaskState().hashCode());
        result = prime * result + ((getProcessor() == null) ? 0 : getProcessor().hashCode());
        result = prime * result + ((getScheduleType() == null) ? 0 : getScheduleType().hashCode());
        result = prime * result + ((getTimeZone() == null) ? 0 : getTimeZone().hashCode());
        result = prime * result + ((getScheduleExpress() == null) ? 0 : getScheduleExpress().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getParallel() == null) ? 0 : getParallel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", taskName=").append(taskName);
        sb.append(", taskContext=").append(taskContext);
        sb.append(", taskState=").append(taskState);
        sb.append(", processor=").append(processor);
        sb.append(", scheduleType=").append(scheduleType);
        sb.append(", timeZone=").append(timeZone);
        sb.append(", scheduleExpress=").append(scheduleExpress);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", priority=").append(priority);
        sb.append(", parallel=").append(parallel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}