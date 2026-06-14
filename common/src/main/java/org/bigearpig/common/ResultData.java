package org.bigearpig.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code = 20000;
    private boolean apiFlag = true;
    private String message = "请求成功";
    private T result = null;
    private long timestamp = System.currentTimeMillis();


    public static <T> ResultData<T> setObj(T data) {
        ResultData<T> r = new ResultData<>();
        r.result = data;
        return r;
    }


    public static ResultData<String> setErr(String mes){
        ResultData<String> r = new ResultData<>();
        r.code = 20000;
        r.apiFlag = false;
        r.message = mes;
        return r;
    }
}