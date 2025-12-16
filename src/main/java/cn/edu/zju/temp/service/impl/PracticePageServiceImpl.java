package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.entity.*;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.PracticeErrorTopTo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.QuestionAndPracticeUo;
import cn.edu.zju.temp.entity.vo.ObjectionVo;
import cn.edu.zju.temp.enums.AnswerType;
import cn.edu.zju.temp.mapper.PracticeMapper;
import cn.edu.zju.temp.service.*;
import cn.edu.zju.temp.util.PageQuerySo;
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
import java.util.List;

/**
 * @author: gsq
 * @description: 练习题 错题以及 收藏题
 * @date: 2024/5/11 10:35
 * @version: 1.0
 */
@Service
public class PracticePageServiceImpl extends ServiceImpl<PracticeMapper, PracticePage> implements IPracticePageService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IQuestionService questionService;


    @Autowired
    private PracticeMapper practiceMapper;

    @Autowired
    private IPatientAnalyseService patientAnalyseService;

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IPracticePageService practicePageService;

    @Autowired
    private IQuestionSetService questionSetService;

    @Override
    public Boolean addErrorQuestion(Long questionId, Long userId,String answer,Long questionSetId) {
        //  先判断是否已经收藏
        PracticePage practicePage1 = getOne(new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("question_id", questionId));

        if (practicePage1 != null){
            practicePage1.setIsTrue(false);
            return updateById(practicePage1);
        }else {
            //  设置上错题
            Question question = questionService.getById(questionId);
            PracticePage practicePage = new PracticePage(false);

            practicePage.setUserId(userId);
            practicePage.setQuestionId(questionId);
            practicePage.setUserAnswer(answer);
            practicePage.setAnswer(question.getAnswer());
            practicePage.setName(question.getName());
            practicePage.setIsObjection(false);
            practicePage.setIsTrue(false);
            practicePage.setIsCollect(false);
            practicePage.setTopicSource(question.getTopicSource());
            practicePage.setQuestionSetId(questionSetId);
            return save(practicePage);
        }
    }

    @Override
    public Page<PracticePage> getErrors(ObjectionVo objectionVo) {

        Page<PracticePage> practicePage = new Page<>(objectionVo.getPage(), objectionVo.getLimit());
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id",objectionVo.getUserId())
                .eq("is_true",false);
        return practiceMapper.selectPage(practicePage, wrapper);
    }

    @Override
    public Boolean addTrueQuestion(Long questionId, Long userId, String answer,Long questionSetId) {

//  先判断是否已经收藏
        PracticePage practicePage1 = getOne(new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("question_id", questionId));

//  存在就更新是否正确字段
        if (practicePage1 != null){
            practicePage1.setIsTrue(true);
            return updateById(practicePage1);
        }else {
//  不存在就添加 设置上错题
            Question question = questionService.getById(questionId);
            PracticePage practicePage = new PracticePage(false);

            practicePage.setUserId(userId);
            practicePage.setQuestionId(questionId);
            practicePage.setUserAnswer(answer);
            practicePage.setAnswer(question.getAnswer());
            practicePage.setName(question.getName());
            practicePage.setIsObjection(false);
            practicePage.setIsTrue(true);
            practicePage.setIsCollect(false);
            practicePage.setTopicSource(question.getTopicSource());
            practicePage.setQuestionSetId(questionSetId);
            return save(practicePage);
        }
    }

    @Override
    public Boolean addCollectQuestion(Long questionId, Long userId, Integer answerId,Long questionSetId) {

//  先判断是否在练习题库里面
        PracticePage practicePage1 = getOne(new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("question_id", questionId));

//  1.在练习题库里面就 更新是否收藏字段为收藏
        if (practicePage1 != null){
            practicePage1.setIsCollect(true);
            boolean isUpdate = updateById(practicePage1);
            return isUpdate;
        }else {
//  2 不在就添加练习题库

            String answer = "";
            if (answerId != null && answerId == 0){
                answer = AnswerType.NON_STANDARD_ASPECT.getMsg();
            }else if (answerId != null){
                answer = "切面"+answerId;
            }else {
            }
            Question question = questionService.getById(questionId);
            PracticePage practicePage = new PracticePage(false);

            practicePage.setUserId(userId);
            practicePage.setQuestionId(questionId);
            practicePage.setUserAnswer(answer);
            practicePage.setAnswer(question.getAnswer());
            practicePage.setName(question.getName());
            practicePage.setIsObjection(false);
            practicePage.setIsCollect(true);
            practicePage.setTopicSource(question.getTopicSource());
            practicePage.setQuestionSetId(questionSetId);
            boolean save = save(practicePage);
            return save;
        }
    }

    @Override
    public Page<PracticePage> getCollects(ObjectionVo objectionVo) {
        Page<PracticePage> practicePage = new Page<>(objectionVo.getPage(), objectionVo.getLimit());
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", objectionVo.getUserId())
                .eq("is_collect", true);
        return practiceMapper.selectPage(practicePage, wrapper);
    }

    @Override
    public Page<PracticePage> getErrorsByUserId(UserSo userSo) {
        Page<PracticePage> practicePagePage = new Page<>(userSo.getPage(), userSo.getLimit());
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", userSo.getUserId())
                .eq("is_true", false);
        return practiceMapper.selectPage(practicePagePage,wrapper);
    }

    @Override
    public PracticeErrorTopTo getErrorTopInfo(Integer userId) {

        PracticeErrorTopTo practiceErrorTopTo = new PracticeErrorTopTo();

//  今日错题
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .apply("date(create_time) = date(now())")
                .eq("is_true",false)
                .eq("user_id",userId);

        List<PracticePage> todaypatients = list(wrapper);
        practiceErrorTopTo.setTodayError(todaypatients.size());

//  错题总数
        QueryWrapper<PracticePage> queryWrapper = new QueryWrapper<PracticePage>()
                .eq("is_true", false)
                .eq("user_id",userId);

        List<PracticePage> pageList = list(queryWrapper);
        practiceErrorTopTo.setTotalSum(pageList.size());

//  正确率
        User user = userService.getById(userId);
        if (user != null){

            if (user.getCorrectRateQuestionBank() == null || user.getCorrectRateQuestionBank().equals("")){
                practiceErrorTopTo.setCorrectRate("0.0%");
            }else {
                practiceErrorTopTo.setCorrectRate(user.getCorrectRateQuestionBank());
            }
//  完成率
            if (user.getQuestionBankCompleteRate() == null || user.getQuestionBankCompleteRate().equals("")){
                practiceErrorTopTo.setCompletaRate("0.0%");
            }else {
                practiceErrorTopTo.setCompletaRate(user.getQuestionBankCompleteRate());
            }
        }
        return practiceErrorTopTo;
    }

    @Override
    public Integer getPatientIdByQuestionId(Integer objectionId) {

        Objection objection = objectionService.getById(objectionId);
        if (objection == null){
            throw new RuntimeException("该异议不存在");
        }

        Long patientAnalyseId = objection.getPatientAnalyseId();
        if (patientAnalyseId == null){
            throw new RuntimeException("该错题对应病例不存在");
        }

        PatientAnalyse patientAnalyse = patientAnalyseService.getById(patientAnalyseId);
        if (patientAnalyse == null){
            throw new RuntimeException("病例分析图不存在");
        }
        return patientAnalyse.getPatientId();
    }

    @Override
    public Page<PracticePage> getAllErrorQuestionOrderBy(OrderPatientUo orderPatientUo) {
        if (orderPatientUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }

        if (orderPatientUo.getOrder() == 1){
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .orderBy(true, true, convertedString)
                    .eq("is_true",false)
                    .eq("user_id",orderPatientUo.getUserId());
            Page<PracticePage> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());

            return practiceMapper.selectPage(page,wrapper);
        }else {
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .orderBy(true, false, convertedString)
                    .eq("is_true",false)
                    .eq("user_id",orderPatientUo.getUserId());
            Page<PracticePage> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());

            return practiceMapper.selectPage(page,wrapper);
        }
    }

    @Override
    public Page<PracticePage> getAllCollectQuestionOrderBy(OrderPatientUo orderPatientUo) {
        if (orderPatientUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }
        if (orderPatientUo.getOrder() == 1){
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .orderBy(true, true, convertedString)
                    .eq("is_collect",true)
                    .eq("user_id",orderPatientUo.getUserId());
            Page<PracticePage> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());

            return practiceMapper.selectPage(page,wrapper);
        }else {
            String convertedString = orderPatientUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            System.out.println(convertedString);
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .orderBy(true, false, convertedString)
                    .eq("is_collect",true)
                    .eq("user_id",orderPatientUo.getUserId());
            Page<PracticePage> page = new Page<>(orderPatientUo.getPage(), orderPatientUo.getLimit());

            return practiceMapper.selectPage(page,wrapper);
        }
    }

    @Override
    public void deleteByUserId(Long id) {
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", id);
        //对应题目已答题人数-1，并删除答题记录
        List<PracticePage> list = list(wrapper);
        for (PracticePage practicePage:list){
            Long questionId = practicePage.getQuestionId();
            Question question = questionService.getById(questionId);
            Integer answeredUser = question.getAnsweredUser();
            if (answeredUser > 0){
                answeredUser--;
                question.setAnsweredUser(answeredUser);
            }
            if (practicePage.getIsTrue() == true){
                Integer correctAnswered = question.getCorrectAnswered();
                if (correctAnswered > 0){
                    correctAnswered--;
                    question.setCorrectAnswered(correctAnswered);
                }
            }
            removeById(practicePage.getId());
        //重新计算正确率
            String correctRateStr = practicePageService.setCorrectByQuestionId(questionId);
            if (correctRateStr != null && correctRateStr.endsWith("%")) {
                correctRateStr = correctRateStr.replace("%", "");
            }
            question.setCorrectRate(correctRateStr);
            questionService.updateById(question);

        }
    }

    @Override
    public void deletePracticeByIds(Long[] ids) {
        for (Long userId : ids){
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .eq("user_id", userId);
            //对应题目已答题人数-1，并删除答题记录
            List<PracticePage> list = list(wrapper);

            for (PracticePage practicePage:list){
            //修改题目回答正确人数以及答题人数
                Long questionId = practicePage.getQuestionId();
                Question question = questionService.getById(questionId);
                Integer answeredUser = question.getAnsweredUser();
                if (answeredUser > 0){
                    answeredUser--;
                    question.setAnsweredUser(answeredUser);
                }
                if (practicePage.getIsTrue() == true){
                    Integer correctAnswered = question.getCorrectAnswered();
                    if (correctAnswered > 0){
                        correctAnswered--;
                        question.setCorrectAnswered(correctAnswered);
                    }
                }
                removeById(practicePage.getId());
            //重新计算正确率
                String correctRateStr = practicePageService.setCorrectByQuestionId(questionId);
                if (correctRateStr != null && correctRateStr.endsWith("%")) {
                    correctRateStr = correctRateStr.replace("%", "");
                }
                question.setCorrectRate(correctRateStr);
                questionService.updateById(question);
            }
        }
    }

    @Override
    public String setCorrectByQuestionId(Long questionId) {
        Question question = questionService.getById(questionId);
        List<PracticePage> practicePageList = list(Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getQuestionId, questionId));
        if (practicePageList == null || practicePageList.size() == 0){
            return "0.0%";
        }
        double answerUser = 0.0;
        double correctUser = 0.0;
        for (PracticePage practicePage:practicePageList){
            answerUser++;
            if (practicePage.getIsTrue() == true){
                correctUser++;
            }
        }
        BigDecimal correctUserBigDecimal = new BigDecimal(correctUser);
        BigDecimal answerUserBigDecimal = new BigDecimal(answerUser);

        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
            BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

            double rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            String rate = rateNum + "%";
            return rate;
            // 使用 rateNum 进行后续逻辑
        } else {
            // 处理除数为0的情况
            throw new RuntimeException("除0异常");
        }
    }

    @Override
    public List<PracticePage> getErrorsByUserIdList(Integer userId) {
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("is_true", false);
        return list(wrapper);
    }

    @Override
    public List<PracticePage> getCollectsByUserIdList(Integer userId) {
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("is_collect", true);
        return list(wrapper);
    }

    @Override
    public QuestionAndPracticeUo redoErrorQuestion(Integer questionId,Integer userId) {
        //得到当前题目的详细信息
        QuestionAndPracticeUo questionAndPracticeUo = new QuestionAndPracticeUo();
        LambdaQueryWrapper<PracticePage> wrapper = Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getQuestionId, questionId)
                .eq(PracticePage::getUserId, userId);
        PracticePage practicePage = practicePageService.getOne(wrapper);
        Question question = questionService.getById(questionId);
        BeanUtils.copyProperties(question,questionAndPracticeUo);
        questionAndPracticeUo.setIsCollect(practicePage.getIsCollect());
        questionAndPracticeUo.setIsTrue(practicePage.getIsTrue());
        //是否已提出异议
        LambdaQueryWrapper<Objection> queryWrapper = Wrappers.lambdaQuery(Objection.class)
                .eq(Objection::getUserId, userId)
                .eq(Objection::getQuestionId,questionId);
        Objection objection = objectionService.getOne(queryWrapper);
        if (objection == null){
            questionAndPracticeUo.setIsObjection(false);
        }else {
            questionAndPracticeUo.setIsObjection(true);
            String reply = "";
            if (objection.getProcessingResult() == null){
                reply = "未处理";
            }else if (objection.getProcessingResult() == 0){
                reply = objection.getAnswer() + "%" + "已驳回" + "%" + objection.getProcessingReply();
            }else {
                reply = objection.getObjectionAnswer() + "%" + "已采纳" + "%" + objection.getProcessingReply();
            }
            questionAndPracticeUo.setReply(reply);
        }
        return questionAndPracticeUo;
    }

    @Override
    public QuestionAndPracticeUo redoErrorInfo(Long questionId, Integer answerId, Integer questionSetId,Integer userId) {
        QuestionAndPracticeUo questionAndPracticeUo = new QuestionAndPracticeUo();
        PracticePage practicePage = getByUserIdAndQuestionId(userId,questionId);
        //得到题目的正确人数/回答人数 便于答题正确之后重新计算正确率，完成率
        Question question = questionService.getById(questionId);
        if (question.getAnsweredUser() == null){
            question.setAnsweredUser(0);
        }
        if (question.getCorrectAnswered() == null){
            question.setCorrectAnswered(0);
        }
        double answerUser = question.getAnsweredUser();
        double correctUser = question.getCorrectAnswered();
        //得到用户回答答案
        String answer = "";
        if (answerId == 0){
            answer = AnswerType.NON_STANDARD_ASPECT.getMsg();
        }else {
            answer = "切面"+answerId;
        }
        //  回答正确
        if (question.getAnswer() != null && question.getAnswer().equals(answer)){
        //答题记录已经存在，修改正确状态和回答内容   题库回答人数 正确人数 正确率  完成率
            if (practicePage != null && (practicePage.getIsTrue() == null || practicePage.getIsTrue() == false)){
                practicePage.setIsTrue(true);
                practicePage.setUserAnswer(answer);
                practicePageService.updateById(practicePage);
                correctUser++;
                BigDecimal correctUserBigDecimal = new BigDecimal(correctUser);
                BigDecimal answerUserBigDecimal = new BigDecimal(answerUser);
                if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                    BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
                    BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));
                    double rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    question.setCorrectAnswered((int) Math.round(correctUser));
                    question.setCorrectRate(rateNum + "%");
                    questionService.updateById(question);
        //更新套题正确率等
                    questionSetService.setSomeThing(Long.valueOf(questionSetId));
                } else {
                    // 处理除数为0的情况
                    throw new RuntimeException("除0异常");
                }
            }
        }else {
        //回答错误，答题记录存在 更新修改时间
            if (practicePage != null && practicePage.getIsTrue()==false){
                updateById(practicePage);
            }
        }
        BeanUtils.copyProperties(question,questionAndPracticeUo);
        //是否已提出异议
        LambdaQueryWrapper<Objection> queryWrapper = Wrappers.lambdaQuery(Objection.class)
                .eq(Objection::getUserId, userId)
                .eq(Objection::getQuestionId,questionId);
        Objection objection = objectionService.getOne(queryWrapper);
        if (objection == null){
            questionAndPracticeUo.setIsObjection(false);
        }else {
            questionAndPracticeUo.setIsObjection(true);
            String reply = "";
            if (objection.getProcessingResult() == null){
                reply = "未处理";
            }else if (objection.getProcessingResult() == 0){
                reply = objection.getAnswer() + "%" + "已驳回" + "%" + objection.getProcessingReply();
            }else {
                reply = objection.getObjectionAnswer() + "%" + "已采纳" + "%" + objection.getProcessingReply();
            }
            questionAndPracticeUo.setReply(reply);
        }
        return questionAndPracticeUo;
    }

    @Override
    public PracticePage getByUserIdAndQuestionId(Integer collectId, Integer userId) {
        LambdaQueryWrapper<PracticePage> wrapper = Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getQuestionId, collectId)
                .eq(PracticePage::getUserId,userId);
        PracticePage practicePage = getOne(wrapper);
        return practicePage;
    }

    private PracticePage getByUserIdAndQuestionId(Integer userId, Long questionId) {
        LambdaQueryWrapper<PracticePage> wrapper = Wrappers.lambdaQuery(PracticePage.class)
                .eq(PracticePage::getUserId, userId)
                .eq(PracticePage::getQuestionId, questionId);
        PracticePage practicePage = getOne(wrapper);
        return practicePage;
    }
}