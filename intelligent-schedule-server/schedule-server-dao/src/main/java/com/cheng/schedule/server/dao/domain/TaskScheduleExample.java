package com.cheng.schedule.server.dao.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskScheduleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public TaskScheduleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("task_id is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("task_id is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(Long value) {
            addCriterion("task_id =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(Long value) {
            addCriterion("task_id <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(Long value) {
            addCriterion("task_id >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("task_id >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(Long value) {
            addCriterion("task_id <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("task_id <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(List<Long> values) {
            addCriterion("task_id in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(List<Long> values) {
            addCriterion("task_id not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(Long value1, Long value2) {
            addCriterion("task_id between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("task_id not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andScheduleHostIsNull() {
            addCriterion("schedule_host is null");
            return (Criteria) this;
        }

        public Criteria andScheduleHostIsNotNull() {
            addCriterion("schedule_host is not null");
            return (Criteria) this;
        }

        public Criteria andScheduleHostEqualTo(String value) {
            addCriterion("schedule_host =", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostNotEqualTo(String value) {
            addCriterion("schedule_host <>", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostGreaterThan(String value) {
            addCriterion("schedule_host >", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostGreaterThanOrEqualTo(String value) {
            addCriterion("schedule_host >=", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostLessThan(String value) {
            addCriterion("schedule_host <", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostLessThanOrEqualTo(String value) {
            addCriterion("schedule_host <=", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostLike(String value) {
            addCriterion("schedule_host like", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostNotLike(String value) {
            addCriterion("schedule_host not like", value, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostIn(List<String> values) {
            addCriterion("schedule_host in", values, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostNotIn(List<String> values) {
            addCriterion("schedule_host not in", values, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostBetween(String value1, String value2) {
            addCriterion("schedule_host between", value1, value2, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andScheduleHostNotBetween(String value1, String value2) {
            addCriterion("schedule_host not between", value1, value2, "scheduleHost");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeIsNull() {
            addCriterion("pre_fire_time is null");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeIsNotNull() {
            addCriterion("pre_fire_time is not null");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeEqualTo(Date value) {
            addCriterion("pre_fire_time =", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeNotEqualTo(Date value) {
            addCriterion("pre_fire_time <>", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeGreaterThan(Date value) {
            addCriterion("pre_fire_time >", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("pre_fire_time >=", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeLessThan(Date value) {
            addCriterion("pre_fire_time <", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeLessThanOrEqualTo(Date value) {
            addCriterion("pre_fire_time <=", value, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeIn(List<Date> values) {
            addCriterion("pre_fire_time in", values, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeNotIn(List<Date> values) {
            addCriterion("pre_fire_time not in", values, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeBetween(Date value1, Date value2) {
            addCriterion("pre_fire_time between", value1, value2, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreFireTimeNotBetween(Date value1, Date value2) {
            addCriterion("pre_fire_time not between", value1, value2, "preFireTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeIsNull() {
            addCriterion("pre_use_time is null");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeIsNotNull() {
            addCriterion("pre_use_time is not null");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeEqualTo(Integer value) {
            addCriterion("pre_use_time =", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeNotEqualTo(Integer value) {
            addCriterion("pre_use_time <>", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeGreaterThan(Integer value) {
            addCriterion("pre_use_time >", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pre_use_time >=", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeLessThan(Integer value) {
            addCriterion("pre_use_time <", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeLessThanOrEqualTo(Integer value) {
            addCriterion("pre_use_time <=", value, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeIn(List<Integer> values) {
            addCriterion("pre_use_time in", values, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeNotIn(List<Integer> values) {
            addCriterion("pre_use_time not in", values, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeBetween(Integer value1, Integer value2) {
            addCriterion("pre_use_time between", value1, value2, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andPreUseTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("pre_use_time not between", value1, value2, "preUseTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeIsNull() {
            addCriterion("next_fire_time is null");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeIsNotNull() {
            addCriterion("next_fire_time is not null");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeEqualTo(Date value) {
            addCriterion("next_fire_time =", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeNotEqualTo(Date value) {
            addCriterion("next_fire_time <>", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeGreaterThan(Date value) {
            addCriterion("next_fire_time >", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("next_fire_time >=", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeLessThan(Date value) {
            addCriterion("next_fire_time <", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeLessThanOrEqualTo(Date value) {
            addCriterion("next_fire_time <=", value, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeIn(List<Date> values) {
            addCriterion("next_fire_time in", values, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeNotIn(List<Date> values) {
            addCriterion("next_fire_time not in", values, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeBetween(Date value1, Date value2) {
            addCriterion("next_fire_time between", value1, value2, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andNextFireTimeNotBetween(Date value1, Date value2) {
            addCriterion("next_fire_time not between", value1, value2, "nextFireTime");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdIsNull() {
            addCriterion("running_cmd_id is null");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdIsNotNull() {
            addCriterion("running_cmd_id is not null");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdEqualTo(Long value) {
            addCriterion("running_cmd_id =", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdNotEqualTo(Long value) {
            addCriterion("running_cmd_id <>", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdGreaterThan(Long value) {
            addCriterion("running_cmd_id >", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdGreaterThanOrEqualTo(Long value) {
            addCriterion("running_cmd_id >=", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdLessThan(Long value) {
            addCriterion("running_cmd_id <", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdLessThanOrEqualTo(Long value) {
            addCriterion("running_cmd_id <=", value, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdIn(List<Long> values) {
            addCriterion("running_cmd_id in", values, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdNotIn(List<Long> values) {
            addCriterion("running_cmd_id not in", values, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdBetween(Long value1, Long value2) {
            addCriterion("running_cmd_id between", value1, value2, "runningCmdId");
            return (Criteria) this;
        }

        public Criteria andRunningCmdIdNotBetween(Long value1, Long value2) {
            addCriterion("running_cmd_id not between", value1, value2, "runningCmdId");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}