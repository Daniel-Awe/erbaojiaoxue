package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.so.ObjectionSo;
import cn.edu.zju.temp.entity.so.QuestionSo;
import cn.edu.zju.temp.entity.to.QuestionTopTo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.entity.uo.PatientBankUo;
import cn.edu.zju.temp.entity.uo.PracticeUo;
import cn.edu.zju.temp.entity.uo.QuestionAndPracticeUo;
import cn.edu.zju.temp.entity.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IQuestionService extends IService<Question> {
    Page<Question> getBankList(Integer page,Integer limit);

    Question addQuestion(QuestionSo questionSo);

    Boolean deleteOneQuestionById(Long id);

    Boolean deleteQuestionMoreByIds(Long[] ids);

    Boolean updateOneByQuestionVo(QuestionVo questionVo);

    Question getOneQuestionById(Long id);

    Boolean answerQuestion(Long questionId, Integer answerId,Long userId,Long questionSetId);

    Boolean objectionOnQuestion(ObjectionSo objectionSo);

    Boolean collectQuestion(Long questionId, Integer answerId, Long userId,Long questionSetId);

    Page<Question> getBankListByPage(Integer page, Integer limit);

    Boolean addByZip(MultipartFile file,Long questionSetId) throws IOException;

    Boolean importPatientAnalyse(List<PatientBankUo> patientBankUos);

    Boolean deleteErrorOne(Integer errorId);

    Boolean deleteCollectOne(Integer questionId,Integer userId);

    Boolean deleteErrorMore(Long[] errorids);

    Boolean deleteCollectMore(Long[] collectids);

    Boolean deleteObjectionOne(Integer objectionId);

    Boolean deleteObjectionMore(Long[] objectionIds);

    Boolean addQuestionByAnalyseIds(Long[] ids,Long questionSetId);

    void setUserCorrect(Long userId);

    QuestionTopTo getTopInfo();

    Page<Question> getAllQuestionOrderBy(OrderUo orderUo);

    Page<QuestionAndPracticeUo> getQuestionDetail(PracticeUo practiceUo);
}
