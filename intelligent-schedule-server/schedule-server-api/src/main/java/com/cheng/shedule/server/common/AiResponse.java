package com.cheng.shedule.server.common;

public class AiResponse<T> {

    private boolean success;
    private String code;
    private String msg;
    private T data;

    public AiResponse() {
    }

    public static <T> AiResponse<T> emptyFail() {
        AiResponse aiResponse = new AiResponse();
        aiResponse.success = false;
        aiResponse.code = ResponseCode.FAIL.code;
        return aiResponse;
    }

    public static <T> AiResponse<T> emptySucess() {
        AiResponse aiResponse = new AiResponse();
        aiResponse.code = ResponseCode.SUCCESS.code;
        aiResponse.success = true;
        return aiResponse;
    }

    public AiResponse<T> withCode(String code) {
        this.code = code;
        return this;
    }

    public AiResponse<T> withBusinessData(T data) {
        this.data = data;
        return this;
    }

    public AiResponse<T> withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public static <T> AiResponse<T> success(String code, String msg, T data) {
        return success(data).withCode(code).withBusinessData(data).withMsg(msg);
    }

    public static <T> AiResponse<T> success(String msg, T data) {
        return success(data).withMsg(msg);
    }

    public static <T> AiResponse<T> success(T data) {
        AiResponse<T> objectAiResponse = emptySucess();
        objectAiResponse.withBusinessData(data);
        return objectAiResponse;
    }

    public static <T> AiResponse<T> fail(String msg) {
        AiResponse<T> objectAiResponse = emptyFail();
        objectAiResponse.withMsg(msg);
        return objectAiResponse;
    }

    public static <T> AiResponse<T> fail(String code, String msg) {
        AiResponse<T> fail = fail(msg);
        fail.withCode(code);
        return fail;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }
}
