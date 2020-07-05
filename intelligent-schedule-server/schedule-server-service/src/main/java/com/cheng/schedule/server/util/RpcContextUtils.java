package com.cheng.schedule.server.util;

import org.apache.dubbo.rpc.RpcContext;

/**
 * @Description TODO
 * @Author beedoorwei
 * @Date 2019/6/20 6:41
 * @Version 1.0.0
 */
public class RpcContextUtils {

    /**
     * 根据上下文获取用户登录信息
     * @return
     */
    public static String getUserId()
    {
        RpcContext context = RpcContext.getContext();
        String attachment = context.getAttachment("#)_userId");
        return attachment;
    }

}

