package com.cheng.schedule.config;

import com.cheng.schedule.config.register.RegisterConfig;
import com.cheng.schedule.config.task.TaskConfig;
import com.cheng.schedule.config.transport.CodecConfig;
import com.cheng.schedule.config.transport.TransportConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



@Data
@ConfigurationProperties(prefix="schedule.server")
public class ScheduleServerConfig {
	CodecConfig codec = new CodecConfig();
	TransportConfig transport = new TransportConfig();
	RegisterConfig register = new RegisterConfig();
	TaskConfig task = new TaskConfig();
}
