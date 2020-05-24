package com.cheng.util;

import java.util.UUID;

/**
 * @Description TODO
 * @Author beedoorwei
 * @Date 2019/7/9 6:22
 * @Version 1.0.0
 */
public class UUIDUtils {

    public static final String next() {
        return UUID.randomUUID().toString();
    }
}
