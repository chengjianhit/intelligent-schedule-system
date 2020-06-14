package com.cheng.shedule.server.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 2337040907668297137L;
    private Long pageNum = 1L;
    private Integer pageSize = 10;
}
