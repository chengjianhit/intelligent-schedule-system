CREATE TABLE `group_permission` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`group_id` BIGINT(20) NULL DEFAULT NULL COMMENT '分组id',
	`user_id` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户id' COLLATE 'utf8_general_ci',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户名称' COLLATE 'utf8_general_ci',
	`is_deleted` TINYINT(1) NULL DEFAULT '0' COMMENT '是否删除',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	`is_admin` TINYINT(1) NULL DEFAULT '0' COMMENT '是否管理员',
	PRIMARY KEY (`id`) USING BTREE
)COLLATE='utf8_general_ci' ENGINE=InnoDB AUTO_INCREMENT=265;

CREATE TABLE `operation_log` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`operation_type` VARCHAR(255) NULL DEFAULT NULL COMMENT '操作类型' COLLATE 'utf8_general_ci',
	`operation_id` BIGINT(20) NULL DEFAULT NULL COMMENT '操作Id',
	`operation_user` VARCHAR(255) NULL DEFAULT NULL COMMENT '操作人' COLLATE 'utf8_general_ci',
	`operation_desc` VARCHAR(1024) NULL DEFAULT NULL COMMENT '操作描述' COLLATE 'utf8_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=6107
;

CREATE TABLE `schedule_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户ID' COLLATE 'utf8_general_ci',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户名称' COLLATE 'utf8_general_ci',
	`is_deleted` TINYINT(1) NULL DEFAULT '0' COMMENT '是否删除',
	`user_flag` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户标记,NORMAL,ADMIN' COLLATE 'utf8_general_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=14
;


CREATE TABLE `task_command` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`command` VARCHAR(96) NOT NULL COMMENT '待下发指令，调度、暂停、恢复、优雅停止' COLLATE 'utf8_general_ci',
	`publish_host` VARCHAR(255) NULL DEFAULT NULL COMMENT '发起指令下发HOST' COLLATE 'utf8_general_ci',
	`publish_time` DATETIME NOT NULL COMMENT '待下发时间',
	`schedule_host` VARCHAR(255) NULL DEFAULT NULL COMMENT '发起调度IP' COLLATE 'utf8_general_ci',
	`processor` VARCHAR(256) NOT NULL COMMENT '任务执行器' COLLATE 'utf8_general_ci',
	`context` TEXT(65535) NULL DEFAULT NULL COMMENT '下发指令上下文' COLLATE 'utf8_general_ci',
	`group_id` BIGINT(20) NOT NULL COMMENT '分组ID',
	`task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
	`schedule_id` BIGINT(20) NOT NULL COMMENT '调度ID',
	`state` VARCHAR(255) NOT NULL COMMENT '状态,待下发，已取消、下发中、执行中、执行完成、执行失败' COLLATE 'utf8_general_ci',
	`target_host` VARCHAR(4096) NULL DEFAULT NULL COMMENT '下发目标HOST' COLLATE 'utf8_general_ci',
	`is_deleted` TINYINT(1) NULL DEFAULT '0' COMMENT '是否删除',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	`priority` TINYINT(4) NOT NULL DEFAULT '5' COMMENT '任务优先级(1-10)',
	`parallel` TINYINT(4) NULL DEFAULT '0' COMMENT '是否允许并发执行',
	`ref_command_id` BIGINT(20) NULL DEFAULT NULL COMMENT '原引用的commandId，任务终止，暂停，恢复需要指向原任务',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=558937
;




