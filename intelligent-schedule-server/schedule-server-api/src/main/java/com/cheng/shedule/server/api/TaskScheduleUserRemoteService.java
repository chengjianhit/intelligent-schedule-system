package com.cheng.shedule.server.api;


import com.cheng.shedule.server.common.AiResponse;

/**
 * @Description TODO
 * @Author beedoorwei
 * @Date 2019/7/29 14:16
 * @Version 1.0.0
 */
public interface TaskScheduleUserRemoteService {

    /**
     * if the user is validate user
     * @param userId
     * @return
     */
    public AiResponse<Boolean> isValidateUser(String userId);

    /**
     * decide if the user has permission to  operation
     * @param userId
     * @param opertionCode
     * @return
     */
    public AiResponse<Boolean> hasPermission(String userId, String opertionCode);
}
