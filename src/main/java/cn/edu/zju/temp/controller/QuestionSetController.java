package cn.edu.zju.temp.controller;

import cn.edu.zju.temp.auth.CheckLogin;
import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.QuestionSet;
import cn.edu.zju.temp.entity.so.QuestionSetPageSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.QuestionSetTopTo;
import cn.edu.zju.temp.entity.to.QuestionTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionAndPracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionSetUo;
import cn.edu.zju.temp.entity.vo.QuestionSetPageVo;
import cn.edu.zju.temp.entity.vo.QuestionSetVo;
import cn.edu.zju.temp.service.IQuestionSetService;
import cn.edu.zju.temp.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/17 9:38
 * @version: 1.0
 */
@RestController
@RequestMapping("/question/set")
public class QuestionSetController {

    @Autowired
    private IQuestionSetService questionSetService;

    @ApiOperation(value = "根据题目套题id 得到所有题目")
    @PostMapping("/list")
    @CheckLogin
    public Result<Page<Question>> getQuestionBySetId(@RequestBody QuestionSetVo questionSetVo){
        Page<Question> questionPage = questionSetService.getPageQuestionBySetId(questionSetVo);
        return Result.ResultUtil.success(questionPage);
    }

    @ApiOperation("题目套题根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/orderby")
    @CheckLogin
    public Result<Page<QuestionSet>> getPatientTop(@RequestBody OrderUo orderUo, HttpServletRequest request){
        Page<QuestionSet> questionSetPage = questionSetService.getAllQuestionSetOrderBy(orderUo);
        if (questionSetPage != null){
            return Result.ResultUtil.success(questionSetPage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

    @ApiOperation("新增套题名称 返回给创建的套题信息")
    @PostMapping("/save")
    @CheckLogin
    public Result<QuestionSet> saveQuestionSet(@RequestParam("name") String name,
                                               @RequestParam(value = "examDuration", required = false, defaultValue = "0") Integer examDuration) {
        QuestionSet questionSet = questionSetService.saveOneQuestionSet(name, examDuration);
        return Result.ResultUtil.success(questionSet);
    }

    @ApiOperation("删除单个套题信息")
    @DeleteMapping("/deleteone")
    @CheckLogin
    public Result<Boolean> deleteOneQuestionSet(@RequestParam("id") String setId){
        Boolean bool = questionSetService.deleteSetOneById(setId);
        return Result.ResultUtil.success(bool);
    }

    @ApiOperation("修改套题信息")
    @PostMapping("/update")
    @CheckLogin
    public Result<Boolean> updateQuestionSet(@RequestBody QuestionSet questionSet){
        Boolean bool = questionSetService.updateQuestionSet(questionSet);
        return Result.ResultUtil.success(bool);
    }

    @ApiOperation(value = "获取套题头部列表")
    @GetMapping("/top")
    public Result<QuestionSetTopTo> getQuestionTopInfo(@RequestParam("questionSetId") Integer questionSetId){
        QuestionSetTopTo questionSetTopTo = questionSetService.getTopInfo(questionSetId);
        return Result.ResultUtil.success(questionSetTopTo);
    }

    @ApiOperation(value = "用户得到所有错题的套题列表")
    @PostMapping("/errors")
    @CheckLogin
    public Result<Page<QuestionSetPageVo>> getQuestionBySetId(@RequestBody UserSo userSo){
        Page<QuestionSetPageVo> questionSetPage = questionSetService.getErrorsQuestionSet(userSo);
        return Result.ResultUtil.success(questionSetPage);
    }
    @ApiOperation(value = "得到当前套题的错题")
    @PostMapping("/error/info")
    @CheckLogin
    public Result<Page<PracticePage>> getErrors(@RequestBody QuestionSetPageSo questionSetPageSo,HttpServletRequest request){
        Integer userId = null;
        if (questionSetPageSo.getUserId() == null){
            userId = (Integer) request.getAttribute("userId");
        }else {
            userId = questionSetPageSo.getUserId();
        }
        Page<PracticePage> practicePagePage = questionSetService.getAllerrorsByQuestionId(questionSetPageSo,userId);
        return Result.ResultUtil.success(practicePagePage);
    }

    @ApiOperation(value = "用户得到所有收藏的套题列表")
    @PostMapping("/collects")
    @CheckLogin
    public Result<Page<QuestionSetPageVo>> getQuestionBySetIdCollect(@RequestBody UserSo userSo){
        Page<QuestionSetPageVo> questionSetPage = questionSetService.getCollectQuestionSet(userSo);
        return Result.ResultUtil.success(questionSetPage);
    }

    @ApiOperation(value = "得到当前套题的收藏题")
    @PostMapping("/collect/info")
    @CheckLogin
    public Result<Page<PracticePage>> getCollects(@RequestBody QuestionSetPageSo questionSetPageSo,HttpServletRequest request){
        Integer userId = null;
        if (questionSetPageSo.getUserId() == null){
            userId = (Integer) request.getAttribute("userId");
        }else {
            userId = questionSetPageSo.getUserId();
        }
        Page<PracticePage> practicePagePage = questionSetService.getAllcollectsByQuestionId(questionSetPageSo,userId);
        return Result.ResultUtil.success(practicePagePage);
    }

    @ApiOperation("模拟练习获得所有套题信息")
    @PostMapping("/page")
    @CheckLogin
    public Result<Page<QuestionSetUo>> listQuestion(@RequestBody PracticeUo practiceUo,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        Page<QuestionSetUo> questionSetPage = questionSetService.pageQuestionSet(practiceUo,userId);
        if (questionSetPage == null){
            return Result.ResultUtil.fail("获取失败");
        }else {
            return Result.ResultUtil.success(questionSetPage);
        }
    }
}