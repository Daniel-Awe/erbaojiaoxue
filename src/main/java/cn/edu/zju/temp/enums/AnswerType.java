package cn.edu.zju.temp.enums;

public enum AnswerType {

    NON_STANDARD_ASPECT("非标准切面"),
    ASPECT_ONE("切面一"),
    ASPECT_TWO("切面二"),
    ASPECT_THREE("切面三"),
    ASPECT_FOUR("切面四"),
    ASPECT_FIVE("切面五"),
    ASPECT_SEX("切面六"),
    ASPECT_SEVEN("切面七");

    private String msg;

    AnswerType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
