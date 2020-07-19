package com.cheng.schedule.server.entity;

import lombok.Data;

import java.util.List;


@Data
public class PageInfo<T> {
    private Integer pageSize;

    private Long pageNum;

    private Long totalPage;

    private List<T> dataList;

    public static PageInfo instance(Integer pageSize,Long pageNum,Long totalPage)
    {
        PageInfo pageInfo = new PageInfo();
        pageInfo.pageNum=pageNum;
        pageInfo.pageSize= pageSize;
        pageInfo.totalPage=totalPage;
        return pageInfo;
    }
}
