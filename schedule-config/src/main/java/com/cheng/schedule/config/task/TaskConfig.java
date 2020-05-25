package com.cheng.schedule.config.task;

import com.cheng.schedule.config.datasource.DataSourceConfig;
import lombok.Data;

import java.util.List;


@Data
public class TaskConfig {
	List<String> groupList;
	int processThread = 10;
	DataSourceConfig dataSource;
}
