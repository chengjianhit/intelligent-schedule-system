package com.cheng.schedule.server.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * task_schedule
 * @author 
 */
public class TaskSchedule implements Serializable {
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 当前调度发起host，目前作为排它锁用，任务调度发起前先更新此字段为本机host，调度完毕后再更新回去
     */
    private String scheduleHost;

    /**
     * 上一次调度发起时间
     */
    private Date preFireTime;

    /**
     * 上一次调度任务执行时间
     */
    private Integer preUseTime;

    /**
     * 下次调度发起时间
     */
    private Date nextFireTime;

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
     * 正在执行的调度ID
     */
    private Long runningCmdId;

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

    public String getScheduleHost() {
        return scheduleHost;
    }

    public void setScheduleHost(String scheduleHost) {
        this.scheduleHost = scheduleHost;
    }

    public Date getPreFireTime() {
        return preFireTime;
    }

    public void setPreFireTime(Date preFireTime) {
        this.preFireTime = preFireTime;
    }

    public Integer getPreUseTime() {
        return preUseTime;
    }

    public void setPreUseTime(Integer preUseTime) {
        this.preUseTime = preUseTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
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

    public Long getRunningCmdId() {
        return runningCmdId;
    }

    public void setRunningCmdId(Long runningCmdId) {
        this.runningCmdId = runningCmdId;
    }
}