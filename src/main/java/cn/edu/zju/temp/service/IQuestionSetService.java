package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.QuestionSet;
import cn.edu.zju.temp.entity.so.QuestionSetPageSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.QuestionSetTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionSetUo;
import cn.edu.zju.temp.entity.vo.QuestionSetPageVo;
import cn.edu.zju.temp.entity.vo.QuestionSetVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IQuestionSetService extends IService<QuestionSet> {
    Page<Question> getPageQuestionBySetId(QuestionSetVo questionSetVo);

    List<QuestionSet> setCorUsers(List<QuestionSet> questionSets);

    Page<QuestionSet> getAllQuestionSetOrderBy(OrderUo orderUo);

    QuestionSet saveOneQuestionSet(String name, Integer examDuration);

    Boolean deleteSetOneById(String setId);

    Boolean updateQuestionSet(QuestionSet questionSet);

    QuestionSetTopTo getTopInfo(Integer questionSetId);

    Page<QuestionSetPageVo> getErrorsQuestionSet(UserSo userSo);

    Page<PracticePage> getAllerrorsByQuestionId(QuestionSetPageSo questionSetPageSo,Integer userId);

    Page<QuestionSetPageVo> getCollectQuestionSet(UserSo userSo);

    Page<PracticePage> getAllcollectsByQuestionId(QuestionSetPageSo questionSetPageSo, Integer userId);

    Page<QuestionSetUo> pageQuestionSet(PracticeUo practiceUo, Integer userId);

    void setSomeThing(Long questionSetId);
}
