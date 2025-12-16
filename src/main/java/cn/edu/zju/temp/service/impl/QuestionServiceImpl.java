package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.config.LocalEnv;
import cn.edu.zju.temp.entity.*;
import cn.edu.zju.temp.entity.so.ObjectionSo;
import cn.edu.zju.temp.entity.so.QuestionSo;
import cn.edu.zju.temp.entity.to.QuestionTo;
import cn.edu.zju.temp.entity.to.QuestionTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PatientBankUo;
import cn.edu.zju.temp.entity.uo.PracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionAndPracticeUo;
import cn.edu.zju.temp.entity.vo.QuestionVo;
import cn.edu.zju.temp.enums.AnswerType;
import cn.edu.zju.temp.enums.ObjectionType;
import cn.edu.zju.temp.enums.QuestionType;
import cn.edu.zju.temp.mapper.QuestionMapper;
import cn.edu.zju.temp.service.*;
import cn.edu.zju.temp.util.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/4/30 16:20
 * @version: 1.0
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private CommonUpload commonUpload;

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IPracticePageService practicePageService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private LocalEnv localEnv;

    @Autowired
    private IPatientAnalyseService patientAnalyseService;

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IQuestionSetService questionSetService;

    @Override
    public Page<Question> getBankList(Integer page,Integer limit) {
        Page<Question> pageQuestion = new Page<>(page, limit);
        Page<Question> pageRecords = questionMapper.selectPage(pageQuestion, null);
        return pageRecords;
    }

    @Override
    public Question addQuestion(QuestionSo questionSo) {
//        int count = count(new QueryWrapper<Question>()
//                .eq("name", questionSo.getName())
//                .eq("answer", questionSo.getAnswer()));
//        if (count > 0){
//            throw new RuntimeException("增加题目失败");
//        }

        if (questionSo == null || questionSo.getName() == null){
            throw new RuntimeException("未上传图片");
        }

        MultipartFile file = questionSo.getName();
        String filename = file.getOriginalFilename();
        String substring = filename.substring(filename.lastIndexOf("."));
        if (substring.equals(".jpg") || substring.equals(".png")){

            File fileQuestion = new File(localEnv.getStaticPath() + localEnv.getImgPath() + UUID.randomUUID().toString().substring(0,8) + ".jpg");
            String absolutePath = fileQuestion.getAbsolutePath();
            int length = localEnv.getStaticPath().length();
            String realPath = absolutePath.substring(length - 1);
            System.out.println("absolutePath" + "   "+absolutePath);
            System.out.println("realPath"+"   "+realPath);
            try {
                file.transferTo(fileQuestion);
            } catch (IOException e) {
                throw new RuntimeException("文件下载失败，请重新上传");
            }
            String answerTrue = "切面";
            if (questionSo.getAnswer().equals("0")){
                answerTrue = "非标准切面";
            }else {
                answerTrue += questionSo.getAnswer();
            }
            Question question = new Question(realPath, answerTrue,false);
            question.setTopicSource(QuestionType.BANK);
            question.setQuestionSetId(questionSo.getQuestionSetId());
            save(question);
//  也要修改用户正确率与完成率
            List<User> userList = userService.list();
            for (User user:userList){
                setUserCorrect((long)user.getId());
            }
//计算套题正确率，正确人数，回答人数，题目数量
            questionSetService.setSomeThing(questionSo.getQuestionSetId());
            return question;
        }else {
            throw new RuntimeException("上传的文件非图片格式");
        }
    }

    @Override
    public Boolean deleteOneQuestionById(Long id) {
        Question question = getById(id);
        boolean remove = removeById(id);
        if (remove){
//  删除用户练习记录
            QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                    .eq("question_id", id);
            List<PracticePage> practicePages = practicePageService.list(wrapper);
            if (practicePages == null || practicePages.size() == 0){
            }else {
                for (PracticePage practicePage:practicePages){
                    practicePageService.removeById(practicePage.getId());
                }
            }
//  修改用户正确率与完成率
            List<User> userList = userService.list();
            for (User user:userList){
                setUserCorrect((long)user.getId());
            }
//计算套题正确率，正确人数，回答人数，题目数量
            questionSetService.setSomeThing(question.getQuestionSetId());
        }
        return remove;
    }

    @Override
    public Boolean deleteQuestionMoreByIds(Long[] ids) {
        Long questionSetId = null;
        if (ids.length > 0){
            Long questionId = ids[0];
            if (questionSetId == null){
                Question question = getById(questionId);
                questionSetId = question.getQuestionSetId();
            }
        }
        boolean remove = removeByIds(Arrays.asList(ids));
        if (remove){
//  删除用户练习记录
            for (Long questionId:ids){
                QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                        .eq("question_id", questionId);
                List<PracticePage> practicePages = practicePageService.list(wrapper);
                if (practicePages == null || practicePages.size() == 0){
                }else {
                    for (PracticePage practicePage:practicePages){
                        practicePageService.removeById(practicePage.getId());
                    }
                }
            }
//  修改用户正确率与完成率
            List<User> userList = userService.list();
            for (User user:userList){
                setUserCorrect((long)user.getId());
            }
//计算套题正确率，正确人数，回答人数，题目数量
            if (questionSetId != null){
                questionSetService.setSomeThing(questionSetId);
            }
        }
        return remove;
    }

    @Override
    public Boolean updateOneByQuestionVo(QuestionVo questionVo) {

        Question question = new Question(questionVo.getId(),questionVo.getAnswer());
        boolean isUpdated = updateById(question);
        return isUpdated;
    }

    @Override
    public Question getOneQuestionById(Long id) {
        return getById(id);
    }

    @Override
    public Boolean answerQuestion(Long questionId, Integer answerId,Long userId,Long questionSetId) {

        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", userId)
                .eq("question_id", questionId);
        PracticePage practicePage = practicePageService.getOne(wrapper);

        Question question = getOneQuestionById(questionId);
        if (question.getAnsweredUser() == null){
            question.setAnsweredUser(0);
        }
        if (question.getCorrectAnswered() == null){
            question.setCorrectAnswered(0);
        }
        double answerUser = question.getAnsweredUser();
        double correctUser = question.getCorrectAnswered();

        String answer = "";
        if (answerId == 0){
            answer = AnswerType.NON_STANDARD_ASPECT.getMsg();
        }else {
            answer = "切面"+answerId;
        }
//  回答正确
        if (question.getAnswer() != null && question.getAnswer().equals(answer)){
//如果答题记录已经存在，就修改正确状态和回答内容   题库回答人数 正确人数 正确率  完成率
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
                    return true;
                    // 使用 rateNum 进行后续逻辑
                } else {
                    // 处理除数为0的情况
                    throw new RuntimeException("除0异常");
                }

            }else if(practicePage == null){  //答题记录不存在   回答正确  添加进题库 以便计算正确率 套题id
                Boolean addTrue = practicePageService.addTrueQuestion(questionId,userId,answer,questionSetId);
                if (!addTrue){
                    throw new RuntimeException("添加练习题库失败");
                }
                correctUser++;
                answerUser++;

                BigDecimal correctUserBigDecimal = new BigDecimal(correctUser);
                BigDecimal answerUserBigDecimal = new BigDecimal(answerUser);

                if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                    BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
                    BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

                    double rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();

                    question.setCorrectAnswered((int) Math.round(correctUser));
                    question.setAnsweredUser((int) Math.round(answerUser));
                    question.setCorrectRate(rateNum + "%");

                    questionService.updateById(question);

                    return true;
                    // 使用 rateNum 进行后续逻辑
                } else {
                    // 处理除数为0的情况
                    throw new RuntimeException("除0异常");
                }
            }
        }else {  //回答错误
            if (practicePage == null){  //答题记录不存在  回答错误  添加进题库 标注为错误题目
                Boolean addError = practicePageService.addErrorQuestion(questionId, userId, answer,questionSetId);
                answerUser++;

                BigDecimal correctUserBigDecimalT = new BigDecimal(correctUser);
                BigDecimal answerUserBigDecimalT = new BigDecimal(answerUser);

                if (answerUserBigDecimalT.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                    BigDecimal completeRateNumBigDecimalT = correctUserBigDecimalT.divide(answerUserBigDecimalT, 2, RoundingMode.HALF_UP);
                    BigDecimal rateNumBigDecimalT = completeRateNumBigDecimalT.multiply(new BigDecimal(100));

                    double rateNum = rateNumBigDecimalT.setScale(2, RoundingMode.HALF_UP).doubleValue();

                    question.setAnsweredUser((int) Math.round(answerUser));
                    question.setCorrectRate(rateNum + "%");
                    questionService.updateById(question);
                }
                if (!addError){
                    throw new RuntimeException("添加错题库失败");
                }
                return true;
            }else if (practicePage != null && practicePage.getIsTrue()==false){  //回答错误，但答题记录存在，(可能已收藏)
                practicePage.setIsTrue(false);
                practicePage.setUserAnswer(answer);
                practicePageService.updateById(practicePage);

                BigDecimal correctUserBigDecimalT = new BigDecimal(correctUser);
                BigDecimal answerUserBigDecimalT = new BigDecimal(answerUser);
                if (answerUserBigDecimalT.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
                    BigDecimal completeRateNumBigDecimalT = correctUserBigDecimalT.divide(answerUserBigDecimalT, 2, RoundingMode.HALF_UP);
                    BigDecimal rateNumBigDecimalT = completeRateNumBigDecimalT.multiply(new BigDecimal(100));

                    double rateNum = rateNumBigDecimalT.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    question.setCorrectAnswered((int) Math.round(correctUser));
                    question.setCorrectRate(rateNum + "%");
                    questionService.updateById(question);
                }
                return true;
            }
        }
        return true;
    }

    /**
     * 添加收藏题目
     * @param questionId
     * @param answerId
     * @param userId
     * @return
     */
    @Override
    public Boolean collectQuestion(Long questionId, Integer answerId, Long userId,Long questionSetId) {
        Boolean isCollect = practicePageService.addCollectQuestion(questionId,userId,answerId,questionSetId);
        return isCollect;
    }

    @Override
    public Page<Question> getBankListByPage(Integer page, Integer limit) {
        Page<Question> page1 = new Page<>(page, limit);
        Page<Question> questionPage = questionMapper.selectPage(page1, null);
        return questionPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addByZip(MultipartFile file,Long questionSetId) throws IOException {

        if (file == null || file.getSize() <= 0){
            throw new RuntimeException("上传文件为空");
        }
        String fileName = file.getOriginalFilename();
        if (!"zip".equals(fileName.substring(fileName.lastIndexOf(".")+1))){
            throw new RuntimeException("上传的非zip文件");
        }

//  将zip题库数据导入数据库
        return addQuestionByZip(file,questionSetId);
    }

    private boolean addQuestionByZip(MultipartFile file,Long questionSetId) throws IOException {
        //  存放图片的url
        HashMap<String, String> urlMap = new HashMap<>();

//  将zip文件保存下来
        ZipEntry zipEntry = null;
        List<Question> questions = null;
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
                    questions = getListByxlsx(multipartFile);
                }else if (substring != null && (substring.equals("jpg") || substring.equals("png"))){
//  是jpg/png文件，存放起来，最后将lists集合的题目名称附上url地址值
//                    String url = aliOSSUtils.upload(multipartFile);
                    urlMap.put(realFilename,realPath);
                }else {
                    throw new RuntimeException("zip文件格式错误，导入失败");
                }
            }

            for (Question question:questions){
                String name = question.getName();
                String url = urlMap.get(name);
                question.setQuestionSetId(questionSetId);
                if (url == null || url.equals("")){
                    throw new RuntimeException("导入失败，图片不存在");
                }
                question.setName(url);
            }
