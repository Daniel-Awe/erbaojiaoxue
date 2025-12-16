package cn.edu.zju.temp.mapper;

import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.entity.uo.OrderUo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: gsq
 * @description: 用户
 * @date: 2024/4/30 16:14
 * @version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> getUsersByUsername(@Param("name") String name);

    List<User> OrderAsc(@Param("orderUo") OrderUo orderUo, @Param("limit") Long limit, @Param("rows") Long rows);

    List<User> orderDesc(@Param("orderUo") OrderUo orderUo, @Param("limit") Long limit, @Param("rows") Long rows);
}
