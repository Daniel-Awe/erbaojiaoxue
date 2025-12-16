package cn.edu.zju.temp.controller;

import cn.edu.zju.temp.auth.CheckLogin;
import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.so.ImgInfoSo;
import cn.edu.zju.temp.entity.so.PatientSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ImgInfoFrameTo;
import cn.edu.zju.temp.entity.to.PatientAnalyseTo;
import cn.edu.zju.temp.entity.to.PatientTopTo;
import cn.edu.zju.temp.entity.to.UserTopTo;
import cn.edu.zju.temp.entity.uo.*;
import cn.edu.zju.temp.entity.vo.PatientAnalyseVo;
import cn.edu.zju.temp.entity.vo.PatientVo;
import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.service.IPatientAnalyseService;
import cn.edu.zju.temp.service.IPatientService;
import cn.edu.zju.temp.service.IQuestionService;
import cn.edu.zju.temp.service.IUserService;
import cn.edu.zju.temp.util.PageQuerySo;
import cn.edu.zju.temp.util.Result;
import cn.edu.zju.temp.util.ZipTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author: gsq
 * @description: 病人列表
 * @date: 2024/5/7 8:44
 * @version: 1.0
 */
@Slf4j
@ApiOperation("实操练习页面")
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private IPatientService patientService;


    @Autowired
    private IUserService userService;

    @Autowired
    private IPatientAnalyseService patientAnalyseService;


    @ApiOperation(value = "增加病人数据方式1 新增页面")
    @PostMapping("/addbypage")
    public Result<Patient> addPatientByPage(@Validated PatientSo patientSo) throws IOException {
        Patient patient = patientService.addPatientPage(patientSo);
        return Result.ResultUtil.success(patient);
    }

    @ApiOperation(value = "通过zip压缩包导入病人表")
    @PostMapping("/addbyzip")
    public Result<Boolean> addPatientByZip(@Validated PatientZipUo patientZipUo) throws IOException {
        Boolean bool = patientService.addByZip(patientZipUo);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("通过zip压缩包添加病人失败");
        }
    }

    @ApiOperation(value = "删除单个病人数据")
    @DeleteMapping("/deleteone")
    @CheckLogin
    public Result<Boolean> deleteOnePatient(@RequestParam("id") Long id,HttpServletRequest request){
        Boolean isDeleted = patientService.deleteOnePatientById(id);
        if (isDeleted){
//  删除对应的病例分析图  以及对应异议
            patientAnalyseService.delByPatientId(id);

//  将当前用户的上传病例数 -1
            Integer userId = (Integer) request.getAttribute("userId");
            userService.delUploadCase(userId);
//  修改用户病例切面正确率
            userService.setPatientAspectCorrect(userId);
            return Result.ResultUtil.success(isDeleted);
        }else {
            return Result.ResultUtil.fail("删除病人信息失败");
        }
    }

    @ApiOperation(value = "删除多个病人数据")
    @DeleteMapping("/deletemore")
    @CheckLogin
    public Result<Boolean> deleteMorePatient(@RequestBody Map<String,Long[]> map,HttpServletRequest request){
        Long[] ids = map.get("ids");
        if (ids == null || ids.length == 0){
            throw new RuntimeException("请选择病人删除");
        }
        Boolean bool = patientService.deleteMoreByIds(ids);
        if (bool){
//  删除对应病理分析图 todo 待部署  以及异议
            patientAnalyseService.delMoreByids(ids);

//  将当前用户的上传病例数 -ids.length
            Integer userId = (Integer) request.getAttribute("userId");
            userService.delUploadCases(ids.length,userId);
//  设置用户切面正确率
            userService.setPatientAspectCorrect(userId);
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除多个病人信息失败");
        }
    }

    @ApiOperation(value = "修改病人信息")
    @PutMapping("/update")
    public Result<Boolean> updatePatient(@RequestBody Patient patient){

        Boolean bool = patientService.updatePatient(patient);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("修改病人信息失败");
        }
    }

    @ApiOperation(value = "获取所有病人数据 分页显示")
    @PostMapping("/page")
    @CheckLogin
    public Result<Page<Patient>> getallPatient(@RequestBody UserSo userSo){
        QueryWrapper<Patient> wrapper = null;
        if (userSo.getUserId() == null){
            wrapper = new QueryWrapper<Patient>();
        }else {
            wrapper = new QueryWrapper<Patient>()
                    .eq("user_id",userSo.getUserId());
        }
        return Result.ResultUtil.success(patientService.page(new Page<Patient>(userSo.getPage(),userSo.getLimit()), wrapper));
    }

    @ApiOperation(value = "条件查询病人数据")
    @PostMapping("/info/page")
    @CheckLogin
    public Result<Page<Patient>> getListInfo(@RequestBody PatientVo patientVo){
        Page<Patient> pages = patientService.updatePatientInfo(patientVo);
        return Result.ResultUtil.success(pages);
    }

    @ApiOperation(value = "病人列表 查看图片页面/ 导入题库时，也调用这个请求")
    @PostMapping("/imgs/page")
    @CheckLogin
    public Result<Page<PatientAnalyseTo>> getPatientAnalyseImgs(@RequestBody PatientAnalyseVo patientAnalyseVo,HttpServletRequest request){
        Page<PatientAnalyseTo> patientAnalyseToPage = patientService.getAnalyseImgsById(patientAnalyseVo,patientAnalyseVo.getUserId());
        return Result.ResultUtil.success(patientAnalyseToPage);
    }

    @ApiOperation(value = "选择视频，解析每一帧")
    @GetMapping("/video")
    public Result<Integer> getVideo(@RequestParam("videopath") String videopath) throws IOException {
       Integer sum = patientService.getVideoByPath(videopath);
       return Result.ResultUtil.success(sum);
    }

    @ApiOperation(value = "根据每一帧路径，算法解析得到结果")
    @GetMapping("/video/frame")
    public Result<ImgInfoFrameTo> getVideoFrame(@RequestParam("framePath") String framePath){
        ImgInfoFrameTo imgInfoFrameTo = patientService.getAnalyseByC(framePath);
        if (imgInfoFrameTo == null){
            return Result.ResultUtil.fail("算法未检测出来");
        }else {
            return Result.ResultUtil.success(imgInfoFrameTo);
        }
    }

    @ApiOperation("病例列表头部信息")
    @GetMapping("/top")
    @CheckLogin
    public Result<PatientTopTo> getPatientTop(@RequestParam("userId") Integer userId, HttpServletRequest request){
        if (userId == null){
            throw new RuntimeException("用户不存在");
        }
        PatientTopTo patientTopTo = patientService.getTopInfo(userId);
        if (patientTopTo != null){
            return Result.ResultUtil.success(patientTopTo);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }


    @ApiOperation("病人列表根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/orderby")
    public Result<Page<Patient>> getPatientTop(@RequestBody OrderPatientUo orderPatientUo){

        Page<Patient> questionPage = patientService.getAllPatientOrderBy(orderPatientUo);
        if (questionPage != null){
            return Result.ResultUtil.success(questionPage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

}