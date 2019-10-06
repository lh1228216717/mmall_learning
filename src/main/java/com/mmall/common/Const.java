package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currenUser";
    public static final String USERNAME= "username";
    public static final String EMAIL="email";
    public interface Rore{
        int RORE_CUSTOMER=0;    //普通用户
        int RORE_ADMIN=1;       //管理员
    }
    public interface Cart{
        public static final int CHECKED =0;
        public static final int UN_CHECKED = 1;
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    }



    public interface productPriceConst{
        Set<String> price_set = Sets.newHashSet("price_desc","price_asc");
    }

    public enum ProductEnum {

        ON_SALE(1,"在线");//参数异常可向下拓展
        private final int code;
        private final String desc;

        ProductEnum(int code,String desc){
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
}
