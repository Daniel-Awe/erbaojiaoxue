package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.auth.JwtOperator;
import cn.edu.zju.temp.entity.*;
import cn.edu.zju.temp.entity.so.UserLoginSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.PatientAnalyseTo;
import cn.edu.zju.temp.entity.to.UserTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.UserLoginUo;
import cn.edu.zju.temp.entity.uo.UserRoleUo;
import cn.edu.zju.temp.entity.vo.UserLoginVo;
import cn.edu.zju.temp.entity.vo.UserVo;
import cn.edu.zju.temp.enums.UserType;
import cn.edu.zju.temp.mapper.PatientMapper;
import cn.edu.zju.temp.mapper.UserMapper;
import cn.edu.zju.temp.service.*;
import cn.edu.zju.temp.util.Md5Encoder;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: gsq
 * @description: 操作用户页面
 * @date: 2024/4/30 16:13
 * @version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Autowired
    private JwtOperator jwtOperator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IPracticePageService practicePageService;

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IQuestionService questionService;

    @Override
    public UserLoginVo userLoginso(UserLoginSo userLoginSo) {

//  得到匹配的所有用户
//        String encode = Md5Encoder.encode(userLoginSo.getPassword());

        User user = this.getOne(new QueryWrapper<User>()
                .eq("name", userLoginSo.getUsername()));

        if (user == null){
            throw new RuntimeException("用户名或密码错误");
        }

        if (user.getIsDeleted()){
            throw new RuntimeException("用户已被删除");
        }

        boolean match = Md5Encoder.match(userLoginSo.getPassword(), user.getPassword());

        if (!match){
            throw new RuntimeException("用户名或密码错误");
        }
        HashMap<String, Object> tokenMap = new HashMap<>();

        tokenMap.put("userId",user.getId());
        tokenMap.put("username",user.getName());

        String token = jwtOperator.generateToken(tokenMap);
        user.setLastLogin(LocalDateTime.now());
        updateById(user);

        return UserLoginVo.success(user,token);
    }
    @Override
    public List<User> getAllUsers() {
        return list();
    }
    @Override
    public User addOneUser(UserLoginSo userLoginSo) {
//检验用户是否存在
        String encode = Md5Encoder.encode(userLoginSo.getPassword());
        int count = count(new QueryWrapper<User>()
                .eq("name", userLoginSo.getUsername()));

        if (count > 0){
            throw new RuntimeException("用户已存在");
        }
//密码加密，存储
        User user = new User(userLoginSo.getUsername(),encode);
        user.setRole(userLoginSo.getRole());
        user.setRealName(userLoginSo.getRealName());
        user.setGender(userLoginSo.getGender());
        user.setGrade(userLoginSo.getGrade());
        user.setSchool(userLoginSo.getSchool());
        user.setInstitute(userLoginSo.getInstitute());

        save(user);
        return user;
    }

    @Override
    public Boolean deleteOneUserById(Long id) {
        return removeById(id);
    }

//需要
    @Override
    public Boolean deleteMoreUserByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    /**
     * todo   模糊匹配查询 并分页
     * @param userVo
     * @return
     */
    @Override
    public Page<User> getUserByName(UserVo userVo,Integer userId) {

        Page<User> userPage = new Page<>(userVo.getPage(), userVo.getLimit());
        User user = getById(userId);

        if (userVo.getName() == null || userVo.getName().equals("")){
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            if (user.getRole() == UserType.SUPERADMIN){
                wrapper.ne("role",UserType.SUPERADMIN);
            } else if (user.getRole() == UserType.ADMIN) {
                wrapper.ne("role",UserType.SUPERADMIN);
                wrapper.ne("role",UserType.ADMIN);
            }
            return userMapper.selectPage(userPage, wrapper);
        }else {
            QueryWrapper<User> wrapper = new QueryWrapper<User>().like("name", userVo.getName());
            if (user.getRole() == UserType.SUPERADMIN){
                wrapper.ne("role",UserType.SUPERADMIN);
            } else if (user.getRole() == UserType.ADMIN) {
                wrapper.ne("role",UserType.SUPERADMIN);
                wrapper.ne("role",UserType.ADMIN);
            }
            return userMapper.selectPage(userPage,wrapper);
        }
    }

    @Override
    public Boolean updateOneByUser(UserLoginUo userLoginUo, Integer userId) {
        String password = userLoginUo.getPassword();
        String encodePassword = Md5Encoder.encode(password);
        User user = getById(userLoginUo.getId());
        user.setPassword(encodePassword);
        user.setName(userLoginUo.getUsername());
        boolean isUpdate = updateById(user);
        return isUpdate;
    }

    @Override
    public Page<User> getAllUsersByPage(Integer page, Integer limit) {
        Page<User> page1 = new Page<>(page, limit);
        Page<User> page2 = userMapper.selectPage(page1, null);
        return page2;
    }

    @Override
    public Page<PracticePage> getErrorPracticePage(UserSo userSo) {
        Page<PracticePage> pages = practicePageService.getErrorsByUserId(userSo);
        return pages;
    }

    @Override
    public Page<Patient> getcasesPracticePage(UserSo userSo) {
        Page<Patient> pages = patientService.getAllPatientByPageUserId(userSo);
        return pages;
    }

    @Override
    public UserTopTo getTopInfo() {

        UserTopTo userTopTo = new UserTopTo();

//  今日上传病例数
        QueryWrapper<Patient> wrapper = new QueryWrapper<Patient>()
                .apply("date(create_time) = date(now())");

        List<Patient> todaypatients = patientService.list(wrapper);
        userTopTo.setTodayUploadCaseNum(todaypatients.size());

//  病例总数
        int count = patientService.count();
        userTopTo.setCaseTotal(count);

//  未处理异议数   异议数量
        QueryWrapper<Objection> queryWrapper = new QueryWrapper<Objection>()
                .eq("processing_state", "0");
        List<Objection> list = objectionService.list(queryWrapper);
        int objectionNum = objectionService.count();

        userTopTo.setUnResolveCase(list.size());
        userTopTo.setObjectionNum(objectionNum);

//  切面正确率
        List<Patient> patientList = patientService.list();
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
                userTopTo.setAspectCorrectRate(rateNum + "%");
            }
        }



