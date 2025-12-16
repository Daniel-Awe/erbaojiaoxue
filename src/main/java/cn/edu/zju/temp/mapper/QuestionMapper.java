package cn.edu.zju.temp.mapper;

import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.uo.OrderUo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> OrderAsc(@Param("orderUo") OrderUo orderUo, @Param("rows") Long rows);

    List<Question> orderDesc(@Param("orderUo") OrderUo orderUo, @Param("rows") Long rows);
}
