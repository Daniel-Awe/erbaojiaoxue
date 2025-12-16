package cn.edu.zju.temp.controller;

import cn.edu.zju.temp.auth.CheckLogin;
import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.entity.so.UserLoginSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.PatientTopTo;
import cn.edu.zju.temp.entity.to.UserTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PatientAnalyseUo;
import cn.edu.zju.temp.entity.uo.UserLoginUo;
import cn.edu.zju.temp.entity.uo.UserRoleUo;
import cn.edu.zju.temp.entity.vo.UserLoginVo;
import cn.edu.zju.temp.entity.vo.UserVo;
import cn.edu.zju.temp.enums.UserType;
import cn.edu.zju.temp.service.IPatientService;
import cn.edu.zju.temp.service.IPracticePageService;
import cn.edu.zju.temp.service.IUserService;
import cn.edu.zju.temp.util.PageQuerySo;
import cn.edu.zju.temp.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * @author: xjw
 * @description: TODO
 * @date: 2025/3/13 15:49
 * @version: 1.0
 */
@ApiOperation("用户登录")
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPracticePageService practicePageService;


    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result<UserLoginVo> userLogin(@Validated @RequestBody UserLoginSo userLoginSo){
        return Result.ResultUtil.success(userService.userLoginso(userLoginSo));
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    @CheckLogin
    public Result<User> addUser(@Validated @RequestBody UserLoginSo userLoginSo){
        return Result.ResultUtil.success(userService.addOneUser(userLoginSo));
    }

    @ApiOperation(value = "清空单个用户")
    @DeleteMapping("/deleteone")
    @CheckLogin
    public Result<Boolean> deleteUser(@RequestParam("id") Long id) {
        Boolean isdelete = userService.deleteOneUserById(id);
        if (isdelete){
//  当前用户上传的病例也要删除
            patientService.delAllPatientByUserId(id);
//  当前用户做的题记录也删除
            practicePageService.deleteByUserId(id);

            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "删除单个用户")
    @DeleteMapping("/deleteUser")
    @CheckLogin
    public Result<Boolean> deleteUserOne(@RequestParam("id") Long id){
        User user = userService.getById(id);
        user.setIsDeleted(true);
        boolean update = userService.updateById(user);
        return Result.ResultUtil.success(update);
    }

    @ApiOperation(value = "删除多个用户")
    @DeleteMapping("/deleteUsers")
    @CheckLogin
    public Result<Boolean> deleteUserOne(@RequestBody Map<String,Long[]> map){
        Long[] ids = map.get("ids");
        if (ids == null || ids.length == 0){
            throw new RuntimeException("请选择用户删除");
        }
        for (Long userId:ids){
            User user = userService.getById(userId);
            user.setIsDeleted(true);
            userService.updateById(user);
        }
        return Result.ResultUtil.success();
    }

    @ApiOperation(value = "清空多个用户")
    @DeleteMapping("/deletemore")
    @CheckLogin
    public Result<Boolean> deleteUserMore(@RequestBody Map<String,Long[]> map){
        Long[] ids = map.get("ids");
        if (ids == null || ids.length == 0){
            throw new RuntimeException("请选择用户清空");
        }
        Boolean isdelete = userService.deleteMoreUserByIds(map.get("ids"));
        if (isdelete){
//  当前用户上传的病例也要删除
            patientService.delMorePatientIds(ids);
//用户做的题记录也要删除
            practicePageService.deletePracticeByIds(ids);
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("删除失败");
        }
    }

    @ApiOperation(value = "修改用户信息")
    @PutMapping("/updateUser")
    @CheckLogin
    public Result<Boolean> updateUser(@RequestBody UserLoginUo userLoginUo, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        Boolean isUpdated = userService.updateOneByUser(userLoginUo,userId);
        if (isUpdated){
            return Result.ResultUtil.success();
        }else {
            return Result.ResultUtil.fail("修改失败");
        }
    }

    @PostMapping("/page")
    @ApiOperation("分页 获取所有用户列表")
    @CheckLogin
    public Result<Page<User>> allUsers(@RequestBody UserRoleUo userRoleUo){
        Page<User> pages = userService.pageByRole(userRoleUo);
        return Result.ResultUtil.success(pages);
    }

    @PostMapping("/getByName/page")
    @ApiOperation("通过姓名检索用户")
    @CheckLogin
    public Result<Page<User>> getUserByName(@RequestBody UserVo userVo,HttpServletRequest request){

        Integer userId = (Integer) request.getAttribute("userId");
        Page<User> users = userService.getUserByName(userVo,userId);
        if (users == null || users.getRecords().size() == 0){
            return Result.ResultUtil.fail("未查到用户信息");
        }else {
            return Result.ResultUtil.success(users);
        }
    }

    @ApiOperation("查看用户错题")
    @PostMapping("/geterror/page")
    @CheckLogin
    public Result<Page<PracticePage>> getUserErrorByUserId(@RequestBody UserSo userSo){
        Page<PracticePage> pages = userService.getErrorPracticePage(userSo);
        return Result.ResultUtil.success(pages);
    }

    @ApiOperation("查看用户上传病例")
    @PostMapping("/getcase/page")
    @CheckLogin
    public Result<Page<Patient>> getUsercasesByUserId(@RequestBody UserSo userSo){
        Page<Patient> pages = userService.getcasesPracticePage(userSo);
        return Result.ResultUtil.success(pages);
    }

    //得到用户列表头部信息
    @ApiOperation("用户列表头部信息")
    @GetMapping("/top")
    public Result<UserTopTo> getUserTop(){

        UserTopTo userTopTo = userService.getTopInfo();
        if (userTopTo != null){
            return Result.ResultUtil.success(userTopTo);
        }else {
            return Result.ResultUtil.fail("获取用户头部信息失败");
        }
    }

    @ApiOperation("用户列表根据字段排序显示  order字段1升序 0降序    column要排序的字段名")
    @PostMapping("/orderby")
    @CheckLogin
    public Result<Page<User>> getPatientTop(@RequestBody OrderUo orderUo,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        Page<User> users = userService.getAllUserOrderBy(orderUo,userId);
        if (users != null){
            return Result.ResultUtil.success(users);
        }else {
            return Result.ResultUtil.fail("获取病例头部信息失败");
        }
    }

    @ApiOperation("用户列表点击病例查看")
    @PostMapping("/patientImgs")
    public Result<Page<Patient>> getPatientTop(@RequestBody PatientAnalyseUo patientAnalyseUo){

        Page<Patient> page = patientService.getUserPatientImgs(patientAnalyseUo);
        if (page != null){
            return Result.ResultUtil.success(page);
        }else {
            return Result.ResultUtil.fail("获取病例分析信息失败");
        }
    }
}