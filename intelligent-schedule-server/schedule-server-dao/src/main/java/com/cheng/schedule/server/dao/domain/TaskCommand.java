package com.cheng.schedule.server.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * task_command
 * @author 
 */
public class TaskCommand implements Serializable {
    private Long id;

    /**
     * 待下发指令，调度、暂停、恢复、优雅停止
     */
    private String command;

    /**
     * 发起指令下发HOST
     */
    private String publishHost;

    /**
     * 待下发时间
     */
    private Date publishTime;

    /**
     * 发起调度IP
     */
    private String scheduleHost;

    /**
     * 任务执行器
     */
    private String processor;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 调度ID
     */
    private Long scheduleId;

    /**
     * 状态,待下发，已取消、下发中、执行中、执行完成、执行失败
     */
    private String state;

    /**
     * 下发目标HOST
     */
    private String targetHost;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 任务优先级(1-10)
     */
    private Byte priority;

    /**
     * 是否允许并发执行
     */
    private Byte parallel;

    /**
     * 原引用的commandId，任务终止，暂停，恢复需要指向原任务
     */
    private Long refCommandId;

    /**
     * 下发指令上下文
     */
    private String context;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPublishHost() {
        return publishHost;
    }

    public void setPublishHost(String publishHost) {
        this.publishHost = publishHost;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getScheduleHost() {
        return scheduleHost;
    }

    public void setScheduleHost(String scheduleHost) {
        this.scheduleHost = scheduleHost;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Long getRefCommandId() {
        return refCommandId;
    }

    public void setRefCommandId(Long refCommandId) {
        this.refCommandId = refCommandId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}