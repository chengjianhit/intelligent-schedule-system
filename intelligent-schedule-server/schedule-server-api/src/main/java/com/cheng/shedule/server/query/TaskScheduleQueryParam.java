package com.cheng.shedule.server.query;

import lombok.Data;

import java.util.Date;


@Data
public class TaskScheduleQueryParam extends PageQuery {
	String taskId;
	Date scheduleStartTime;
	Date scheduleEndTime;
	Long groupId;
}