//  集合保存
            saveBatch(questions);
//  更新所有用户题库完成率与正确率
            List<User> list = userService.list();
            for (User user:list){
                setUserCorrect((long)user.getId());
            }
//计算套题正确率，正确人数，回答人数，题目数量
            questionSetService.setSomeThing(questionSetId);
        } catch (IOException e) {
            throw new RuntimeException("zip文件上传格式错误");
        }
        return true;
    }

    @Override
    public Boolean importPatientAnalyse(List<PatientBankUo> patientBankUos) {
        if (patientBankUos == null || patientBankUos.size() <= 0){
            throw new RuntimeException("没有选择任何一个图片，导入题库失败");
        }
        ArrayList<Question> questions = new ArrayList<>();
        for (PatientBankUo patientBankUo:patientBankUos){
            Question question = new Question(patientBankUo.getName(),patientBankUo.getAnswer(),false);
            question.setPatientAnalyseId(patientBankUo.getPatientAnalyseId());
            question.setTopicSource(QuestionType.CASE);
            questions.add(question);
        }
        boolean isTrue = questionService.saveBatch(questions);
        return isTrue;
    }

    @Override
    public Boolean deleteErrorOne(Integer errorId) {
        PracticePage practicePage = practicePageService.getById(errorId);
        //确定是错题
        if (practicePage.getIsTrue() != null && practicePage.getIsTrue() == false){
            boolean remove = practicePageService.removeById(errorId);
        //题库答题人数-1
            Long questionId = practicePage.getQuestionId();
            Question question = questionService.getById(questionId);
            Integer answeredUser = question.getAnsweredUser();
            if (answeredUser > 0){
                answeredUser--;
                question.setAnsweredUser(answeredUser);
        //重新计算题目正确率
                String correctrate = getQuestionRate(question);
                if (correctrate != null && correctrate.endsWith("%")) {
                    correctrate = correctrate.replace("%", "");
                }
                question.setCorrectRate(correctrate);
        //计算套题正确率，正确人数，回答人数，题目数量
                questionService.updateById(question);
                questionSetService.setSomeThing(question.getQuestionSetId());
            }
            return remove;
        }
        return false;
    }

    private String getQuestionRate(Question question) {
        Integer answeredUser = question.getAnsweredUser();
        Integer correctAnswered = question.getCorrectAnswered();
        BigDecimal correctUserBigDecimal = new BigDecimal(correctAnswered);
        BigDecimal answerUserBigDecimal = new BigDecimal(answeredUser);
        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
            BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));
            double rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            return rateNum+"%";
        }else {
            return "0.0%";
        }
    }

    @Override
    public Boolean deleteCollectOne(Integer questionId,Integer userId) {
        PracticePage practicePage = practicePageService.getByUserIdAndQuestionId(questionId,userId);
        if (practicePage.getIsCollect() != null && practicePage.getIsCollect() == true){
            practicePage.setIsCollect(false);
            boolean remove = practicePageService.updateById(practicePage);
            return remove;
        }
        return false;
    }

    @Override
    public Boolean deleteErrorMore(Long[] errorids) {
        for (Long errorid : errorids){
            PracticePage practicePage = practicePageService.getById(errorid);
            if (practicePage.getIsTrue() != null && practicePage.getIsTrue() == false){
    //题库答题人数-1
                Long questionId = practicePage.getQuestionId();
                Question question = questionService.getById(questionId);
                Integer answeredUser = question.getAnsweredUser();
                if (answeredUser > 0){
                    answeredUser--;
                    question.setAnsweredUser(answeredUser);
                    questionService.updateById(question);
                }
            }else {
                throw new RuntimeException("选中的已经被删除，请刷新");
            }
        }
        boolean remove = practicePageService.removeByIds(Arrays.asList(errorids));
        return remove;
    }

    @Override
    public Boolean deleteCollectMore(Long[] collectids) {
        for (Long collectId : collectids){
            PracticePage practicePage = practicePageService.getById(collectId);
            if (practicePage.getIsCollect() != null && practicePage.getIsCollect() == true){
                practicePage.setIsCollect(false);
                practicePageService.updateById(practicePage);
            }else {
                throw new RuntimeException("选中的已经被删除，请刷新");
            }
        }
        boolean remove = practicePageService.removeByIds(Arrays.asList(collectids));
        return remove;
    }

    @Override
    @Transactional
    public Boolean deleteObjectionOne(Integer objectionId) {
        Objection objection = objectionService.getById(objectionId);
        if (objection != null && objection.getType() != null && objection.getType() == ObjectionType.CASE) {
            boolean remove = objectionService.removeById(objectionId);
    //根据异议id 找到病例异议数-1
            PatientAnalyse patientAnalyse = patientAnalyseService.getById(objection.getPatientAnalyseId());
            Patient patient = patientService.getById(patientAnalyse.getPatientId());
            Integer objectionNum = patient.getObjectionNum();
            if (objectionNum > 0){
                objectionNum--;
                patient.setObjectionNum(objectionNum);
                patientService.updateById(patient);
            }
            return remove;
        }else if (objection != null && objection.getType() != null && objection.getType() == ObjectionType.BANK){
    //题库异议 就将练习记录的是否异议改为false 然后删除该异议
            Objection objectionOne = objectionService.getById(objectionId);
            Long userId = objectionOne.getUserId();
            Long questionId = objectionOne.getQuestionId();
            PracticePage practicePage = practicePageService.getByUserIdAndQuestionId(Math.toIntExact(questionId), Math.toIntExact(userId));
            practicePage.setIsObjection(false);
            practicePageService.updateById(practicePage);
            boolean removeById = objectionService.removeById(objectionId);
            return removeById;
        }else {
            throw new RuntimeException("删除异议不存在");
        }
    }

    @Override
    @Transactional
    public Boolean deleteObjectionMore(Long[] objectionIds) {
        for (Long objectionId : objectionIds) {
            Objection objection = objectionService.getById(objectionId);
            if (objection != null && objection.getType() != null && (objection.getType() == ObjectionType.BANK || objection.getType() == ObjectionType.CASE)) {
                //根据异议id 找到病例异议数-1
                PatientAnalyse patientAnalyse = patientAnalyseService.getById(objection.getPatientAnalyseId());
                Patient patient = patientService.getById(patientAnalyse.getPatientId());
                Integer objectionNum = patient.getObjectionNum();
                if (objectionNum > 0){
                    objectionNum--;
                    patient.setObjectionNum(objectionNum);
                    patientService.updateById(patient);
                }
            }else {
                throw new RuntimeException("删除的非题库异议或病例异议");
            }
        }
        boolean remove = practicePageService.removeByIds(Arrays.asList(objectionIds));
        return remove;
    }

    @Override
    public Boolean addQuestionByAnalyseIds(Long[] ids,Long questionSetId) {
        if (ids == null || ids.length <= 0){
            return false;
        }
        ArrayList<Question> questions = new ArrayList<>();
        for (Long patientAnalyseId: ids){
            PatientAnalyse patientAnalyse = patientAnalyseService.getById(patientAnalyseId);
            if (patientAnalyse == null){
                throw new RuntimeException("导入失败，有为空的病例");
            }
            Question question = new Question(patientAnalyse.getOriginalImg(),patientAnalyse.getAnalyseAnswer(),false);
            question.setTopicSource(QuestionType.CASE);
            question.setPatientAnalyseId(patientAnalyseId);
            question.setQuestionSetId(questionSetId);

            questions.add(question);
        }
        boolean batch = questionService.saveBatch(questions);

//  还要修改所有用户题库正确率与完成率
        List<User> users = userService.list();
        for (User user:users){
            setUserCorrect((long) user.getId());
        }
//计算套题正确率，正确人数，回答人数，题目数量
        questionSetService.setSomeThing(questionSetId);
        return batch;
    }

    @Override
    public void setUserCorrect(Long userId) {
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", userId);

        List<PracticePage> list = practicePageService.list(wrapper);
        if (list == null){
            return;
        }

        User user = userService.getById(userId);
        double correctQuestion = 0;
        double errorQuestion = 0;
        double total = 0;
        for (PracticePage practicePage: list){
            if (practicePage.getIsTrue() == null){

            }else if (practicePage.getIsTrue()){
                correctQuestion ++;
            }else {
                errorQuestion++;
            }
        }
        total = correctQuestion+errorQuestion;

        BigDecimal correctUserBigDecimal = new BigDecimal(correctQuestion);
        BigDecimal answerUserBigDecimal = new BigDecimal(total);

        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
            BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

            double rateNum = rateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
//  用户题库正确率
            user.setCorrectRateQuestionBank(rateNum + "%");
        }

