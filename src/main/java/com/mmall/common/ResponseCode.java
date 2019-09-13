package com.mmall.common;

/**
 * 常量 ENUM
 *  0 表示 success
 *  100-199  代码error
 *  200-299 参数异常
 *  300-399 逻辑异常
 */
public enum  ResponseCode {
    SUCCESSS(0,"SUCCESS"),
        ERROR(100,"ERROR"),                                 //代码异常 可向下拓展
            NEED_LOGIN(300,"NEED_LOGIN"),                  //逻辑异常 可向下拓展
                ILLEGAL_ARGUMENT(200,"ILLEGAL_ARGUMENT");//参数异常可向下拓展
    private final int code;
    private final String desc;

    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
