package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.PracticePage;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.PracticeErrorTopTo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.QuestionAndPracticeUo;
import cn.edu.zju.temp.entity.vo.ObjectionVo;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPracticePageService extends IService<PracticePage> {
    Boolean addErrorQuestion(Long questionId, Long userId,String answer,Long questionSetId);

    Page<PracticePage> getErrors(ObjectionVo objectionVo);

    Boolean addTrueQuestion(Long questionId, Long userId, String answer,Long questionSetId);

    Boolean addCollectQuestion(Long questionId, Long userId, Integer answerId,Long questionSetId);

    Page<PracticePage> getCollects(ObjectionVo objectionVo);

    Page<PracticePage> getErrorsByUserId(UserSo userSo);

    PracticeErrorTopTo getErrorTopInfo(Integer userId);

    Integer getPatientIdByQuestionId(Integer objectionId);

    Page<PracticePage> getAllErrorQuestionOrderBy(OrderPatientUo orderPatientUo);

    Page<PracticePage> getAllCollectQuestionOrderBy(OrderPatientUo orderPatientUo);

    void deleteByUserId(Long id);

    void deletePracticeByIds(Long[] ids);

    String setCorrectByQuestionId(Long questionId);

    List<PracticePage> getErrorsByUserIdList(Integer userId);

    List<PracticePage> getCollectsByUserIdList(Integer userId);

    QuestionAndPracticeUo redoErrorQuestion(Integer questionId,Integer userId);

    QuestionAndPracticeUo redoErrorInfo(Long questionId, Integer answerId, Integer questionSetId,Integer userId);

    PracticePage getByUserIdAndQuestionId(Integer collectId, Integer userId);
}
