package com.sdust.crawler.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by LiuYuanZhe on 18/5/16.
 */

public class RestFulResult implements Serializable {
    private int status;
    private String message;
    private Object data;

    //新添加字段
    private String note = "";
    private String success = "";
    private Object raw_data = "";

    public RestFulResult(int status, String message, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }


    public RestFulResult() {
    }

    public RestFulResult(String success, int status, String message, Object data)
    {
        this.note = "";
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = "";
        if(data != null)
        {
            JSONObject json = new JSONObject();
            json.put("members", JSON.parse(String.valueOf(data)));
            this.raw_data = json;
        }
    }

    public boolean hasError()
    {
        return status == 0;
    }

    public static RestFulResult success(int status, String message, Object data){
        return new RestFulResult(status, message, data);
    }
    public static RestFulResult success(int status, String message){
        return new RestFulResult(status, message, null);
    }

    public static RestFulResult failure(int status, String message){
        return new RestFulResult(status, message, null);
    }

    public static RestFulResult success(Object data) {
        return new RestFulResult(4, null, data);
    }
    public static RestFulResult success(String message, Object data) {

        return new RestFulResult(4, message, data);
    }



    public static RestFulResult failure(String errorMsg) {
        return new RestFulResult(0, errorMsg,null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 登录时需要验证返回值,status=2
     * @param errorMsg
     * @return
     */
    public static RestFulResult needValidate(String errorMsg)
    {
        return new RestFulResult(2, errorMsg, null);
    }


    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }


    public String getSuccess() {
        return success;
    }


    public void setSuccess(String success) {
        this.success = success;
    }


    public Object getRaw_data() {
        return raw_data;
    }


    public void setRaw_data(Object raw_data) {
        this.raw_data = raw_data;
    }


}