//  今日答题人数
        QueryWrapper<PracticePage> wrapperPracticePage = new QueryWrapper<PracticePage>()
                .apply("date(create_time) = date(now())")
                .select("distinct (user_id)");

        userTopTo.setTodayAnswerNum(practicePageService.count(wrapperPracticePage));

//  题库正确率  从用户身上获取正确率
        double userCountCorrect = 0.0;
        double totalCompleteRateCorrect = 0.0;

        QueryWrapper<User> userQueryWrapperCorrect = new QueryWrapper<User>()
                .ne("role", "admin")
                .ne("role","superadmin");

        List<User> userListCorrect = list(userQueryWrapperCorrect);
        userCountCorrect = userListCorrect.size();
        for (User user:userListCorrect){
            String StringCorrect = user.getCorrectRateQuestionBank();
            if (StringCorrect == null || StringCorrect.equals("")){
                continue;
            }
            int indexOfPercent = StringCorrect.indexOf('%');
            double doubleValueT = Double.parseDouble(StringCorrect.substring(0, indexOfPercent));
            totalCompleteRateCorrect += doubleValueT;
        }

        BigDecimal correctUserBigDecimalCorrect = new BigDecimal(userCountCorrect);
        BigDecimal answerUserBigDecimalCorrect = new BigDecimal(totalCompleteRateCorrect);

        if (answerUserBigDecimalCorrect.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimalCorrect = answerUserBigDecimalCorrect.divide(correctUserBigDecimalCorrect, 2, RoundingMode.HALF_UP);

            double rateNumCorrect = completeRateNumBigDecimalCorrect.setScale(2, RoundingMode.HALF_UP).doubleValue();
            userTopTo.setQuestionCorrect(rateNumCorrect + "%");
        }else {
            userTopTo.setQuestionCorrect("0.0%");
        }

//        List<Question> questionList = questionService.list();
//        if (questionList != null && questionList.size() > 0){
//            double totalCorrect = 0.0;
//            double numPatient = 0.0;
//            for (Question question:questionList){
//                if (question.getCorrectRate() != null && !question.getCorrectRate().equals("")){
//                    double oneAspectCorrect = Double.parseDouble(question.getCorrectRate().replace("%", "0"));
//                    totalCorrect += oneAspectCorrect;
//                    numPatient++;
//                }else {
//                    numPatient++;
//                }
//            }
//            BigDecimal correctUserBigDecimal = new BigDecimal(totalCorrect);
//            BigDecimal answerUserBigDecimal = new BigDecimal(numPatient);
//
//            if (answerUserBigDecimal.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
//                BigDecimal completeRateNumBigDecimal = correctUserBigDecimal.divide(answerUserBigDecimal, 2, RoundingMode.HALF_UP);
////                BigDecimal rateNumBigDecimal = completeRateNumBigDecimal.multiply(new BigDecimal(100));
//
//                double rateNum = completeRateNumBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
//                userTopTo.setQuestionCorrect(rateNum + "%");
//            }
//        }