CREATE TABLE `task_group` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`group_code` VARCHAR(96) NOT NULL COMMENT '分组编码(数字)' COLLATE 'utf8_general_ci',
	`group_name` VARCHAR(255) NOT NULL COMMENT '分组名称' COLLATE 'utf8_general_ci',
	`desc` VARCHAR(512) NULL DEFAULT NULL COMMENT '描述' COLLATE 'utf8_general_ci',
	`app_info` VARCHAR(1024) NOT NULL COMMENT '归属应用信息{appId:xxx,appName:xxx}' COLLATE 'utf8_general_ci',
	`is_deleted` TINYINT(1) NULL DEFAULT '0' COMMENT '是否删除',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `group_idx_uniq` (`group_code`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=78
;

CREATE TABLE `task_log` (
	`id` BIGINT(20) NOT NULL DEFAULT '0',
	`task_id` BIGINT(20) NULL DEFAULT NULL COMMENT '任务配置ID',
	`command_id` BIGINT(20) NULL DEFAULT NULL COMMENT '指令ID',
	`group_code` VARCHAR(255) NULL DEFAULT NULL COMMENT '任务分组号' COLLATE 'utf8_general_ci',
	`fetch_finish` TINYINT(1) NULL DEFAULT '0' COMMENT '取数是否结束',
	`total` BIGINT(20) NULL DEFAULT '0',
	`success` BIGINT(20) NULL DEFAULT '0',
	`fail` BIGINT(20) NULL DEFAULT '0',
	`trace_id` VARCHAR(128) NULL DEFAULT NULL COMMENT '任务全局追踪ID' COLLATE 'utf8_general_ci',
	`current_step_code` VARCHAR(255) NULL DEFAULT NULL COMMENT '当前步骤编码（预留给多级任务使用）' COLLATE 'utf8_general_ci',
	`parent_task_id` BIGINT(20) NULL DEFAULT NULL COMMENT '如果是多级任务，表示父任务ID（预留给多级任务使用）',
	`schedule_server` VARCHAR(256) NULL DEFAULT NULL COMMENT '调度中心ip' COLLATE 'utf8_general_ci',
	`is_finished` TINYINT(4) NULL DEFAULT '0' COMMENT '当前步骤是否结束1是，0否',
	`start_time` DATETIME NULL DEFAULT NULL COMMENT '任务开始时间',
	`end_time` DATETIME NULL DEFAULT NULL COMMENT '结束时间',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `task_schedule` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
	`schedule_host` VARCHAR(512) NULL DEFAULT NULL COMMENT '当前调度发起host，目前作为排它锁用，任务调度发起前先更新此字段为本机host，调度完毕后再更新回去' COLLATE 'utf8_general_ci',
	`pre_fire_time` DATETIME NULL DEFAULT NULL COMMENT '上一次调度发起时间',
	`pre_use_time` INT(11) NULL DEFAULT NULL COMMENT '上一次调度任务执行时间',
	`next_fire_time` DATETIME NULL DEFAULT NULL COMMENT '下次调度发起时间',
	`is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	`running_cmd_id` BIGINT(20) NULL DEFAULT '0' COMMENT '正在执行的调度ID',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=650
;


#console页面录入
DROP table IF EXISTS `task_config`
CREATE TABLE `task_config`
(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`group_id` BIGINT(20) NOT NULL COMMENT '任务所属分组ID',
	`task_name` VARCHAR(1024) NOT NULL COMMENT '任务名称' COLLATE 'utf8_general_ci',
	`task_context` VARCHAR(4000) NULL DEFAULT NULL COMMENT '任务上下文' COLLATE 'utf8_general_ci',
	`task_state` VARCHAR(96) NOT NULL DEFAULT 'NORMAL' COMMENT '任务状态' COLLATE 'utf8_general_ci',
	`processor` VARCHAR(256) NOT NULL COMMENT '任务执行器' COLLATE 'utf8_general_ci',
	`schedule_type` VARCHAR(96) NOT NULL DEFAULT 'CRON' COMMENT '调度时间类型' COLLATE 'utf8_general_ci',
	`time_zone` VARCHAR(96) NULL DEFAULT 'Asia/Shanghai' COMMENT '调度时区' COLLATE 'utf8_general_ci',
	`schedule_express` VARCHAR(512) NOT NULL COMMENT '任务调度表达式' COLLATE 'utf8_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	`is_deleted` TINYINT(1) NULL DEFAULT '0' COMMENT '是否删除',
	`priority` TINYINT(4) NOT NULL DEFAULT '5' COMMENT '任务优先级(1-10)',
	`parallel` TINYINT(4) NULL DEFAULT '0' COMMENT '是否允许并发执行',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='调度任务配置表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
ROW_FORMAT=DYNAMIC
AUTO_INCREMENT=10662
;