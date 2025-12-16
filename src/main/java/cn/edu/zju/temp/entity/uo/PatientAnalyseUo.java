package cn.edu.zju.temp.entity.uo;

import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/23 13:23
 * @version: 1.0
 */
@Data
public class PatientAnalyseUo {
    private Integer userId;
    private Long page = 1L;
    private Long limit = 10L;
}
