package me.k28611.manage.utils;

import lombok.Data;
import me.k28611.manage.enums.ResultCode;

import java.io.Serializable;
@Data
public class JsonResult<T> implements Serializable {
    //Serializable将对象的状态保存在存储媒体中以便可以在以后重新创建出完全相同的副本
    private int code;
    private String message;
    private T data;
    private String extraMessage;



    public JsonResult(ResultCode resultCode, T data) {
        super();
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    public JsonResult(Throwable e){
        this(ResultCode.FAIL,  null);
    }
    public JsonResult(T data) {
        this(ResultCode.SUCCESS,data);
    }

}
