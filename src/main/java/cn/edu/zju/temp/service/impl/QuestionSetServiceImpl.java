package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.entity.*;
import cn.edu.zju.temp.entity.so.QuestionSetPageSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.QuestionSetTopTo;
import cn.edu.zju.temp.entity.to.QuestionTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionSetUo;
import cn.edu.zju.temp.entity.vo.QuestionSetPageVo;
import cn.edu.zju.temp.entity.vo.QuestionSetVo;
import cn.edu.zju.temp.enums.UserType;
import cn.edu.zju.temp.mapper.QuestionSetMapper;
import cn.edu.zju.temp.service.*;
import cn.edu.zju.temp.util.PageQuerySo;
import com.alibaba.druid.sql.PagerUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/17 9:42
 * @version: 1.0
 */
@Service
public class QuestionSetServiceImpl extends ServiceImpl<QuestionSetMapper, QuestionSet> implements IQuestionSetService {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private QuestionSetMapper questionSetMapper;

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IPracticePageService practicePageService;

    @Autowired
    private IUserService userService;

    @Override
    public Page<Question> getPageQuestionBySetId(QuestionSetVo questionSetVo) {
        LambdaQueryWrapper<Question> wrapper = Wrappers.lambdaQuery(Question.class)
                .eq(Question::getQuestionSetId,questionSetVo.getQuestionSetId());

        Page<Question> questionPage = questionService.page(new Page<>(questionSetVo.getPage(), questionSetVo.getLimit()));
        return questionService.page(questionPage, wrapper);
    }

