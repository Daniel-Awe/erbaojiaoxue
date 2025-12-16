package cn.edu.zju.temp.util;

public enum EnumAppCode {
    SUCCESS(0, "成功"),
    UNAUTHORIZED(401, "Token认证失败"),
    ERROR(2002, "操作失败"),
    SYSTEM_ERROR(2001, "系统错误"),
    LOGIN_FAIL(3001, "用户名或密码错误"),
    SAME_TIME_DATA_ERROR(3002, "同时段数据已存在，请勿重复新增"),
    PARAM_FAIL(3003,"参数出错"),
    DATA_NULL(3004,"数据异常"),
    STATUS_FAIL(3005,"状态异常"),
    PASSWORD_FAIL(3006, "密码太弱"),
    USER_NOT_ENABLED(3007, "用户未启用，请联系管理员"),
    IMPORT_FAIL(3008, "数据导入失败"),
    OLDPASSWORD_ERROE(3009, "原密码输入错误"),
    USER_UNAVAILABLE(3010, "用户不可用"),
    MAXUPLOADSIZE_EERROR(3011, "上传文件超过1M，请重新上传合适大小的文件"),
    SAME_MOBILE_ERROR(3012, "重复的手机号"),
    PHONE_NOT_EXIST(3013, "当用户不存在！"),
    ILLEGAL_PHONE_FORMAT(3014, "手机号格式异常！"),
    PHONE_CODE_ERROR(3015, "手机号验证码错误！"),
    FAILE_GET(3016, "当前还在计算"),
    USER_PHONE_ERROR(3016, "当前用户的手机号输入错误！"),
    ANSWER_ERROR(4000,"答题错误");

    private final int code;
    private final String message;

    EnumAppCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}