package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.entity.Objection;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.mapper.PatientAnalyseMapper;
import cn.edu.zju.temp.service.IObjectionService;
import cn.edu.zju.temp.service.IPatientAnalyseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/7 15:56
 * @version: 1.0
 */
@Service
public class PatientAnalyseServiceImpl extends ServiceImpl<PatientAnalyseMapper, PatientAnalyse> implements IPatientAnalyseService {

    @Autowired
    private IObjectionService objectionService;

    @Override
    public void delByPatientId(Long id) {
        QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>()
                .eq("patient_id", id);

        List<PatientAnalyse> patientAnalyses = list(wrapper);
        if (patientAnalyses == null || patientAnalyses.size() == 0){
        }else {
            for (PatientAnalyse patientAnalyse:patientAnalyses){
// 删除分析图
                removeById(patientAnalyse.getId());
//  删除对应异议
                QueryWrapper<Objection> queryWrapper = new QueryWrapper<Objection>()
                        .eq("patient_analyse_id", patientAnalyse.getId());
                List<Objection> objectionList = objectionService.list(queryWrapper);
                if (objectionList == null || objectionList.size() == 0){
                }else {
                    for (Objection objection:objectionList){
                        objectionService.removeById(objection.getId());
                    }
                }

            }
        }

    }

    @Override
    public void delMoreByids(Long[] ids) {
        for (Long patientId:ids){
            QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>()
                    .eq("patient_id", patientId);

            List<PatientAnalyse> patientAnalyses = list(wrapper);
            if (patientAnalyses == null || patientAnalyses.size() == 0){
            }else {
                for (PatientAnalyse patientAnalyse:patientAnalyses){
//  删除对应病理分析图
                    removeById(patientAnalyse.getId());
//  删除对应病例异议
                    QueryWrapper<Objection> queryWrapper = new QueryWrapper<Objection>()
                            .eq("patient_analyse_id", patientAnalyse.getId());
                    List<Objection> objectionList = objectionService.list(queryWrapper);
                    if (objectionList == null || objectionList.size() == 0){
                    }else {
                        for (Objection objection:objectionList){
                            objectionService.removeById(objection.getId());
                        }
                    }
                }
            }
        }
    }
}
