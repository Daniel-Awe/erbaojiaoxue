package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.Patient;
import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.entity.so.UserLoginSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.UserTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.UserLoginUo;
import cn.edu.zju.temp.entity.uo.UserRoleUo;
import cn.edu.zju.temp.entity.vo.UserLoginVo;
import cn.edu.zju.temp.entity.vo.UserVo;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/4/30 16:13
 * @version: 1.0
 */
public interface IUserService extends IService<User> {


    UserLoginVo userLoginso(UserLoginSo userLoginSo);

    List<User> getAllUsers();

    User addOneUser(UserLoginSo userLoginSo);

    Boolean deleteOneUserById(Long id);

    Boolean deleteMoreUserByIds(Long[] ids);

    Page<User> getUserByName(UserVo userVo,Integer userId);

    Boolean updateOneByUser(UserLoginUo userLoginUo, Integer userId);

    Page<User> getAllUsersByPage(Integer page, Integer limit);

    Page<PracticePage> getErrorPracticePage(UserSo userSo);

    Page<Patient> getcasesPracticePage(UserSo userSo);

    UserTopTo getTopInfo();

    Page<User> getAllUserOrderBy(OrderUo orderUo,Integer userId);

    void delUploadCase(Integer userId);

    void delUploadCases(int length, Integer userId);

    Page<User> pageByRole(UserRoleUo userRoleUo);

    void setPatientAspectCorrect(Integer userId);
}