//  设置题库正确率


//  题库总数
        double countQuestion = questionService.count();

//  已答总数
        double countAnswered = list.size();
////        double completeRate = countAnswered/countQuestion;
//        DecimalFormat dft = new DecimalFormat("#.##");
//        String formattedRate = dft.format(completeRate);
//        double rateUser = Double.parseDouble(formattedRate) * 100;

        BigDecimal correctUserBigDecimalDouble = new BigDecimal(countAnswered);
        BigDecimal answerUserBigDecimalDouble = new BigDecimal(countQuestion);

        if (answerUserBigDecimalDouble.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimalDouble = correctUserBigDecimalDouble.divide(answerUserBigDecimalDouble, 2, RoundingMode.HALF_UP);
            BigDecimal rateNumBigDecimalDouble = completeRateNumBigDecimalDouble.multiply(new BigDecimal(100));

            double rateNumDouble = rateNumBigDecimalDouble.setScale(2, RoundingMode.HALF_UP).doubleValue();

            user.setQuestionBankCompleteRate(rateNumDouble + "%" + "(" + Math.round(countAnswered) + "/" + Math.round(countQuestion) + ")");
            System.out.println("修改用户成功率成功");
        }

        userService.updateById(user);
    }

    @Override
    public QuestionTopTo getTopInfo() {

//未处理异议数
        QuestionTopTo questionTopTo = new QuestionTopTo();

        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("processing_state", "0");
        List<Objection> listUnResolve = objectionService.list(wrapper);
        List<Objection> list = objectionService.list();

        Integer total = list.size();
        Integer unTotal = listUnResolve.size();

        questionTopTo.setUnResolveNums(unTotal + "/" + total);

//  题目数
        List<Question> questions = list();
        questionTopTo.setQuestionNum(questions.size());

//  题库正确率
        List<Question> questionList = questionService.list();
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
//                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

                double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                questionTopTo.setCorrectRate(rateNum + "%");
            }
        }