    @Override
    public List<QuestionSet> setCorUsers(List<QuestionSet> questionSets) {
        //为每一个套题设置 正确率等
        for (QuestionSet questionSet:questionSets){
            Long questionSetId = questionSet.getId();
            LambdaQueryWrapper<Question> wrapper = Wrappers.lambdaQuery(Question.class)
                    .eq(Question::getQuestionSetId, questionSetId);

            List<Question> questionList = questionService.list(wrapper);
            int correctUser = 0; //设置正确人数
            int answerUser = 0; //回答人数
        //设置正确率
            if (questionList != null && questionList.size() > 0){
                double totalCorrect = 0.0;
                double numPatient = 0.0;
                for (Question question:questionList){
                    if (question.getCorrectRate() != null && !question.getCorrectRate().equals("")){
                        double oneAspectCorrect = Double.parseDouble(question.getCorrectRate().replace("%", "0"));
                        totalCorrect += oneAspectCorrect;
                        numPatient++;
                    }else {
                        numPatient++;
                    }
                    if (question.getCorrectAnswered() == null){
                    }else {
                        correctUser += question.getCorrectAnswered();
                    }
                    if (question.getAnsweredUser() == null){
                    }else {
                        answerUser += question.getAnsweredUser();
                    }
                }
                questionSet.setAnsweredUser(answerUser);
                questionSet.setCorrectAnswer(correctUser);
                BigDecimal correctUserBigDecimal = new BigDecimal(totalCorrect);
                BigDecimal answerUserBigDecimal = new BigDecimal(numPatient);
                if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                    BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);

                    double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    questionSet.setCorrectRate(rateNum + "%");
                }
            }else {
                questionSet.setCorrectRate("0.0%");
            }
        //设置题目总数
            QueryWrapper<Question> queryWrapper = new QueryWrapper<Question>()
                    .eq("question_set_id", questionSetId);
            List<Question> list = questionService.list(queryWrapper);
            questionSet.setQuestionSum(list.size());
        }
        saveOrUpdateBatch(questionSets);
        return questionSets;
    }

    @Override
    public Page<QuestionSet> getAllQuestionSetOrderBy(OrderUo orderUo) {

        if (orderUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }
        if (orderUo.getOrder() == 1){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<QuestionSet> wrapper = new QueryWrapper<QuestionSet>()
                    .orderBy(true, true, convertedString);
            Page<QuestionSet> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return questionSetMapper.selectPage(page,wrapper);
        }else if (orderUo.getOrder() == 0){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<QuestionSet> wrapper = new QueryWrapper<QuestionSet>()
                    .orderBy(true, false, convertedString);
            Page<QuestionSet> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return questionSetMapper.selectPage(page,wrapper);
        }else {
            return null;
        }
    }

    @Override
    public QuestionSet saveOneQuestionSet(String name, Integer examDuration) {
        QuestionSet questionSet = new QuestionSet();
        questionSet.setName(name);
        questionSet.setDeleted(false);
        questionSet.setQuestionSum(0);
        questionSet.setExamDuration(examDuration);  // 设置做题时长
        save(questionSet);
        return questionSet;
    }


    @Override
    public Boolean deleteSetOneById(String setId) {
        boolean isDeleted = removeById(setId);
        return isDeleted;
    }

    @Override
    public Boolean updateQuestionSet(QuestionSet questionSet) {
        boolean isupdated = updateById(questionSet);
        return isupdated;
    }

    @Override
    public QuestionSetTopTo getTopInfo(Integer questionSetId) {
        //未处理异议数
        QuestionSetTopTo questionSetTopTo = new QuestionSetTopTo();

        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("processing_state", "0")
                .eq("question_set_id",questionSetId);
        List<Objection> listUnResolve = objectionService.list(wrapper);
        QueryWrapper<Objection> objectionQueryWrapper = new QueryWrapper<Objection>()
                .eq("question_set_id", questionSetId);
        List<Objection> list = objectionService.list(objectionQueryWrapper);

        Integer total = list.size();
        Integer unTotal = listUnResolve.size();

        questionSetTopTo.setUnResolveNums(unTotal + "/" + total);

        //  题目数
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<Question>()
                .eq("question_set_id", questionSetId);
        List<Question> questionList = questionService.list(questionQueryWrapper);
        questionSetTopTo.setQuestionNum(questionList.size());

        //  题库正确率
        if (questionList != null && questionList.size() > 0){
            double totalCorrect = 0.0;
            double numPatient = 0.0;
            for (Question question:questionList){
                if (question.getCorrectRate() != null && !question.getCorrectRate().equals("")){
                    double oneAspectCorrect = Double.parseDouble(question.getCorrectRate().replace("%", "0"));
                    totalCorrect += oneAspectCorrect;
                    numPatient++;
                }else {
                    numPatient++;
                }
            }
            BigDecimal correctUserBigDecimal = new BigDecimal(totalCorrect);
            BigDecimal answerUserBigDecimal = new BigDecimal(numPatient);

            if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
                double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                questionSetTopTo.setCorrectRate(rateNum + "%");
            }else {
                questionSetTopTo.setCorrectRate("0.0%");
            }
        }else {
        //题库为空
            questionSetTopTo.setCorrectRate("0.0%");
        }

