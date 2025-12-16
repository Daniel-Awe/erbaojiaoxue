package cn.edu.zju.temp.entity.uo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: gsq
 * @description: 病例图导入题库
 * @date: 2024/5/16 11:01
 * @version: 1.0
 */
@Data
public class PatientBankUo {

    private Long patientAnalyseId;

    private String name;

    private String answer;
}
