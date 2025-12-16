package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.config.LocalEnv;
import cn.edu.zju.temp.entity.Objection;
import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.entity.so.ImgInfoSo;
import cn.edu.zju.temp.entity.so.PatientSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ImgInfoFrameTo;
import cn.edu.zju.temp.entity.to.PatientAnalyseTo;
import cn.edu.zju.temp.entity.to.PatientTo;
import cn.edu.zju.temp.entity.to.PatientTopTo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.PatientAnalyseUo;
import cn.edu.zju.temp.entity.uo.PatientUo;
import cn.edu.zju.temp.entity.uo.PatientZipUo;
import cn.edu.zju.temp.entity.vo.PatientAnalyseVo;
import cn.edu.zju.temp.entity.vo.PatientVo;
import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.enums.SexType;
import cn.edu.zju.temp.mapper.PatientAnalyseMapper;
import cn.edu.zju.temp.mapper.PatientMapper;
import cn.edu.zju.temp.service.IObjectionService;
import cn.edu.zju.temp.service.IPatientService;
import cn.edu.zju.temp.service.IUserService;
import cn.edu.zju.temp.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: gsq
 * @description: 练习服务层实现类
 * @date: 2024/5/7 8:59
 * @version: 1.0
 */
@Slf4j
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private PatientAnalyseServiceImpl patientAnalyseService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private IUserService userService;

