package cn.edu.zju.temp.entity.so;

import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/20 9:10
 * @version: 1.0
 */
@Data
public class UserSo {


    private Integer userId;
    private Long page = 1L;
    private Long limit = 10L;
}
