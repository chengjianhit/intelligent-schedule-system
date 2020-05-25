package com.cheng.schedule.config.datasource;

import lombok.Data;


@Data
public class DataSourceConfig implements java.io.Serializable {
    String url;
    String userName;
    String password;
    String token;
    int pollSize=5;
}
