package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.PatientAnalyse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IPatientAnalyseService extends IService<PatientAnalyse> {
    void delByPatientId(Long id);

    void delMoreByids(Long[] ids);
}