//  题库完成率  得到所有用户完成率  所有用户数量  相除
        double userCount = 0.0;
        double totalCompleteRate = 0.0;

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .ne("role", "admin")
                .ne("role","superadmin");
        List<User> userList = userService.list(userQueryWrapper);
        userCount = userList.size();
        for (User user:userList){
            String string = user.getQuestionBankCompleteRate();
            if (string == null || string.equals("")){
                continue;
            }
            int indexOfPercent = string.indexOf('%');
            double doubleValue = Double.parseDouble(string.substring(0, indexOfPercent));
            System.out.println(doubleValue);
            totalCompleteRate += doubleValue;
        }

        BigDecimal correctUserBigDecimal = new BigDecimal(userCount);
        BigDecimal answerUserBigDecimal = new BigDecimal(totalCompleteRate);

        if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimal = answerUserBigDecimal.divide(correctUserBigDecimal, 2, RoundingMode.HALF_UP);
//                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));

            double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            questionTopTo.setCompleteRate(rateNum + "%");
        }else {
            questionTopTo.setCompleteRate("0.0%");
        }
        return questionTopTo;
    }

    @Override
    public Page<Question> getAllQuestionOrderBy(OrderUo orderUo) {

        if (orderUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }

        if (orderUo.getOrder() == 1){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<Question> wrapper = new QueryWrapper<Question>()
                    .orderBy(true, true, convertedString)
                    .eq("question_set_id",orderUo.getQuestionSetId());
            Page<Question> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return questionMapper.selectPage(page,wrapper);
        }else if (orderUo.getOrder() == 0){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<Question> wrapper = new QueryWrapper<Question>()
                    .orderBy(true, false, convertedString)
                    .eq("question_set_id",orderUo.getQuestionSetId());
            Page<Question> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return questionMapper.selectPage(page,wrapper);
        }else {
            return null;
        }
    }

    @Override
    public Page<QuestionAndPracticeUo> getQuestionDetail(PracticeUo practiceUo) {
        LambdaQueryWrapper<Question> queryWrapper = Wrappers.lambdaQuery(Question.class)
                .eq(Question::getQuestionSetId, practiceUo.getQuestionSetId());
        //得到对应套题所有题目
        List<Question> questionList = questionService.list(queryWrapper);
        QueryWrapper<PracticePage> wrapper = new QueryWrapper<PracticePage>()
                .eq("user_id", practiceUo.getUserId());
        List<PracticePage> practicePageList = practicePageService.list(wrapper);
        //将所有用户做题记录存到hashmap中
        HashMap<Long, PracticePage> practicePageHashMap = new HashMap<>();
        if (practicePageList == null || practicePageList.size() == 0){
            practicePageHashMap = null;
        }else {
            for (PracticePage practicePage:practicePageList){
                practicePageHashMap.put(practicePage.getQuestionId(),practicePage);
            }
        }
        //将所有用户的题库异议存到hashmap中
        HashMap<Long, Objection> objectionHashMap = new HashMap<>();
        //得到当前用户的所有题库异议
        List<Objection> objections = objectionService.list(Wrappers.lambdaQuery(Objection.class)
                .eq(Objection::getUserId, practiceUo.getUserId())
                .isNotNull(Objection::getQuestionId));
        if (objections == null || objections.size() == 0){
            objectionHashMap = null;
        }else {
            for (Objection objection : objections){
                objectionHashMap.put(objection.getQuestionId(),objection);
            }
        }
        //做题记录不存在
        if (practicePageList == null || practicePageList.size() == 0){
            List<QuestionAndPracticeUo> questionAndPracticeUos = new ArrayList<>();

            for (Question question:questionList){
                QuestionAndPracticeUo questionAndPracticeUo = new QuestionAndPracticeUo();
                questionAndPracticeUo.setIsCollect(false);
                BeanUtils.copyProperties(question,questionAndPracticeUo);
        //是否已提出异议
                if (objectionHashMap == null){
                }else {
                    Objection objection = objectionHashMap.get(question.getId());
                    if (objection == null){
                        questionAndPracticeUo.setIsObjection(false);
                        questionAndPracticeUo.setIsCollect(false);
                    }else {
                        questionAndPracticeUo.setIsObjection(true);
                        questionAndPracticeUo.setIsCollect(false);
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
                }
                questionAndPracticeUos.add(questionAndPracticeUo);
            }

            List<QuestionAndPracticeUo> andPracticeUos = null;
            int start = (int) ((practiceUo.getPage() - 1) * practiceUo.getLimit());
            int end = (int) ((practiceUo.getPage() * practiceUo.getLimit()));
            if (end > questionAndPracticeUos.size()){
                end = questionAndPracticeUos.size();
            }

            if (start >= end){
                andPracticeUos = null;
            }else {
                andPracticeUos = questionAndPracticeUos.subList(start, end);
            }
            Page<QuestionAndPracticeUo> page = new Page<>(practiceUo.getPage(), practiceUo.getLimit());
            page.setRecords(andPracticeUos);
            page.setTotal(questionAndPracticeUos.size());
            return page;
        }else {
        //存在做题记录
            System.out.println(practicePageList.size());
            List<QuestionAndPracticeUo> questionAndPracticeUos = new ArrayList<>();
            for (Question question:questionList){
                QuestionAndPracticeUo questionAndPracticeUo = new QuestionAndPracticeUo();
                BeanUtils.copyProperties(question,questionAndPracticeUo);

                PracticePage practicePage = practicePageHashMap.get(question.getId());
                if (practicePage == null){
                }else {
                    questionAndPracticeUo.setIsCollect(practicePage.getIsCollect());
                    questionAndPracticeUo.setIsObjection(practicePage.getIsObjection());
                    questionAndPracticeUo.setIsTrue(practicePage.getIsTrue());
                    questionAndPracticeUo.setUserAnswer(practicePage.getUserAnswer());
                }

                //是否已提出异议
                if (objectionHashMap == null){
                }else {
                    Objection objection = objectionHashMap.get(question.getId());
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
                }

                questionAndPracticeUos.add(questionAndPracticeUo);
            }
            List<QuestionAndPracticeUo> andPracticeUos = null;
            int start = (int) ((practiceUo.getPage() - 1) * practiceUo.getLimit());
            int end = (int) ((practiceUo.getPage() * practiceUo.getLimit()));
            if (end > questionAndPracticeUos.size()){
                end = questionAndPracticeUos.size();
            }

            if (start >= end){
                andPracticeUos = null;
            }else {
                andPracticeUos = questionAndPracticeUos.subList(start, end);
            }

            Page<QuestionAndPracticeUo> page = new Page<>(practiceUo.getPage(), practiceUo.getLimit());
            page.setRecords(andPracticeUos);
            page.setTotal(questionAndPracticeUos.size());
            return page;
        }
    }

    private List<Question> getListByxlsx(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // 获取文件名
//  得到 .jpg/.png/.xlsx
        String substring = fileName.substring(fileName.lastIndexOf("."));
        InputStream is = null;
        try{
            is = file.getInputStream();
            List<Map> questionmaps = getListByExcel(is,fileName);// 获取解析后的List集合
            questionmaps.remove(0);
            ArrayList<Question> questions = new ArrayList<>();
            for (Map map:questionmaps){
                Question question = new Question();
                question.setName((String) map.get("name"));
                question.setAnswer((String) map.get("answer"));
                question.setDeleted(false);
                question.setTopicSource(QuestionType.BANK);
                questions.add(question);
            }
            return questions;
        }catch (Exception e){
            throw new RuntimeException("xlsx文件格式不正确");
        }finally {
            is.close();
        }
    }

    private List<Map> getListByExcel(InputStream is, String fileName) {
            try{
                List<Map> studentList = new ExcelUtil(new QuestionTo()).AnalysisExcel(is, fileName);
                return studentList;
            }catch (Exception e){
                throw new RuntimeException("xlsx文件格式不正确");
            }
        }

    @Override
    public Boolean objectionOnQuestion(ObjectionSo objectionSo) {
        //  题目异议
        if(objectionSo.getQuestionId() != null){
            QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                    .eq("user_id", objectionSo.getUserId())
                    .eq("question_id", objectionSo.getQuestionId());
            Objection objectionServiceOne = objectionService.getOne(wrapper);
            if (objectionServiceOne != null){
                return false;
            }
        //当前题目还未提出异议
            User user = userService.getById(objectionSo.getUserId());
            Question question = questionService.getById(objectionSo.getQuestionId());
            Objection objection = new Objection(false);
            BeanUtils.copyProperties(objectionSo,objection);
            objection.setQuestionId(null);
            objection.setObjectionUser(user.getName());
            objection.setImg(question.getName());
            objection.setAnswer(question.getAnswer());
            objection.setProcessingState(0);  //未处理
            if (objectionSo.getObjectionAnswer().equals("0")){
                objection.setObjectionAnswer("非标准切面");
            }else {
                objection.setObjectionAnswer("切面"+objectionSo.getObjectionAnswer());
            }
                if (question.getPatientAnalyseId() != null && !question.getPatientAnalyseId().equals("")){
                objection.setQuestionId(question.getId());
                objection.setPatientAnalyseId(question.getPatientAnalyseId());
                objection.setType(ObjectionType.CASE);
                objection.setQuestionSetId(objectionSo.getQuestionSetId());
                objectionService.save(objection);
            //使病例的异议数+1
                Long analyseId = question.getPatientAnalyseId();
                Integer patientId = patientAnalyseService.getById(analyseId).getPatientId();
                Patient patient = patientService.getById(patientId);
                if (patient.getObjectionNum() == null){
                    patient.setObjectionNum(0);
                }
                Integer objectionNum = patient.getObjectionNum();
                objectionNum += 1;
                patient.setObjectionNum(objectionNum);
                patientService.updateById(patient);
            }else {
                objection.setType(ObjectionType.BANK);
                objection.setQuestionId(question.getId());
                objectionService.save(objection);
            }
        }else if (objectionSo.getQuestionId() == null && objectionSo.getPatientAnalyseId() != null){
            //  非题目异议
            QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                    .eq("user_id", objectionSo.getUserId())
                    .eq("patient_analyse_id", objectionSo.getPatientAnalyseId());
            Objection objectionServiceOne = objectionService.getOne(wrapper);
            if (objectionServiceOne != null){
                return false;
            }
            User user = userService.getById(objectionSo.getUserId());
            Objection objection = new Objection(false);
            PatientAnalyse patientAnalyse = patientAnalyseService.getById(objectionSo.getPatientAnalyseId());
            BeanUtils.copyProperties(objectionSo,objection);
            objection.setQuestionId(null);
            objection.setObjectionUser(user.getName());
            objection.setImg(patientAnalyse.getOriginalImg());
            objection.setAnswer(patientAnalyse.getAnalyseAnswer());
            objection.setProcessingState(0);  //未处理
            objection.setType(ObjectionType.CASE);
            if (objectionSo.getObjectionAnswer().equals("0")){
                objection.setObjectionAnswer("非标准切面");
            }else {
                objection.setObjectionAnswer("切面"+objectionSo.getObjectionAnswer());
            }
            objectionService.save(objection);
            //使病例的异议数+1
            Long analyseIds = objectionSo.getPatientAnalyseId();
            Integer patientId = patientAnalyseService.getById(analyseIds).getPatientId();

            Patient patient = patientService.getById(patientId);
            if (patient.getObjectionNum() == null){
                patient.setObjectionNum(0);
            }
            Integer objectionNum = patient.getObjectionNum();
            objectionNum += 1;
            patient.setObjectionNum(objectionNum);
            patientService.updateById(patient);
        }
        //练习记录修改为已提出异议
        Long userId = objectionSo.getUserId();
        Long questionId = objectionSo.getQuestionId();
        if (questionId == null){
            //是病例异议 练习记录没有
        }else {
            PracticePage practicePage = practicePageService.getByUserIdAndQuestionId(Math.toIntExact(questionId),Math.toIntExact(userId));
            practicePage.setIsObjection(true);
            practicePageService.updateById(practicePage);
        }
        return true;
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


}