//    @Value("${zip.path}")
//    private String zipPath;
//
//    @Value("${unzip.path}")
//    private String unzipPath;
//
//    @Value("${img.path}")
//    private String imgPath;
//
//    @Value("${video.frame.path}")
//    private String videoFramePath;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private PatientAnalyseMapper patientAnalyseMapper;


    @Autowired
    private LocalEnv localEnv;

    @Override
    public List<Patient> getAllPatirnt() {
        return list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Patient addPatientPage(PatientSo patientSo) throws IOException {
        if (patientSo == null){
            throw new RuntimeException("上传的patientSo为空");
        }
//  通过新增页面 添加新的病人/可添加多张图片(病例图)/视频  并将添加成功的病人返回
        Patient patient = addPatientByPage(patientSo);
        if (patient != null){
//  将用户的上传病例数+1
            Integer userId = patientSo.getUserId();
            User user = userService.getById(userId);
            if (user.getNumUploadCases() == null){
                user.setNumUploadCases(0);
            }
            Integer num = user.getNumUploadCases();
            user.setNumUploadCases(++num);
            userService.updateById(user);
        }

//  设置病例切面正确率
        double aspectTrue = 0.0;
        double aspectSum = 0.0;
        double rateNum = 0.0;

        QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>()
                .eq("patient_id", patient.getId());
        List<PatientAnalyse> list = patientAnalyseService.list(wrapper);
        for (PatientAnalyse patientAnalyse:list){
            if (patientAnalyse.getIsStandard()){
                aspectTrue++;
                aspectSum++;
            }else{
                aspectSum++;
            }
        }

        BigDecimal correctUserBigDecimal = new BigDecimal(aspectTrue);
        BigDecimal answerUserBigDecimal = new BigDecimal(aspectSum);

        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
            BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

            rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        patient.setAspectCorrect(rateNum + "%");
        updatePatient(patient);

//设置用户切面准确率
        Integer userId = patientSo.getUserId();
        User user = userService.getById(userId);


//  设置用户切面正确率
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<Patient>()
                .eq("user_id", userId);
        List<Patient> patientList = list(queryWrapper);
        if (patientList != null) {
            double totalCorrect = 0.0;
            double numPatient = 0.0;
            for (Patient patientDouble : patientList) {
                if (patientDouble.getAspectCorrect() != null && !patientDouble.getAspectCorrect().equals("")) {
                    double oneAspectCorrect = Double.parseDouble(patientDouble.getAspectCorrect().replace("%", "0"));
                    totalCorrect += oneAspectCorrect;
                    numPatient++;
                }else {
                    numPatient ++;
                }
            }
            BigDecimal correctUserBigDecimalt = new BigDecimal(totalCorrect);
            BigDecimal answerUserBigDecimalt = new BigDecimal(numPatient);

            if (answerUserBigDecimalt.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimalt = correctUserBigDecimalt.divide(answerUserBigDecimalt, 2, RoundingMode.HALF_UP);
                double rateNumt = completeRateNumBigDecimalt.setScale(2, RoundingMode.HALF_UP).doubleValue();
                user.setCorrectRateCase(rateNumt + "%");
                userService.updateById(user);
            }
        }
        return patient;
    }

    private Patient addPatientByPage(PatientSo patientSo) throws IOException {
//  如果传来的不为空，图片也有
        if (patientSo != null && patientSo.getUltrasonicImages() != null && patientSo.getUltrasonicImages().length > 0){
            for (MultipartFile file: patientSo.getUltrasonicImages()) {
                String filename = file.getOriginalFilename();
                String substring = filename.substring(filename.lastIndexOf("."));
                if (!substring.equals(".jpg") && !substring.equals(".png") && !substring.equals(".jpeg")) {
                    throw new RuntimeException("上传的图片格式不正确");
                }
            }
            patientSo.setType(PatientType.IMG);
            Patient patient = new Patient(false);
            BeanUtils.copyProperties(patientSo,patient);
            save(patient);
            Integer patient_id = patient.getId();
            for (MultipartFile file: patientSo.getUltrasonicImages()){

                String filename = file.getOriginalFilename();
                String substring = filename.substring(filename.lastIndexOf("."));
                if (!substring.equals(".jpg") && !substring.equals(".png") && !substring.equals(".jpeg")) {
                    throw new RuntimeException("上传的图片格式不正确");
                }
        //根据算法分析得到答案，和概率
                File fileAnalyse = new File(localEnv.getStaticPath() + localEnv.getImgPath() + UUID.randomUUID().toString().substring(0,8) + ".jpg");

                String absolutePath = fileAnalyse.getAbsolutePath();
                int length = localEnv.getStaticPath().length();
                String realPath = absolutePath.substring(length - 1);
                System.out.println("absolutePath" + "   "+absolutePath);
                System.out.println("realPath"+"   "+realPath);
                try {
                    file.transferTo(fileAnalyse);
                } catch (IOException e) {
                    throw new RuntimeException("文件下载失败，请重新上传");
                }
//                ImgInfoSo imgInfoSo = QuestionAnalyse.getAnswerByfile(fileAnalyse);
//                if (imgInfoSo == null){
//                    log.info("该图片未检测出来");
//                    throw new RuntimeException("算法检车图片失败，请重新尝试");
//                }else {
//                    PatientAnalyse patientAnalyse = new PatientAnalyse(patient_id,realPath,false);
//                    String label = imgInfoSo.getLabel();
//                    List<Double> conf = imgInfoSo.getConf();
//                    double possibility = toGetPossibility(conf,label);
//                    possibility += 0.0;
//                    if (label.equals("非标准切面")){
//                        patientAnalyse.setIsStandard(false);
//                    }else {
//                        patientAnalyse.setIsStandard(true);
//                    }
//                    patientAnalyse.setPossibility(possibility);
//                    patientAnalyse.setAnalyseAnswer(label);
//                    patientAnalyseService.save(patientAnalyse);
//                }
                // 模拟算法返回结果
                String label = "标准切面";  // 或 "非标准切面"
                double possibility = 1.00; // 模拟概率

                PatientAnalyse patientAnalyse = new PatientAnalyse(patient_id, realPath, false);
                if (label.equals("非标准切面")) {
                    patientAnalyse.setIsStandard(false);
                } else {
                    patientAnalyse.setIsStandard(true);
                }
                patientAnalyse.setPossibility(possibility);
                patientAnalyse.setAnalyseAnswer(label);
                patientAnalyseService.save(patientAnalyse);

            }
            return patient;

//  上传的不为空，且有视频
        }else if (patientSo != null && patientSo.getUltrasonicVideo() != null && !patientSo.getUltrasonicVideo().isEmpty()){
            MultipartFile file = patientSo.getUltrasonicVideo();
            String filename = file.getOriginalFilename();
            String substring = filename.substring(filename.lastIndexOf("."));
            if (!substring.equals(".mp4") && !substring.equals("wmv") && !substring.equals("asf")){
                throw new RuntimeException("上传的视频格式不正确");
            }
            patientSo.setType(PatientType.VIDEO);

            MultipartFile ultrasonicVideo = patientSo.getUltrasonicVideo();

            String pathss = localEnv.getStaticPath() + localEnv.getVideoPath() + ultrasonicVideo.getOriginalFilename();
            System.out.println(pathss);

            File videoFile = new File(localEnv.getStaticPath() + localEnv.getVideoPath(), ultrasonicVideo.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(videoFile);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();
//            String url = commonUpload.uploadimg(patientSo.getUltrasonicVideo());

            Patient patient = new Patient(localEnv.getVideoPath()+ultrasonicVideo.getOriginalFilename(),false);
            BeanUtils.copyProperties(patientSo,patient);
            save(patient);
            return patient;
//  未上传超声图片或视频是报错
        }else {
            throw new RuntimeException("未上传视频或图片");
        }
    }

    private double toGetPossibility(List<Double> conf,String label) {

        if (label.equals("非标准切面")){
            double possibility = conf.get(0);
            return possibility;
        }else {
            String number = label.substring(2);
            int num = Integer.parseInt(number);
            double possibility = conf.get(num);
            System.out.println(possibility);
            return possibility;
        }

//        double totalCorrect = 0.0;
//        double num = conf.size();
//        for (Double poss:conf) {
//            totalCorrect += poss;
//
//        }
//        BigDecimal correctUserBigDecimalt = new BigDecimal(totalCorrect);
//        BigDecimal answerUserBigDecimalt = new BigDecimal(num);
//
//        if (answerUserBigDecimalt.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
//            BigDecimal completeRateNumBigDecimalt = correctUserBigDecimalt.divide(answerUserBigDecimalt, 2, RoundingMode.HALF_UP);
////                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));
//
//            double rateNumt = completeRateNumBigDecimalt.setScale(2, RoundingMode.HALF_UP).doubleValue();
//            return rateNumt;
//        }
//        return 0.0;



    }


    private Double getchance(ImgInfoSo imgInfoSo) {
        double sum = 0;
        List<Double> conf = imgInfoSo.getConf();
        for (Double chance:conf){
            sum += chance;
        }
        double avg = sum / conf.size();
        return avg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addByZip(PatientZipUo patientZipUo) throws IOException {

        MultipartFile file = patientZipUo.getFile();
        if (file == null || file.getSize() <= 0){
            throw new RuntimeException("上传文件为空");
        }
        String fileName = file.getOriginalFilename();
        if (!"zip".equals(fileName.substring(fileName.lastIndexOf(".")+1))){
            throw new RuntimeException("上传的非zip文件");
        }

//  将zip包的数据导入病人表
        List<Patient> patients = addPatientsByZip(patientZipUo, file);

        if (patients != null && patients.size() > 0){
            Long userId = patientZipUo.getUserId();
            User user = userService.getById(userId);
            if (user.getNumUploadCases() == null){
                user.setNumUploadCases(0);
            }
            Integer ns = user.getNumUploadCases();
            int total = ns + patients.size();
            user.setNumUploadCases(total);
            userService.updateById(user);
        }

//  设置每个病例 的病例切面正确率
        for (Patient patient:patients){
            double aspectTrue = 0.0;
            double aspectSum = 0.0;
            double aspectRate = 0.0;

            QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>()
                    .eq("patient_id", patient.getId());
            List<PatientAnalyse> list = patientAnalyseService.list(wrapper);
            if (list == null){
                continue;
            }
            for (PatientAnalyse patientAnalyse:list){
                if (patientAnalyse.getIsStandard()){
                    aspectTrue++;
                    aspectSum++;
                }else{
                    aspectSum++;
                }
            }

            BigDecimal correctUserBigDecimalone = new BigDecimal(aspectTrue);
            BigDecimal answerUserBigDecimalone = new BigDecimal(aspectSum);

            if (answerUserBigDecimalone.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimalone = correctUserBigDecimalone.divide(answerUserBigDecimalone, 2, RoundingMode.HALF_UP);
                BigDecimal rateNumBigDecimalone = completeRateNumBigDecimalone.multiply(new BigDecimal(100));
                aspectRate = rateNumBigDecimalone.setScale(2, RoundingMode.HALF_UP).doubleValue();
                patient.setAspectCorrect(aspectRate + "%");
            }
            updatePatient(patient);
        }
//设置上传用户的切面正确率
        Long userId = patientZipUo.getUserId();
        User user = userService.getById(userId);

        QueryWrapper<Patient> queryWrapper = new QueryWrapper<Patient>()
                .eq("user_id", userId);
        List<Patient> patientList = list(queryWrapper);
        if (patientList != null) {
            double totalCorrect = 0.0;
            double numPatient = 0.0;
            for (Patient patientDouble : patientList) {
                if (patientDouble.getAspectCorrect() != null && !patientDouble.getAspectCorrect().equals("")) {
                    double oneAspectCorrect = Double.parseDouble(patientDouble.getAspectCorrect().replace("%", "0"));
                    totalCorrect += oneAspectCorrect;
                    numPatient++;
                }else {
                    numPatient ++;
                }
            }
            BigDecimal correctUserBigDecimalt = new BigDecimal(totalCorrect);
            BigDecimal answerUserBigDecimalt = new BigDecimal(numPatient);

            if (answerUserBigDecimalt.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimalt = correctUserBigDecimalt.divide(answerUserBigDecimalt, 2, RoundingMode.HALF_UP);
//                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

                double rateNumt = completeRateNumBigDecimalt.setScale(2, RoundingMode.HALF_UP).doubleValue();
                user.setCorrectRateCase(rateNumt + "%");
                userService.updateById(user);
            }
        }
        return true;
    }

    private List<Patient> addPatientsByZip(PatientZipUo patientZipUo, MultipartFile file) throws IOException {
//  存放图片的url和存放算法结果
        HashMap<String, String> urlMap = new HashMap<>();
        HashMap<String, ImgInfoSo> suanfaMap = new HashMap<>();

        ArrayList<Patient> patients = new ArrayList<>();
//  将zip文件保存到本地
        ZipEntry zipEntry = null;

        List<PatientUo> questions = null;
        try(InputStream in = file.getInputStream();ZipInputStream zipInputStream = new ZipInputStream(in, Charset.forName("GBK"))) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null){
//  1.将zip文件条目转为 file
                byte[] buffer = new byte[1024];
                File entryFile = new File(localEnv.getStaticPath() + localEnv.getUnzipPath(),UUID.randomUUID().toString().substring(0,8) + delMulu(zipEntry.getName()));

                String absolutePath = entryFile.getAbsolutePath();
                int length = localEnv.getStaticPath().length();
                String realPath = absolutePath.substring(length - 1);
                System.out.println("absolutePath" + "   "+absolutePath);
                System.out.println("realPath"+"   "+realPath);
                FileOutputStream fos = new FileOutputStream(entryFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
//  得到MultiPartfile
                MultipartFile multipartFile = FileToMultiFile.getMultipartFile(entryFile);

//  得到当前文件名  判断是xlsx还是jpg文件
                String filename1 = zipEntry.getName();
                String realFilename = delMulu(filename1);
                String substring = filename1.substring(filename1.lastIndexOf(".")+1);
                if (substring != null && substring.equals("xlsx")){
//  是xlsx文件，就得到实体类集合lists
                    questions = getListByxlsx(multipartFile, patientZipUo.getUserId());
                }else if (substring != null && (substring.equals("jpg") || substring.equals("png") || substring.equals("jpeg"))){
//  是jpg/png/mp4文件，存放起来，最后将lists集合的题目名称附上url地址值
                    ImgInfoSo answerByfile = QuestionAnalyse.getAnswerByfile(entryFile);
                    suanfaMap.put(realFilename,answerByfile);
                    urlMap.put(realFilename,realPath);
                }else if (substring != null && substring.equals("mp4")){
                    String url = aliOSSUtils.upload(multipartFile);
                    urlMap.put(realFilename,url);
                } else {
                    throw new RuntimeException("zip文件格式错误，导入失败");
                }
            }


            for (PatientUo question:questions){

                String ultsImgs = question.getUltsImgs();
                String ultrasonicVideo = question.getUltrasonicVideo();
//  若上传的是图片，且不为空
                if (ultsImgs != null && !ultsImgs.equals("")){

                    Patient patient = new Patient();
                    BeanUtils.copyProperties(question,patient);
                    patientMapper.insert(patient);
                    patients.add(patient);
                    Integer patientId = patient.getId();

                    String[] split = ultsImgs.split(",");
                    for (String img : split) {
                        String url = urlMap.get(img);
                        if (url == null || url.isEmpty()) {
                            throw new RuntimeException("导入失败，图片不存在");
                        }

                        // ✅ 不做算法分析，直接保存图片路径
                        PatientAnalyse patientAnalyse = new PatientAnalyse(patientId, url, false);
                        patientAnalyse.setIsStandard(false); // 默认设为非标准切面
                        patientAnalyse.setAnalyseAnswer("非标准切面");
                        patientAnalyse.setPossibility(1.0);
                        patientAnalyseService.save(patientAnalyse);
                    }
                }else if (ultrasonicVideo != null && !ultrasonicVideo.equals("")){
// 上传的视频 不为空
                    Patient patient = new Patient();
                    BeanUtils.copyProperties(question,patient);
                    String url = urlMap.get(ultrasonicVideo);
                    patient.setUltrasonicVideo(url);
                    save(patient);
                    patients.add(patient);
                }else {
                    log.info("图片或视频不存在");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("文件解析失败");
        }
        return patients;


//        //  将传来的zip文件保存在磁盘中
//        File file = new File(zipPath + UUID.randomUUID().toString().substring(0,5) + ".zip");
//        try {
//            zipfile.transferTo(file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
////  获取zip文件的目录
//        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
//
//        ZipEntry entry = zipInputStream.getNextEntry();
//        String mulu = "";
//        while(entry != null){
//            String filename = entry.getName();
//            int indexOf = filename.lastIndexOf("/");
//            String fileMulu = filename.substring(0, indexOf);
//            if (!fileMulu.equals(mulu)){
//                mulu = fileMulu;
//                File targetDir = new File(unzipPath + File.separator + mulu);
//                if (!targetDir.exists()) {
//                    try {
//                        FileUtils.forceMkdir(targetDir);
//                    } catch (IOException e) {
//                        log.error("Failed to create directory: {}", targetDir, e);
//                    }
//                }
//            }
//            entry = zipInputStream.getNextEntry();
//        }
//        File originDir = new File(unzipPath);
//        boolean iszip = ZipTools.unzip(file,originDir);
//        return iszip;
    }

    @Override
    public Boolean deleteOnePatientById(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean deleteMoreByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public Boolean updatePatient(Patient patient) {
        return updateById(patient);
    }

    @Override
    public Page<Patient> updatePatientInfo(PatientVo patientVo) {

        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        Page<Patient> patientPage = new Page<>(patientVo.getPage(), patientVo.getLimit());

        if (patientVo.getName() != null && !patientVo.getName().equals("")){
            wrapper.like("name",patientVo.getName());
        }
        if (patientVo.getUltrasonicNumber() != null && !patientVo.getUltrasonicNumber().equals("")){
            wrapper.eq("ultrasonic_number",patientVo.getUltrasonicNumber());
        }
        if (patientVo.getType() != null){
            wrapper.eq("type",patientVo.getType());
        }
        if (patientVo.getPatientId() != null){
            wrapper.eq("id",patientVo.getPatientId());
        }
        if (patientVo.getUserId() != null){
            wrapper.eq("user_id",patientVo.getUserId());
        }

        if (patientVo.getStartTime() != null && patientVo.getEndTime() != null){
            wrapper.between("create_time",patientVo.getStartTime(),patientVo.getEndTime());
        }else if (patientVo.getStartTime() != null && patientVo.getEndTime() == null){
            wrapper.gt("create_time",patientVo.getStartTime());
        }else if (patientVo.getStartTime() == null && patientVo.getEndTime() != null){
            wrapper.lt("create_time",patientVo.getEndTime());
        }else {
        }
        return patientMapper.selectPage(patientPage, wrapper);
    }

    @Override
    public Page<Patient> getAllPatientByPage(Integer page, Integer limit) {
        Page<Patient> page1 = new Page<>(page, limit);
        Page<Patient> patients = patientMapper.selectPage(page1, null);
        return patients;
    }

    @Override
    public Page<PatientAnalyseTo> getAnalyseImgsById(PatientAnalyseVo patientAnalyseVo,Integer userId) {

        QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>().eq("patient_id", patientAnalyseVo.getPatientId());
        List<PatientAnalyse> patientAnalyses = patientAnalyseService.list(wrapper);
        ArrayList<PatientAnalyseTo> patientAnalyseTos = new ArrayList<>();
        for (PatientAnalyse patientAnalyse:patientAnalyses){
            PatientAnalyseTo patientAnalyseTo = new PatientAnalyseTo();
            BeanUtils.copyProperties(patientAnalyse,patientAnalyseTo);
        //查询异议
            Objection objection = objectionService.getOne(Wrappers.lambdaQuery(Objection.class)
                    .eq(Objection::getPatientAnalyseId, patientAnalyse.getId())
                    .eq(Objection::getUserId,userId));
            if (objection == null){
                patientAnalyseTo.setIsObjection(false);
            }else {
                patientAnalyseTo.setIsObjection(true);
        //采纳  驳回
                String reply = "";
                if (objection.getProcessingResult() == null){
                    reply = "未处理";
                }else if (objection.getProcessingResult() == 0){
                    reply = objection.getAnswer() + "%" + "已驳回" + "%" + objection.getProcessingReply();
                }else {
                    reply = objection.getObjectionAnswer() + "%" + "已采纳" + "%" + objection.getProcessingReply();
                }
                patientAnalyseTo.setReply(reply);
            }
            patientAnalyseTos.add(patientAnalyseTo);
        }
        List<PatientAnalyseTo> andPracticeUos = null;
        int start = (int) ((patientAnalyseVo.getPage() - 1) * patientAnalyseVo.getLimit());
        int end = (int) ((patientAnalyseVo.getPage() * patientAnalyseVo.getLimit()));
        if (end > patientAnalyseTos.size()){
            end = patientAnalyseTos.size();
        }
        if (start >= end){
            andPracticeUos = null;
        }else {
            andPracticeUos = patientAnalyseTos.subList(start, end);
        }
        Page<PatientAnalyseTo> page = new Page<>(patientAnalyseVo.getPage(), patientAnalyseVo.getLimit());
        page.setRecords(andPracticeUos);
        page.setTotal(patientAnalyseTos.size());

        return page;
    }

//    @Override
//    public Page<PatientAnalyse> getAllPatientAnalyseImgsById(PatientIdVo patientIdVo) {
//        Page<PatientAnalyse> patientAnalysePage = new Page<>(patientIdVo.getPage(), patientIdVo.getLimit());
//        QueryWrapper<PatientAnalyse> wrapper = new QueryWrapper<PatientAnalyse>()
//                .eq("patient_id", patientIdVo.getPatientId());
//        return patientAnalyseMapper.selectPage(patientAnalysePage, wrapper);
//    }

    @Override
    public Integer getVideoByPath(String videopath){
//  第一种方式，只适用于windos，占用内存较小
//        long start = System.currentTimeMillis();
//        FFmpegFrameGrabber ff = new FFmpe gFrameGrabber(videopath);
//        ff.start();
//        int lenght = ff.getLengthInFrames();
//        int i = 0;
//        int j = 0;
//        Frame f = null;
//        while (i < lenght) {
//            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
//            f = ff.grabFrame();
////            if ((i > 5) && (f.image != null)) {
////                break;
////            }
//            if (f.image == null){
//                System.out.println("第 "+ i +" 帧为空");
//                i++;
//                continue;
//            }
//
//            opencv_core.IplImage img = f.image;
//            int owidth = img.width();
//            int oheight = img.height();
//            // 对截取的帧进行等比例缩放
//            int width = 800;
//            int height = (int) (((double) width / owidth) * oheight);
//            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//            bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
//                    0, 0, null);
//            ImageIO.write(bi, "jpg", new File(+ j + ".jpg"));
//            j++;
//            i++;
//        }
//
//        //ff.flush();
//        ff.stop();
//        System.out.println(System.currentTimeMillis() - start);
//        return true;


//第二中方法 适用于windows和linux，但内存占用较大
        File video = new File(localEnv.getStaticPath() + videopath);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        // 2.创建一个帧-->图片的转换器
        Java2DFrameConverter converter = new Java2DFrameConverter();
        int cnt = 1;
        try {
            ff.start();
            // 3.先知道这个视频一共有多少帧
            int length = ff.getLengthInFrames();
            System.out.println("高度：" + ff.getImageHeight());
            System.out.println("宽度：" + ff.getImageWidth());
            System.out.println("帧率：" + ff.getFrameRate());
            System.out.println("总帧数：" + length);
            System.out.println("==========================================");
            System.out.println("图片抓取中...");

            // 4.读取视频中每一帧图片

            Frame frame;
            while (true) {
                frame = ff.grabFrame();

                if (frame == null) {
                    break;
                }

                if (frame.image == null) {
                    continue;
                }

                // 5.将获取的帧，存储为图片
                BufferedImage image = converter.getBufferedImage(frame);
//                File picFile = new File(localEnv.getVideoFramePath(), cnt + ".jpg");

                File videoFrameFile = new File(localEnv.getStaticPath() + localEnv.getVideoFramePath(),cnt + ".jpg");

                // 6.将图片保存到目标文件中
                ImageIO.write(image, "jpg", videoFrameFile);
//                ImageIO.write(image, "jpg", picFile);

                if (cnt % 100 == 0) {
                    System.out.println("前" + cnt + "张图片抓取完成；");
                }
                cnt++;
            }
            System.out.println("图片抓取完成。");
            ff.stop();
        } catch (IOException e) {
            throw new RuntimeException("视频检测失败，请重新尝试");
        }
        return cnt;
    }

    @Override
    public Page<Patient> getAllPatientByPageUserId(UserSo userSo) {
        Page<Patient> patientPage = new Page<>(userSo.getPage(), userSo.getLimit());
        QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                .eq("user_id", userSo.getUserId());

        return patientMapper.selectPage(patientPage,wrapper);
    }

    @Override
    public PatientTopTo getTopInfo(Integer userId) {
        PatientTopTo patientTopTo = new PatientTopTo();

//  今日新增
        QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                .apply("date(create_time) = date(now())")
                .eq("user_id",userId);

        List<Patient> todaypatients = list(wrapper);
        patientTopTo.setTodayCase(todaypatients.size());

//  病例总数
        QueryWrapper<Patient> patientQueryWrapper = new QueryWrapper<Patient>()
                .eq("user_id", userId);
        int count = count(patientQueryWrapper);
        patientTopTo.setCaseTotal(count);

//  异议总数
        QueryWrapper<Objection> queryWrapper = new QueryWrapper<Objection>()
                .eq("user_id", userId);
        List<Objection> list = objectionService.list(queryWrapper);
        patientTopTo.setObjectionNum(list.size());

//  切面准确率
        QueryWrapper<Patient> UserIdWrapper = new QueryWrapper<Patient>()
                .eq("user_id", userId);
        List<Patient> patientList = list(UserIdWrapper);
        if (patientList != null) {
            double totalCorrect = 0.0;
            double numPatient = 0.0;
            for (Patient patient : patientList) {
                if (patient.getAspectCorrect() != null && !patient.getAspectCorrect().equals("")) {
                    double oneAspectCorrect = Double.parseDouble(patient.getAspectCorrect().replace("%", "0"));
                    totalCorrect += oneAspectCorrect;
                    numPatient++;
                }else {
                    numPatient ++;
                }
            }
            BigDecimal correctUserBigDecimal = new BigDecimal(totalCorrect);
            BigDecimal answerUserBigDecimal = new BigDecimal(numPatient);

            if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
//                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

                double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                patientTopTo.setAspectRate(rateNum + "%");
            }
        }
        return patientTopTo;

    }

    @Override
    public ImgInfoFrameTo getAnalyseByC(String framePath) {
        ImgInfoFrameTo imgInfoFrameTo = new ImgInfoFrameTo();

        File file = new File(localEnv.getStaticPath() +localEnv.getVideoFramePath() + framePath);
        if (file == null){
            throw new RuntimeException("没有该帧");
        }

        ImgInfoSo imgInfoSo = null;
        try {
            imgInfoSo = QuestionAnalyse.getAnswerByfile(file);

            if (imgInfoSo == null || imgInfoSo.getLabel() ==null){
                return null;
            }else if (imgInfoSo.getLabel() != null && !imgInfoSo.getLabel().equals("非标准切面")){
                List<Double> conf = imgInfoSo.getConf();
                String label = imgInfoSo.getLabel();
                double possibility = toGetPossibility(conf, label);
                imgInfoFrameTo.setPossibility(possibility+"");
                imgInfoFrameTo.setLabel(label);
            }else {
                imgInfoFrameTo.setLabel(imgInfoSo.getLabel());
                imgInfoFrameTo.setPossibility("0.0");
            }
        } catch (IOException e) {
            throw new RuntimeException("算法分析帧失败");
        }
        return imgInfoFrameTo;
    }


    @Override
    public Page<Patient> getUserPatientImgs(PatientAnalyseUo patientAnalyseUo) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                .eq("user_id", patientAnalyseUo.getUserId());

        List<Patient> patients = list(wrapper);
        List<Patient> andPracticeUos = null;
        int start = (int) ((patientAnalyseUo.getPage() - 1) * patientAnalyseUo.getLimit());
        int end = (int) ((patientAnalyseUo.getPage() * patientAnalyseUo.getLimit()));
        if (end > patients.size()){
            end = patients.size();
        }

        if (start >= end){
            andPracticeUos = null;
        }else {
            andPracticeUos = patients.subList(start, end);
        }

        Page<Patient> page = new Page<>(patientAnalyseUo.getPage(), patientAnalyseUo.getLimit());
        page.setRecords(andPracticeUos);
        page.setTotal(patients.size());
        return page;
    }

    @Override
    public Page<Patient> getAllPatientOrderBy(OrderPatientUo orderPatientUo) {
        if (orderPatientUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }

        if (orderPatientUo.getOrder() == 1){
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                    .orderBy(true, true, convertedString)
                    .eq("user_id",orderPatientUo.getUserId());

            if (orderPatientUo.getName() != null && !orderPatientUo.getName().equals("")){
                wrapper.like("name",orderPatientUo.getName());
            }
            if (orderPatientUo.getUltrasonicNumber() != null && !orderPatientUo.getUltrasonicNumber().equals("")){
                wrapper.eq("ultrasonic_number",orderPatientUo.getUltrasonicNumber());
            }
            if (orderPatientUo.getType() != null){
                wrapper.eq("type",orderPatientUo.getType());
            }
            if (orderPatientUo.getPatientId() != null){
                wrapper.eq("id",orderPatientUo.getPatientId());
            }
            if (orderPatientUo.getUserId() != null){
                wrapper.eq("user_id",orderPatientUo.getUserId());
            }
            if (orderPatientUo.getStartTime() != null && orderPatientUo.getEndTime() != null){
                wrapper.between("create_time",orderPatientUo.getStartTime(),orderPatientUo.getEndTime());
            }else if (orderPatientUo.getStartTime() != null && orderPatientUo.getEndTime() == null){
                wrapper.gt("create_time",orderPatientUo.getStartTime());
            }else if (orderPatientUo.getStartTime() == null && orderPatientUo.getEndTime() != null){
                wrapper.lt("create_time",orderPatientUo.getEndTime());
            }else {
            }
            Page<Patient> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());
            return patientMapper.selectPage(page,wrapper);
        }else if (orderPatientUo.getOrder() == 0){
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                    .orderBy(true, false, convertedString)
                    .eq("user_id",orderPatientUo.getUserId());

            if (orderPatientUo.getName() != null && !orderPatientUo.getName().equals("")){
                wrapper.like("name",orderPatientUo.getName());
            }
            if (orderPatientUo.getUltrasonicNumber() != null && !orderPatientUo.getUltrasonicNumber().equals("")){
                wrapper.eq("ultrasonic_number",orderPatientUo.getUltrasonicNumber());
            }
            if (orderPatientUo.getType() != null){
                wrapper.eq("type",orderPatientUo.getType());
            }
            if (orderPatientUo.getPatientId() != null){
                wrapper.eq("id",orderPatientUo.getPatientId());
            }
            if (orderPatientUo.getUserId() != null){
                wrapper.eq("user_id",orderPatientUo.getUserId());
            }
            if (orderPatientUo.getStartTime() != null && orderPatientUo.getEndTime() != null){
                wrapper.between("create_time",orderPatientUo.getStartTime(),orderPatientUo.getEndTime());
            }else if (orderPatientUo.getStartTime() != null && orderPatientUo.getEndTime() == null){
                wrapper.gt("create_time",orderPatientUo.getStartTime());
            }else if (orderPatientUo.getStartTime() == null && orderPatientUo.getEndTime() != null){
                wrapper.lt("create_time",orderPatientUo.getEndTime());
            }else {
            }

            Page<Patient> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());
            return patientMapper.selectPage(page,wrapper);
        }else {
            return null;
        }
    }

    @Override
    public void delAllPatientByUserId(Long id) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                .eq("user_id", id);
        List<Patient> patientList = list(wrapper);
        if (patientList == null || patientList.size() == 0){
        }else {
            for (Patient patient:patientList){
//  这是删除病例对应异议
                patientAnalyseService.delByPatientId((long)patient.getId());
                removeById(patient.getId());
            }
        }
//  删除用户对应异议
        objectionService.delByUserId(id);
    }

    @Override
    public void delMorePatientIds(Long[] ids) {
        for (Long userId:ids){
            QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                    .eq("user_id", userId);
            List<Patient> patientList = list(wrapper);
            if (patientList == null || patientList.size() == 0){
            }else {
                for (Patient patient:patientList){
                    patientAnalyseService.delByPatientId((long)patient.getId());
                    removeById(patient.getId());
                }
            }
//  删除用户对应异议
            objectionService.delByUserId(userId);
        }
    }

    private String delMulu(String filename1) {
        String[] split ;
        String photoName = null;
        if (filename1.contains("/")){
            split = filename1.split("/");
            photoName = split[split.length-1];
        }else if (filename1.contains("\\")){
            split = filename1.split("\\\\");
            photoName = split[split.length-1];
        }else {
            photoName = filename1;
        }
        return photoName;
    }

    private List<PatientUo> getListByxlsx(MultipartFile file,Long userId) throws IOException {
        String fileName = file.getOriginalFilename(); // 获取文件名
//  得到 .jpg/.png/.xlsx
        String substring = fileName.substring(fileName.lastIndexOf("."));
        InputStream is = null;
        try{
            is = file.getInputStream();
            List<Map> patients = getListByExcel(is,fileName);// 获取解析后的List集合
            patients.remove(0);
            ArrayList<PatientUo> patientArrayList = new ArrayList<>();
            for (Map map:patients){
                PatientUo patient = new PatientUo();
                Object age1 = map.get("age");
                String agee = (String) age1;
                double aDouble = Double.parseDouble(agee);
                long round = Math.round(aDouble);
                patient.setAge((int) round);
                patient.setUserId(userId.intValue());
                patient.setDeleted(false);
                patient.setName((String) map.get("name"));
                String sex = (String) map.get("sex");
                if (sex.equals("男")){
                    patient.setSex(SexType.MALE);
                }else if (sex.equals("女")){
                    patient.setSex(SexType.FEMALE);
                }else {
                }
                patient.setInstrumentType((String) map.get("instrumentType"));
                patient.setUltrasonicNumber((String) map.get("ultrasonicNumber"));
                patient.setOutpatientNumber((String) map.get("outpatientNumber"));
                patient.setCheckItem((String) map.get("checkItem"));
                patient.setUltrasonicVideo((String) map.get("video"));
                patient.setUltsImgs((String) map.get("ultsImgs"));

                String ultsImgs = (String) map.get("ultsImgs");
                String ultrasonicVideo = (String) map.get("ultrasonicVideo");
                if (ultsImgs != null && !ultsImgs.equals("")){
                    patient.setType(PatientType.IMG);
                } else if (ultrasonicVideo != null && !ultrasonicVideo.equals("")) {
                    patient.setType(PatientType.VIDEO);
                }else {
                    throw new RuntimeException("未上传图片或视频");
                }
                patientArrayList.add(patient);
            }
            return patientArrayList;
        }catch (Exception e){
            throw new RuntimeException("文件解析失败");
        } finally {
            is.close();
        }
    }

    private List<Map> getListByExcel(InputStream is, String fileName) {
        try{
            List<Map> studentList = new ExcelUtilPatient(new PatientTo()).AnalysisExcel(is, fileName);
            return studentList;
        }catch (Exception e){
            throw new RuntimeException("文件解析失败");
        }
    }

}
