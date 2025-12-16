package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.entity.so.ImgInfoSo;
import cn.edu.zju.temp.entity.so.PatientSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ImgInfoFrameTo;
import cn.edu.zju.temp.entity.to.PatientAnalyseTo;
import cn.edu.zju.temp.entity.to.PatientTopTo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.PatientAnalyseUo;
import cn.edu.zju.temp.entity.uo.PatientZipUo;
import cn.edu.zju.temp.entity.vo.PatientAnalyseVo;
import cn.edu.zju.temp.entity.vo.PatientVo;
import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.bytedeco.javacv.FrameGrabber;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IPatientService extends IService<Patient> {

    List<Patient> getAllPatirnt();

    Patient addPatientPage(PatientSo patientSo) throws IOException;

    Boolean addByZip(PatientZipUo patientZipUo) throws IOException;

    Boolean deleteOnePatientById(Long id);

    Boolean deleteMoreByIds(Long[] ids);

    Boolean updatePatient(Patient patient);

    Page<Patient> updatePatientInfo(PatientVo patientVo);

    Page<Patient> getAllPatientByPage(Integer page, Integer limit);

    Page<PatientAnalyseTo> getAnalyseImgsById(PatientAnalyseVo patientAnalyseVo,Integer userId);


    Integer getVideoByPath(String videopath) throws IOException;

    Page<Patient> getAllPatientByPageUserId(UserSo userSo);

    PatientTopTo getTopInfo(Integer userId);

    ImgInfoFrameTo getAnalyseByC(String framePath);

    Page<Patient> getUserPatientImgs(PatientAnalyseUo patientAnalyseUo);

    Page<Patient> getAllPatientOrderBy(OrderPatientUo orderPatientUo);

    void delAllPatientByUserId(Long id);

    void delMorePatientIds(Long[] ids);

//    Boolean getVideoByPathOpenCv(String videopath);
}