//  题库完成率  得到各个题目答题人数/应该答题人数
        double answerUser = 0.0;
        double shouldAnswerUser = 0.0;
        for (Question question:questionList){
            if (question.getAnsweredUser() == null){
            }else {
                answerUser += question.getAnsweredUser();
            }
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getRole, UserType.USER);
        List<User> users = userService.list(userLambdaQueryWrapper);
        shouldAnswerUser = users.size();
        BigDecimal answerdUserBigDecimal = new BigDecimal(answerUser);
        BigDecimal shouldanswerUserBigDecimal = new BigDecimal(shouldAnswerUser);

        if (shouldanswerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal answercompleteRateNumBigDecimal = answerdUserBigDecimal.divide(shouldanswerUserBigDecimal, 2, RoundingMode.HALF_UP);
            double answerrateNum = answercompleteRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            questionSetTopTo.setCompleteRate(answerrateNum + "%");
        }else {
            questionSetTopTo.setCompleteRate("0.0%");
        }
        return questionSetTopTo;
    }

    @Override
    public Page<QuestionSetPageVo> getErrorsQuestionSet(UserSo userSo) {
        //1.得到所有错题
        List<PracticePage> practicePageList = practicePageService.getErrorsByUserIdList(userSo.getUserId());
        if (practicePageList == null || practicePageList.size() == 0){
            return null;
        }else {
        //有错题，就得到 套题名称，错题总数等
            List<QuestionSet> questionSets = list();
            HashMap<Long, QuestionSetPageVo> questionSetHashMap = new HashMap<>();
            for (QuestionSet questionSet:questionSets){
                QuestionSetPageVo questionSetPageVo = new QuestionSetPageVo();
                BeanUtils.copyProperties(questionSet,questionSetPageVo);
                questionSetPageVo.setErrors(0);
                questionSetHashMap.put(questionSet.getId(),questionSetPageVo);
            }
        //得到了所有套题 用map存储 以便根据错题的套题id查询
        //遍历错题 得到所需结果
            for (PracticePage practicePage:practicePageList){
                Long questionSetId = practicePage.getQuestionSetId();
                if (questionSetId == null){
                }else {
                    QuestionSetPageVo questionSetPageVo = questionSetHashMap.get(questionSetId);
                    Integer errors = questionSetPageVo.getErrors();
                    errors++;
                    questionSetPageVo.setErrors(errors);
                }
            }
        //得到了所有套题，若有错题则错题总数量就不会为0
            Iterator<Long> iterator = questionSetHashMap.keySet().iterator();
            ArrayList<QuestionSetPageVo> questionSetPageVos = new ArrayList<>();
            while (iterator.hasNext()){
                Long questionSetId = iterator.next();
                QuestionSetPageVo questionSetPageVo = questionSetHashMap.get(questionSetId);
                if (questionSetPageVo.getErrors() > 0){
                    questionSetPageVos.add(questionSetPageVo);
                }
            }
        //得到所有有错题的套题
            Page<QuestionSet> questionSetPage = new Page<>(userSo.getPage(), userSo.getLimit());
            Page<QuestionSetPageVo> questionSetPageVoPage = new Page<>(questionSetPage.getCurrent(), questionSetPage.getSize(), questionSetPage.getTotal());
            Long page = userSo.getPage();
            Long limit = userSo.getLimit();
            Long startIndex = (page - 1) * limit;
            List<QuestionSetPageVo> questionSetPageVoList = questionSetPageVos.stream().skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList());
            questionSetPageVoPage.setRecords(questionSetPageVoList);
            questionSetPageVoPage.setTotal(questionSetPageVos.size());
            return questionSetPageVoPage;
        }
    }

    @Override
    public Page<PracticePage> getAllerrorsByQuestionId(QuestionSetPageSo questionSetPageSo,Integer userId) {
        LambdaQueryWrapper<PracticePage> wrapper = Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getQuestionSetId, questionSetPageSo.getQuestionSetId())
                .eq(PracticePage::getUserId, userId)
                .eq(PracticePage::getIsTrue,false);

        Page<PracticePage> page = new Page<>(questionSetPageSo.getPage(), questionSetPageSo.getLimit());
        Page<PracticePage> practicePagePage = practicePageService.page(page, wrapper);
        return practicePagePage;
    }

    @Override
    public Page<QuestionSetPageVo> getCollectQuestionSet(UserSo userSo) {
        //1.得到所有错题
        List<PracticePage> practicePageList = practicePageService.getCollectsByUserIdList(userSo.getUserId());
        if (practicePageList == null || practicePageList.size() == 0){
            return null;
        }else {
            //有错题，就得到 套题名称，错题总数等
            List<QuestionSet> questionSets = list();
            HashMap<Long, QuestionSetPageVo> questionSetHashMap = new HashMap<>();
            for (QuestionSet questionSet:questionSets){
                QuestionSetPageVo questionSetPageVo = new QuestionSetPageVo();
                BeanUtils.copyProperties(questionSet,questionSetPageVo);
                questionSetPageVo.setErrors(0);
                questionSetHashMap.put(questionSet.getId(),questionSetPageVo);
            }
            //得到了所有套题 用map存储 以便根据错题的套题id查询
            //遍历错题 得到所需结果
            for (PracticePage practicePage:practicePageList){
                Long questionSetId = practicePage.getQuestionSetId();
                if (questionSetId == null){
                }else {
                    QuestionSetPageVo questionSetPageVo = questionSetHashMap.get(questionSetId);
                    Integer errors = questionSetPageVo.getErrors();
                    errors++;
                    questionSetPageVo.setErrors(errors);
                }
            }
            //得到了所有套题，若有错题则错题总数量就不会为0
            Iterator<Long> iterator = questionSetHashMap.keySet().iterator();
            ArrayList<QuestionSetPageVo> questionSetPageVos = new ArrayList<>();
            while (iterator.hasNext()){
                Long questionSetId = iterator.next();
                QuestionSetPageVo questionSetPageVo = questionSetHashMap.get(questionSetId);
                if (questionSetPageVo.getErrors() > 0){
                    questionSetPageVos.add(questionSetPageVo);
                }
            }
            //得到所有有错题的套题
            Page<QuestionSet> questionSetPage = new Page<>(userSo.getPage(), userSo.getLimit());
            Page<QuestionSetPageVo> questionSetPageVoPage = new Page<>(questionSetPage.getCurrent(), questionSetPage.getSize(), questionSetPage.getTotal());
            Long page = userSo.getPage();
            Long limit = userSo.getLimit();
            Long startIndex = (page - 1) * limit;
            List<QuestionSetPageVo> questionSetPageVoList = questionSetPageVos.stream().skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList());
            questionSetPageVoPage.setRecords(questionSetPageVoList);
            questionSetPageVoPage.setTotal(questionSetPageVos.size());
            return questionSetPageVoPage;
        }
    }
    @Override
    public Page<PracticePage> getAllcollectsByQuestionId(QuestionSetPageSo questionSetPageSo, Integer userId) {
        LambdaQueryWrapper<PracticePage> wrapper = Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getQuestionSetId, questionSetPageSo.getQuestionSetId())
                .eq(PracticePage::getUserId, userId)
                .eq(PracticePage::getIsCollect,true);

        Page<PracticePage> page = new Page<>(questionSetPageSo.getPage(), questionSetPageSo.getLimit());
        Page<PracticePage> practicePagePage = practicePageService.page(page, wrapper);
        return practicePagePage;
    }

    @Override
    public Page<QuestionSetUo> pageQuestionSet(PracticeUo practiceUo,Integer userId) {
        //正确数 题目数量 准确率
        Page<QuestionSet> questionSetPage = new Page<>(practiceUo.getPage(), practiceUo.getLimit());
        Page<QuestionSet> questionSetPageList = page(questionSetPage, null);
        List<QuestionSetUo> questionSetUoList = questionSetPageList.getRecords().stream()
                .map(questionSet -> {
                    QuestionSetUo questionSetUo = new QuestionSetUo();
                    BeanUtils.copyProperties(questionSet, questionSetUo);
                    questionSetUo.setQuestionRight(0);
                    setAnswerRightAndRate(questionSetUo,userId);
                    return questionSetUo;
                }).collect(Collectors.toList());
        Page<QuestionSetUo> resultPage = new Page<>(questionSetPage.getCurrent(), questionSetPage.getSize(), questionSetPage.getTotal());
        resultPage.setRecords(questionSetUoList);
        return resultPage;
    }

    @Override
    public void setSomeThing(Long questionSetId) {
        QuestionSet questionSet = getById(questionSetId);

        //  题目数
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<Question>()
                .eq("question_set_id", questionSetId);
        List<Question> questionList = questionService.list(questionQueryWrapper);

        //  题库正确率
        if (questionList != null && questionList.size() > 0){
            double totalCorrect = 0.0;
            double numPatient = 0.0;
            Integer correctUser = 0;
            Integer answerUser = 0;
            for (Question question:questionList){
                if (question.getCorrectRate() != null && !question.getCorrectRate().equals("")){
                    double oneAspectCorrect = Double.parseDouble(question.getCorrectRate().replace("%", "0"));
                    totalCorrect += oneAspectCorrect;
                    numPatient++;
                }else {
                    numPatient++;
                }
                Integer correctAnswered = question.getCorrectAnswered();
                if (correctAnswered == null){
                }else {
                    correctUser += correctAnswered;
                }
                Integer answeredUser = question.getAnsweredUser();
                if (answeredUser == null){
                }else {
                    answerUser += answeredUser;
                }
            }
            int totalQuestion = (int) Math.round(numPatient);
            questionSet.setQuestionSum(totalQuestion);
            questionSet.setCorrectAnswer(correctUser);
            questionSet.setAnsweredUser(answerUser);
            BigDecimal correctUserBigDecimal = new BigDecimal(totalCorrect);
            BigDecimal answerUserBigDecimal = new BigDecimal(numPatient);

            if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
                double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
//                user.setCorrectRateQuestionBank(String.valueOf(rateNum)); // ✅ 正确写法
                questionSet.setCorrectRate(String.valueOf(rateNum));
            }else {
                questionSet.setCorrectRate("0.0%");
            }
        }else {            //题库为空
            questionSet.setCorrectRate("0.0%");
        }

