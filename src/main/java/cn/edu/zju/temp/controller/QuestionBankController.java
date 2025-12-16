package cn.edu.zju.temp.controller;

import cn.edu.zju.temp.auth.CheckLogin;
import cn.edu.zju.temp.config.OssUploadConfig;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.QuestionSet;
import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.entity.so.QuestionSo;
import cn.edu.zju.temp.entity.to.QuestionTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.vo.CaseIdAndSetIdVo;
import cn.edu.zju.temp.entity.vo.QuestionSetVo;
import cn.edu.zju.temp.entity.vo.QuestionVo;
import cn.edu.zju.temp.service.IObjectionService;
import cn.edu.zju.temp.service.IQuestionService;
import cn.edu.zju.temp.service.IQuestionSetService;
import cn.edu.zju.temp.util.CommonUpload;
import cn.edu.zju.temp.util.PageQuerySo;
import cn.edu.zju.temp.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: gsq
 * @description: 题库控制器
 * @date: 2025/3/6 16:01
 * @version: 1.0
 */

@ApiOperation("题库列表")
@RestController
@RequestMapping("/question")
public class QuestionBankController {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IQuestionSetService questionSetService;

    /**
     * 首先将图片或视频上传至oss，得到url路径返回前端
     * @param questionSo
     * @return
     */
    @ApiOperation(value = "增加题目")
    @PostMapping("/add/bank")
    public Result<Question> addQuestion(@Validated QuestionSo questionSo){
        Question question = questionService.addQuestion(questionSo);
        return Result.ResultUtil.success(question);
    }

    @ApiOperation(value = "zip文件导入题目 导入之前要先创建套题记录")
    @PostMapping("/add/zip")
    public Result<Boolean> addQuestionByZip(@RequestPart("file")MultipartFile file,@RequestParam("questionSetId") Long questionSetId) throws IOException {
        Boolean bool = questionService.addByZip(file,questionSetId);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("上传失败");
        }
    }

    @ApiOperation(value = "病例导入题目 传来病理分析ids")
    @PostMapping("/add/case")
    public Result<List<Question>> addQuestionByCase(@RequestBody CaseIdAndSetIdVo caseIdAndSetIdVo){
        Long[] ids = caseIdAndSetIdVo.getIds();
        if (ids == null || ids.length == 0){
            throw new RuntimeException("请选择病例分析id");
        }
        Boolean bool = questionService.addQuestionByAnalyseIds(ids,caseIdAndSetIdVo.getQuestionSetId());
        if (bool){
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("添加失败失败");
        }
    }


    @ApiOperation(value = "删除单个题目")
    @DeleteMapping("/deleteone")
    public Result<Boolean> deleteQuestion(@RequestParam("id") Long id) {
        Boolean isdelete = questionService.deleteOneQuestionById(id);
        if (isdelete){
//  对应题目异议删除
            objectionService.delByQuestionId(id);
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "删除多个题目")
    @DeleteMapping("/deletemore")
    public Result<Boolean> deleteQuestionMore(@RequestBody Map<String,Long[]> map){
        Long[] ids = map.get("ids");
        if (ids == null || ids.length == 0){
            throw new RuntimeException("请选择题目删除");
        }
        Boolean isdelete = questionService.deleteQuestionMoreByIds(map.get("ids"));
        if (isdelete){
//  对应题目异议删除
            objectionService.delMoreQuestionIds(ids);
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "修改题目信息")
    @PutMapping("/update")
    public Result<Boolean> updateQuestion(QuestionVo questionVo){
        Boolean isUpdated = questionService.updateOneByQuestionVo(questionVo);
        if (isUpdated){
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("修改失败");
        }
    }

    @ApiOperation(value = "分页获取题库列表,显示所有套题")
    @PostMapping("/page")
    @CheckLogin
    public Result<Page<QuestionSet>> getQuestionBank(@RequestBody QuestionSetVo questionSetVo){
        LambdaQueryWrapper<QuestionSet> wrapper = Wrappers.lambdaQuery(QuestionSet.class);
        Page<QuestionSet> page = new Page<>(questionSetVo.getPage(), questionSetVo.getLimit());
        Page<QuestionSet> questionSetPage = questionSetService.page(page, wrapper);
        List<QuestionSet> questionSets = questionSetPage.getRecords();
        //设置所有套题正确率，正确人数，答题人数,题目总数
        List<QuestionSet> questionSetList = questionSetService.setCorUsers(questionSets);

        page.setRecords(questionSetList);
        return Result.ResultUtil.success(page);
    }

    @ApiOperation(value = "获取题目头部列表")
    @GetMapping("/top")
    public Result<QuestionTopTo> getQuestionTopInfo(){

        QuestionTopTo questionTopTo = questionService.getTopInfo();
        return Result.ResultUtil.success(questionTopTo);
    }

    @ApiOperation("题目列表根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/orderby")
    @CheckLogin
    public Result<Page<Question>> getPatientTop(@RequestBody OrderUo orderUo, HttpServletRequest request){
        Page<Question> questionPage = questionService.getAllQuestionOrderBy(orderUo);
        if (questionPage != null){
            return Result.ResultUtil.success(questionPage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

}