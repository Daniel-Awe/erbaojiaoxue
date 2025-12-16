package cn.edu.zju.temp.entity.uo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author: gsq
 * @description: 将通过zip包得来的 附带上userId
 * @date: 2024/5/15 8:57
 * @version: 1.0
 */
@Data
@ToString
public class PatientZipUo {

    @Schema(description = "传来的zip病例包")
    @NotNull
    private MultipartFile file;

    @Schema(description = "传来的用户id")
    @NotNull
    private Long userId;

}