//  题库完成率  得到各个题目答题人数/应该答题人数
        double answerUser = 0.0;
        double shouldAnswerUser = 0.0;
        for (Question question:questionList){
            if (question.getAnsweredUser() == null){
            }else {
                answerUser += question.getAnsweredUser();
            }
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getRole, UserType.USER);
        List<User> users = userService.list(userLambdaQueryWrapper);
        shouldAnswerUser = users.size();
        BigDecimal answerdUserBigDecimal = new BigDecimal(answerUser);
        BigDecimal shouldanswerUserBigDecimal = new BigDecimal(shouldAnswerUser);

        if (shouldanswerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal answercompleteRateNumBigDecimal = answerdUserBigDecimal.divide(shouldanswerUserBigDecimal, 2, RoundingMode.HALF_UP);
            double answerrateNum = answercompleteRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            questionSet.setCompleteRate(answerrateNum + "%");
        }else {
            questionSet.setCompleteRate("0.0%");
        }
        updateById(questionSet);
    }

//    Page<ModelFusion> modelFusionPage = page(page.initPage());
//        List<ModelFusionPageVo> list = modelFusionPage.getRecords()
//                .stream()
//                .map(ModelFusionPageVo::create)
//                .collect(Collectors.toList());
//        Page<ModelFusionPageVo> resultPage = new Page<>(modelFusionPage.getCurrent(), modelFusionPage.getSize(), modelFusionPage.getTotal());
//        resultPage.setRecords(list);
//        return resultPage;

    private void setAnswerRightAndRate(QuestionSetUo questionSetUo,Integer userId) {
        QueryWrapper<PracticePage> queryWrapper = new QueryWrapper<PracticePage>()
                .eq("question_set_id", questionSetUo.getId())
                .eq("user_id",userId);
        List<PracticePage> practicePageList = practicePageService.list(queryWrapper);
        if (practicePageList == null || practicePageList.size() == 0){
        }else {
            for (PracticePage practicePage:practicePageList){
                if (practicePage.getIsTrue() == null || practicePage.getIsTrue() == false){
                }else {
                    Integer questionRight = questionSetUo.getQuestionRight();
                    questionRight++;
                    questionSetUo.setQuestionRight(questionRight);
                }
            }
        }
        //有正确数量，也有全部题目数量，算出正确率
        BigDecimal correctUserBigDecimal = new BigDecimal(questionSetUo.getQuestionRight());
        BigDecimal answerUserBigDecimal = new BigDecimal(questionSetUo.getQuestionSum());

        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
            double correctRate = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            questionSetUo.setCorrectRate(correctRate);
        }else {
            questionSetUo.setCorrectRate(0.0);
        }
    }


}