package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg= msg;
    }
    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg= msg;
        this.data = data;
    }
    @JsonIgnore
    //序列化之后不会出现在结果中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESSS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static  <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESSS.getCode(),ResponseCode.SUCCESSS.getDesc());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<>(ResponseCode.SUCCESSS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T t){
        return new ServerResponse<>(ResponseCode.SUCCESSS.getCode(),t);
    }
    public static <T> ServerResponse<T> createBySuccess(String message, T t){
        return new ServerResponse<>(ResponseCode.SUCCESSS.getCode(),message,t);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createByErrorMessage(int code, String msg){
        return new ServerResponse<T>(code,msg);
    }
    public static <T> ServerResponse<T> createByError(T t){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),t);
    }
    public static <T> ServerResponse<T> createByError(String msg, T t){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg,t);
    }
}
