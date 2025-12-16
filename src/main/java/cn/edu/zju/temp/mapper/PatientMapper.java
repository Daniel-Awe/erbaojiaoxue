package cn.edu.zju.temp.mapper;

import cn.edu.zju.temp.entity.Patient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病人
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {

}
