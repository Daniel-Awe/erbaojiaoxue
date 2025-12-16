package cn.edu.zju.temp.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    public Result() {
    }

    private static final long serialVersionUID = -7755597058154875073L;

    private String message;

    private int code;

    private T data;

    public Result(EnumAppCode appCode) {
        if (appCode == null) {
            return;
        }
        this.code = appCode.getCode();
        this.message = appCode.getMessage();
    }

    public Result(EnumAppCode appCode, String message, T data) {
        this.code = appCode.getCode();
        this.message = message;
        this.data = data;
    }

    public Result(EnumAppCode appCode, String message) {
        this.code = appCode.getCode();
        this.message = message;
    }

    public Result(EnumAppCode appCode, T data) {
        this.code = appCode.getCode();
        this.message = appCode.getMessage();
        this.data = data;
    }

    public static class ResultUtil {

        public static <T> Result<T> success() {
            return new Result<T>(EnumAppCode.SUCCESS);
        }

        public static <T> Result<T> success(T obj) {
            return new Result<T>(EnumAppCode.SUCCESS, obj);
        }

        public static <T> Result<T> fail(String message) {
            return new Result<T>(EnumAppCode.ERROR, message);
        }

        public static <T> Result<T> fail(EnumAppCode code, String message) {
            return new Result<T>(code, message);
        }

        public static <T> Result<T> fail(EnumAppCode code) {
            return new Result<T>(code, code.getMessage());
        }
    }

}