package com.cheng.shedule.server.common;

import java.io.Serializable;
import java.util.List;

public class AiPageResponse<T> implements Serializable {
    private static final long serialVersionUID = -1769822327349805825L;
    private boolean success;
    private String code;
    private String msg;
    private List<T> data;
    private Integer pageSize;
    private Long currPage;
    private Long totalNum;

    public AiPageResponse() {
    }

    public AiPageResponse<T> withPageInfo(Long totalNum, Long currPage, Integer pageSize) {
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.currPage = currPage;
        return this;
    }

    public static <T> AiPageResponse<T> emptyFail() {
        AiPageResponse aiResponse = new AiPageResponse();
        aiResponse.success = false;
        aiResponse.code = ResponseCode.FAIL.code;
        return aiResponse;
    }

    public static <T> AiPageResponse<T> emptySucess() {
        AiPageResponse aiResponse = new AiPageResponse();
        aiResponse.code = ResponseCode.SUCCESS.code;
        aiResponse.success = true;
        return aiResponse;
    }

    public AiPageResponse<T> withCode(String code) {
        this.code = code;
        return this;
    }

    public AiPageResponse<T> withBusinessData(List<T> data) {
        this.data = data;
        return this;
    }

    public AiPageResponse<T> withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public static <T> AiPageResponse<T> success(String code, String msg, List<T> data) {
        return success(data).withCode(code).withBusinessData(data).withMsg(msg);
    }

    public static <T> AiPageResponse<T> success(String msg, List<T> data) {
        return success(data).withMsg(msg);
    }

    public static <T> AiPageResponse<T> success(List<T> data) {
        AiPageResponse<T> response = emptySucess();
        response.withBusinessData(data);
        return response;
    }

    public static <T> AiPageResponse<T> fail(String code, String msg) {
        AiPageResponse<T> fail = fail(msg);
        fail.withCode(code);
        return fail;
    }

    public static <T> AiPageResponse<T> fail(String msg) {
        AiPageResponse<T> response = emptyFail();
        response.withMsg(msg);
        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public List<T> getData() {
        return this.data;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Long getCurrPage() {
        return this.currPage;
    }

    public Long getTotalNum() {
        return this.totalNum;
    }
}
