package com.ityouzi.core.core.domain;

import java.util.HashMap;
import java.util.Map;

public class ResultMsg extends HashMap<String, Object> {

    //
    private static final long serialVersionUID = -8157613083634272196L;

    public ResultMsg() {
        put("code", 0);
        put("msg", "success");
    }

    public static ResultMsg error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultMsg error(String msg) {
        return error(500, msg);
    }

    public static ResultMsg error(int code, String msg) {
        ResultMsg r = new ResultMsg();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ResultMsg ok(String msg) {
        ResultMsg r = new ResultMsg();
        r.put("msg", msg);
        return r;
    }

    public static ResultMsg data(Object obj) {
        ResultMsg r = new ResultMsg();
        r.put("data", obj);
        return r;
    }

    public static ResultMsg ok(Map<String, Object> map) {
        ResultMsg r = new ResultMsg();
        r.putAll(map);
        return r;
    }

    public static ResultMsg ok() {
        return new ResultMsg();
    }

    @Override
    public ResultMsg put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}
