package cn.edu.zju.temp.controller;

import cn.edu.zju.temp.auth.CheckLogin;
import cn.edu.zju.temp.entity.Objection;
import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.so.ObjectionSo;
import cn.edu.zju.temp.entity.so.ResultObjectionSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ObjectionTo;
import cn.edu.zju.temp.entity.to.PracticeErrorTopTo;
import cn.edu.zju.temp.entity.to.UserTopTo;
import cn.edu.zju.temp.entity.uo.*;
import cn.edu.zju.temp.entity.vo.ObjectionVo;
import cn.edu.zju.temp.enums.AnswerType;
import cn.edu.zju.temp.service.IObjectionService;
import cn.edu.zju.temp.service.IPracticePageService;
import cn.edu.zju.temp.service.IQuestionService;
import cn.edu.zju.temp.service.IQuestionSetService;
import cn.edu.zju.temp.util.PageQuerySo;
import cn.edu.zju.temp.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/9 10:50
 * @version: 1.0
 */
@ApiOperation("模拟练习")
@RestController
@RequestMapping("/practice")
public class PracticeController {

    @Autowired
    IQuestionService questionService;

    @Autowired
    IObjectionService objectionService;

    @Autowired
    IPracticePageService practicePageService;

    @Autowired
    private IQuestionSetService questionSetService;


    /**
     * 得到所有题库列表，并显示在左边
     */
    @ApiOperation("得到对应套题要练习的题目")
    @PostMapping("/question/page")
    @CheckLogin
    public Result<Page<QuestionAndPracticeUo>> listQuestion(@RequestBody PracticeUo practiceUo){
        Page<QuestionAndPracticeUo> questionAndPracticeUoPage = questionService.getQuestionDetail(practiceUo);

        if (questionAndPracticeUoPage == null){
            return Result.ResultUtil.fail("获取失败");
        }else {
            return Result.ResultUtil.success(questionAndPracticeUoPage);
        }
    }
    /**
     * 模拟练习 答题 根据id查找对应题目以及正确答案
     * 返回true证明  回答正确   添加进练习题库里了
     * 返回false证明 回答错误  都添加进练习题库里了
     */
    @ApiOperation("模拟练习答题  true回答正确  false回答错误且添加进错题库")
    @GetMapping("/answer")
    @CheckLogin
    public Result<Boolean> practiceIssue(@RequestParam("id") Long questionId,@RequestParam("answer") Integer answerId,
                                         @RequestParam("userId") Long userId,@RequestParam("questionSetId") Long questionSetId){
        Boolean bool = questionService.answerQuestion(questionId, answerId,userId,questionSetId);
//设置上用户的题库正确率和题库完成率
        questionService.setUserCorrect(userId);
//计算套题正确率，正确人数，回答人数，题目数量
        questionSetService.setSomeThing(questionSetId);
        return Result.ResultUtil.success(bool);
    }

    @ApiOperation("收藏题目")
    @GetMapping("/collect")
    public Result<Boolean> collectQuestion(@RequestParam("id") Long questionId,
                                           @RequestParam(value = "answer",required = false) Integer answerId,
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("questionSetId") Long questionSetId){
        Boolean bool = questionService.collectQuestion(questionId,answerId,userId,questionSetId);
        return Result.ResultUtil.success(bool);
    }

