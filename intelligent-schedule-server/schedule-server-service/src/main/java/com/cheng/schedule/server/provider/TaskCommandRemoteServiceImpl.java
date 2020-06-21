package com.cheng.schedule.server.provider;

import com.cheng.shedule.server.api.TaskCommandRemoteService;
import com.cheng.shedule.server.common.AiResponse;
import org.apache.dubbo.config.annotation.Service;


/**
 * @author jiancheng
 */
@Service(version = "${dubbo.service.version}", group = "${dubbo.service.group}")
public class TaskCommandRemoteServiceImpl implements TaskCommandRemoteService {
    @Override
    public AiResponse<Boolean> abortCommand(String userId, Long commandId) {
        return null;
    }

    @Override
    public AiResponse<Boolean> copyCommand(String userId, Long commandId) {
        return null;
    }
}
