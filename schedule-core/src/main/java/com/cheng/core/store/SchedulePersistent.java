package com.cheng.core.store;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheng.core.util.ScheduleEncrypt;
import com.cheng.schedule.config.ScheduleServerConfig;
import com.cheng.schedule.config.datasource.DataSourceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulePersistent {

    @Autowired
    private ScheduleServerConfig scheduleServerConfig;

    private DruidDataSource druidDataSource;

    public DruidDataSource buildDataSource() throws Exception{
        DataSourceConfig dataConfig = scheduleServerConfig.getTask().getDataSource();
        druidDataSource = new DruidDataSource();
        if (StringUtils.isNotEmpty(dataConfig.getToken())) {
            String decrypt = ScheduleEncrypt.decrypt(dataConfig.getToken());
            JSONObject jsonObject = JSON.parseObject(decrypt);
            druidDataSource.setUrl(jsonObject.getString("url"));
            druidDataSource.setUsername(jsonObject.getString("user"));
            druidDataSource.setPassword(jsonObject.getString("pwd"));
        } else {
            druidDataSource.setUsername(ScheduleEncrypt.decrypt(dataConfig.getUserName()));
            druidDataSource.setPassword(ScheduleEncrypt.decrypt(dataConfig.getPassword()));
            druidDataSource.setUrl(ScheduleEncrypt.decrypt(dataConfig.getUrl()));
        }

        druidDataSource.setMaxActive(dataConfig.getPollSize());
        druidDataSource.setDefaultAutoCommit(false);
        druidDataSource.init();
        return druidDataSource;
    }

    public DruidDataSource getDruidDataSource(){
        return druidDataSource;
    }
}