//  题库完成率  得到所有用户完成率  所有用户数量  相除
        double userCount = 0.0;
        double totalCompleteRateT = 0.0;

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .ne("role", "admin")
                .ne("role","superadmin");

        List<User> userListT = list(userQueryWrapper);
        userCount = userListT.size();
        for (User user:userListT){
            String stringT = user.getQuestionBankCompleteRate();
            if (stringT == null || stringT.equals("")){
                continue;
            }
            int indexOfPercent = stringT.indexOf('%');
            double doubleValueT = Double.parseDouble(stringT.substring(0, indexOfPercent));
            totalCompleteRateT += doubleValueT;
        }

        BigDecimal correctUserBigDecimalT = new BigDecimal(userCount);
        BigDecimal answerUserBigDecimalT = new BigDecimal(totalCompleteRateT);

        if (answerUserBigDecimalT.compareTo(BigDecimal.ZERO) != 0) { // 避免除以0的情况
            BigDecimal completeRateNumBigDecimalT = answerUserBigDecimalT.divide(correctUserBigDecimalT, 2, RoundingMode.HALF_UP);

            double rateNum = completeRateNumBigDecimalT.setScale(2, RoundingMode.HALF_UP).doubleValue();
            userTopTo.setQuestionComplete(rateNum + "%");
        }else {
            userTopTo.setQuestionComplete("0.0%");
        }
        return userTopTo;
    }

    @Override
    public Page<User> getAllUserOrderBy(OrderUo orderUo,Integer userId) {

        if (orderUo == null){
            throw new RuntimeException("上传得排序条件为空");
        }
        User user = getById(userId);
        if (orderUo.getOrder() == 1){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<User> wrapper = new QueryWrapper<User>()
                    .orderBy(true, true, convertedString);
            if (user.getRole() == UserType.SUPERADMIN){
                wrapper.ne("role",UserType.SUPERADMIN);
            } else if (user.getRole() == UserType.ADMIN) {
                wrapper.ne("role",UserType.SUPERADMIN);
                wrapper.ne("role",UserType.ADMIN);
            }else {
            }
            if (orderUo.getName() == null || orderUo.getName().equals("")){
            }else {
        //有搜索字段
                wrapper.like("name",orderUo.getName());
            }
            Page<User> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return userMapper.selectPage(page, wrapper);
        }else if (orderUo.getOrder() == 0){
            String convertedString = orderUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
            QueryWrapper<User> wrapper = new QueryWrapper<User>()
                    .orderBy(true, false, convertedString);
            if (user.getRole() == UserType.SUPERADMIN){
                wrapper.ne("role",UserType.SUPERADMIN);
            } else if (user.getRole() == UserType.ADMIN) {
                wrapper.ne("role",UserType.SUPERADMIN);
                wrapper.ne("role",UserType.ADMIN);
            }else {
            }
            if (orderUo.getName() == null || orderUo.getName().equals("")){
            }else {
                //有搜索字段
                wrapper.like("name",orderUo.getName());
            }

            Page<User> page = new Page<>(orderUo.getPage(), orderUo.getLimit());
            return userMapper.selectPage(page,wrapper);
        }else {
            return null;
        }
    }

    @Override
    public void delUploadCase(Integer userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("id", userId);
        User user = getOne(wrapper);
        if (user.getNumUploadCases() <= 0){
            throw new RuntimeException("用户上传病例为0");
        }else {
            Integer numUploadCases = user.getNumUploadCases();
            numUploadCases--;
            user.setNumUploadCases(numUploadCases);
            updateById(user);
        }
    }

    @Override
    public void delUploadCases(int length, Integer userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("id", userId);
        User user = getOne(wrapper);

        if (user.getNumUploadCases() < length){
            throw new RuntimeException("用户上传病例数不足");
        }else {
            Integer numUploadCases = user.getNumUploadCases();
            numUploadCases -= length;
            user.setNumUploadCases(numUploadCases);
            updateById(user);
        }

    }

    @Override
    public Page<User> pageByRole(UserRoleUo userRoleUo) {
        if (userRoleUo.getRole() == UserType.SUPERADMIN){
            LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                    .ne(User::getRole, UserType.SUPERADMIN);
            Page<User> userPage = new Page<>(userRoleUo.getPage(), userRoleUo.getLimit());
            Page<User> page = page(userPage, wrapper);
            return page;
        } else if (userRoleUo.getRole() == UserType.ADMIN) {
            LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                    .ne(User::getRole, UserType.SUPERADMIN)
                    .ne(User::getRole, UserType.ADMIN);
            Page<User> userPage = new Page<>(userRoleUo.getPage(), userRoleUo.getLimit());
            Page<User> page = page(userPage, wrapper);
            return page;
        }else {
            return null;
        }
    }

    @Override
    public void setPatientAspectCorrect(Integer userId) {
        User user = getById(userId);
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<Patient>()
                .eq("user_id", userId);
        List<Patient> patientList = patientService.list(queryWrapper);
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
                updateById(user);
            }else {
                user.setCorrectRateCase("0.0%");
                updateById(user);
            }
        }
    }


}