    @ApiOperation("针对当前题目提提出异议")
    @PostMapping("/objection")
    public Result<Boolean> objectionIssue(@Validated @RequestBody ObjectionSo objectionSo){
        Boolean bool = questionService.objectionOnQuestion(objectionSo);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("异议已存在，提出失败");
        }
    }

    @ApiOperation("查看病例异议")
    @PostMapping("/objection/getcase/page")
    @CheckLogin
    public Result<Page<Objection>> getlistcaseObjection(@RequestBody ObjectionTo objectionTo){
        Page<Objection> objectionPage = objectionService.getlistcase(objectionTo);
        return Result.ResultUtil.success(objectionPage);
    }

    @ApiOperation("查看题库异议")
    @PostMapping("/objection/getbank/page")
    @CheckLogin
    public Result<Page<Objection>> getlistbankObjection(@RequestBody ObjectionTo objectionTo){
        Page<Objection> objectionPage = objectionService.getlistbank(objectionTo);
        return Result.ResultUtil.success(objectionPage);
    }

    @ApiOperation("病例异议列表根据字段排序显示 传入userId就显示当前用户  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/case/orderby")
    public Result<Page<Objection>> getPatientCaseObjection(@RequestBody OrderPatientStateUo orderPatientStateUo){

        Page<Objection> objectionPage = objectionService.getAllObjectionOrderBy(orderPatientStateUo);
        if (objectionPage != null){
            return Result.ResultUtil.success(objectionPage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }
    @ApiOperation("题库异议列表根据字段排序显示 传入userId就显示当前用户  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/bank/orderby")
    public Result<Page<Objection>> getPatientBankObjection(@RequestBody OrderPatientStateUo orderPatientStateUo){

        Page<Objection> objectionPage = objectionService.getAllObjectionBankOrderBy(orderPatientStateUo);
        if (objectionPage != null){
            return Result.ResultUtil.success(objectionPage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

    @ApiOperation("处理异议")
    @PostMapping("/objection/solve")
    public Result<Boolean> resultObjection(@Validated @RequestBody ResultObjectionSo resultObjectionSo){
        Boolean isSolve = objectionService.solveObjection(resultObjectionSo);
        return Result.ResultUtil.success(isSolve);
    }

    @ApiOperation("根据用户id 得到所有错题")
    @PostMapping("/errors/page")
    public Result<Page<PracticePage>> getAllErrorQuestion(@RequestBody ObjectionVo objectionVo){
        return Result.ResultUtil.success(practicePageService.getErrors(objectionVo));
    }

    @ApiOperation("根据用户id 得到所有收藏题")
    @PostMapping("/collects/page")
    public Result<Page<PracticePage>> getAllCollectQuestion(@RequestBody ObjectionVo objectionVo){
        return Result.ResultUtil.success(practicePageService.getCollects(objectionVo));
    }

    @ApiOperation("根据病例异议id 删除一个 病例异议数-1")
    @DeleteMapping("/objection/deleteone")
    public Result<Boolean> deleteOneByObjectionId(@RequestParam("objectionId") Integer objectionId){
        Boolean bool = questionService.deleteObjectionOne(objectionId);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }

    @ApiOperation("根据异议id 删除多个")
    @DeleteMapping("/objection/deletemore")
    public Result<Boolean> deleteOneObjectionIds(@RequestBody Map<String,Long[]> map){
        Long[] objectionIds = map.get("ids");
        Boolean bool = questionService.deleteObjectionMore(objectionIds);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }

    @ApiOperation("根据错题id 删除一个 题库对应答题数-1")
    @DeleteMapping("/error/deleteone")
    public Result<Boolean> deleteOneByErrorId(@RequestParam("errorId") Integer errorId){
        Boolean bool = questionService.deleteErrorOne(errorId);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }

    @ApiOperation("根据错题id 删除多个")
    @DeleteMapping("/error/deletemore")
    public Result<Boolean> deleteOneByErrorId(@RequestBody Map<String,Long[]> map){
        //得到所有
        Long[] errorids = map.get("ids");
        Boolean bool = questionService.deleteErrorMore(errorids);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }

    @ApiOperation("模拟练习页面根据题目id 删除一个收藏")
    @DeleteMapping("/collect/deleteone")
    @CheckLogin
    public Result<Boolean> deleteOneByCollectId(@RequestParam("questionId") Integer questionId,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        Boolean bool = questionService.deleteCollectOne(questionId,userId);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }
    @ApiOperation("根据收藏题id 删除多个")
    @DeleteMapping("/collect/deletemore")
    public Result<Boolean> deleteOneByCollectId(@RequestBody Map<String,Long[]> map){
        Long[] collectids = map.get("ids");
        Boolean bool = questionService.deleteCollectMore(collectids);
        if (bool){
            return Result.ResultUtil.success(bool);
        }else {
            return Result.ResultUtil.fail("删除失败了");
        }
    }
    @ApiOperation("错题列表根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/error/orderby")
    public Result<Page<PracticePage>> getErrorOrder(@RequestBody OrderPatientUo orderPatientUo){

        Page<PracticePage> practicePagePage = practicePageService.getAllErrorQuestionOrderBy(orderPatientUo);
        if (practicePagePage != null){
            return Result.ResultUtil.success(practicePagePage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

    @ApiOperation("收藏列表根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/collect/orderby")
    public Result<Page<PracticePage>> getCollectOrder(@RequestBody OrderPatientUo orderPatientUo){

        Page<PracticePage> practicePagePage = practicePageService.getAllCollectQuestionOrderBy(orderPatientUo);
        if (practicePagePage != null){
            return Result.ResultUtil.success(practicePagePage);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

    @ApiOperation("错题列表头部信息")
    @GetMapping("/top")
    @CheckLogin
    public Result<PracticeErrorTopTo> getUserTop(@RequestParam("userId") Integer userId){

        PracticeErrorTopTo practiceErrorTopTo = practicePageService.getErrorTopInfo(userId);
        if (practiceErrorTopTo != null){
            return Result.ResultUtil.success(practiceErrorTopTo);
        }else {
            return Result.ResultUtil.fail("获取错题头部信息失败");
        }
    }

    @ApiOperation("根据异议id查询 到patientId")
    @GetMapping("/toPatientId")
    public Result<Integer> getPatientIdByQuestionId(@RequestParam("objectionId") Integer objectionId){
        Integer patientId = practicePageService.getPatientIdByQuestionId(objectionId);
        return Result.ResultUtil.success(patientId);
    }

    @ApiOperation("重做错题接口")
    @GetMapping("/errorinfo")
    @CheckLogin
    public Result<QuestionAndPracticeUo> redoErrorQuestion(@RequestParam("questionId") Integer questionId, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        QuestionAndPracticeUo questionAndPracticeUo = practicePageService.redoErrorQuestion(questionId,userId);
        return Result.ResultUtil.success(questionAndPracticeUo);
    }

    @ApiOperation("重做做题详情")
    @PostMapping("/redo/error")
    @CheckLogin
    public Result<QuestionAndPracticeUo> redoErrorQuestionInfo(@RequestParam("id") Long questionId,@RequestParam("answer") Integer answerId,
                                                   @RequestParam("questionSetId") Integer questionSetId,
                                                   HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        QuestionAndPracticeUo questionAndPracticeUo = practicePageService.redoErrorInfo(questionId,answerId,questionSetId,userId);
        return Result.ResultUtil.success(questionAndPracticeUo);
    }
}