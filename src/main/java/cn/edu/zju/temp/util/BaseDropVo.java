package cn.edu.zju.temp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseDropVo<T> {
    private Integer id;
    private String name;
    private T data;
}
