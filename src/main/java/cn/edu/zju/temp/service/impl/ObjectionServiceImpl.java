package cn.edu.zju.temp.service.impl;

import cn.edu.zju.temp.entity.Objection;
import cn.edu.zju.temp.entity.PatientAnalyse;
import cn.edu.zju.temp.entity.Question;
import cn.edu.zju.temp.entity.so.ResultObjectionSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ObjectionTo;
import cn.edu.zju.temp.entity.uo.OrderPatientStateUo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.mapper.ObjectionMapper;
import cn.edu.zju.temp.service.IObjectionService;
import cn.edu.zju.temp.service.IPatientAnalyseService;
import cn.edu.zju.temp.service.IQuestionService;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/10 9:26
 * @version: 1.0
 */
@Service
public class ObjectionServiceImpl extends ServiceImpl<ObjectionMapper, Objection> implements IObjectionService {

    @Autowired
    private IObjectionService objectionService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IPatientAnalyseService patientAnalyseService;

    @Autowired
    private ObjectionMapper objectionMapper;


    @Override
    public Page<Objection> getlistcase(ObjectionTo objectionTo) {
        QueryWrapper<Objection> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("patient_analyse_id");
        if (objectionTo.getUserId() == null){
        }else {
            wrapper.eq("user_id",objectionTo.getUserId());
        }
        if (objectionTo == null || objectionTo.getState() == null){
        } else if (objectionTo.getState() == 0) {
        } else if (objectionTo.getState() == 1) {
            wrapper.eq("processing_state",0);
        } else if (objectionTo.getState() == 2) {
            wrapper.eq("processing_state",1);
        }
        return page(new Page<>(objectionTo.getPage(), objectionTo.getLimit()), wrapper);
    }

    @Override
    public Page<Objection> getlistbank(ObjectionTo objectionTo) {
        QueryWrapper<Objection> wrapper = new QueryWrapper<>();
        if (objectionTo.getUserId() == null){
        }else {
            wrapper.eq("user_id",objectionTo.getUserId());
        }
        wrapper.isNotNull("question_id");
        wrapper.isNull("patient_analyse_id");
        if (objectionTo == null || objectionTo.getState() == null){
        } else if (objectionTo.getState() == 0) {

        } else if (objectionTo.getState() == 1) {
            wrapper.eq("processing_state",0);
        } else if (objectionTo.getState() == 2) {
            wrapper.eq("processing_state",1);
        }
        return page(new Page<Objection>(objectionTo.getPage(),objectionTo.getLimit()), wrapper);
    }

    @Override
    public Boolean solveObjection(ResultObjectionSo resultObjectionSo) {

        Objection objection = objectionService.getById(resultObjectionSo.getObjectionId());

            objection.setProcessingState(1);  //表示已处理
            objection.setProcessingResult(resultObjectionSo.getResult());  //表示采纳
            objection.setProcessingReply(resultObjectionSo.getReply());
            boolean update = objectionService.updateById(objection);
//  若采纳    就将题目答案或病例分析答案改成用户异议提出的答案，
            if (resultObjectionSo.getResult() == 1){
                if (objection.getQuestionId() != null){
                    Question question = questionService.getById(objection.getQuestionId());
                    question.setAnswer(objection.getObjectionAnswer());
                    questionService.updateById(question);
                }else if (objection.getPatientAnalyseId() != null){
                    PatientAnalyse patientAnalyse = patientAnalyseService.getById(objection.getPatientAnalyseId());
                    patientAnalyse.setAnalyseAnswer(objection.getObjectionAnswer());
                    patientAnalyseService.updateById(patientAnalyse);
                }else {
                    throw new RuntimeException("异议的题目或病例分析图已被删除");
                }
            }

        return update;
    }

    @Override
    public Page<Objection> getAllObjectionOrderBy(OrderPatientStateUo orderPatientStateUo) {
        if (orderPatientStateUo == null ){
            throw new RuntimeException("上传得排序条件为空");
        }

//  todo  报nullPoint异常
        if (orderPatientStateUo.getOrder() == null || orderPatientStateUo.getOrder() == 1){
            if(orderPatientStateUo.getColumn() == null || orderPatientStateUo.getColumn().equals("")){
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .isNotNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());
                return objectionMapper.selectPage(page,wrapper);
            }else {
                String convertedString = orderPatientStateUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
                System.out.println(convertedString);

                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .orderBy(true, true, convertedString)
                        .isNotNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){
                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());
                return objectionMapper.selectPage(page,wrapper);
            }
        }else {
            if (orderPatientStateUo.getColumn() == null || orderPatientStateUo.getColumn().equals("")){
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .isNotNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }else {
                String convertedString = orderPatientStateUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
                System.out.println(convertedString);
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .orderBy(true, false, convertedString)
                        .isNotNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }
        }
    }

    @Override
    public Page<Objection> getAllObjectionBankOrderBy(OrderPatientStateUo orderPatientStateUo) {
        if (orderPatientStateUo == null){
            throw new RuntimeException("上传的查询条件为空");
        }
        if (orderPatientStateUo.getOrder() == null || orderPatientStateUo.getOrder() == 1){
            if (orderPatientStateUo.getColumn() == null || orderPatientStateUo.getColumn().equals("")){
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .isNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }else {
                String convertedString = orderPatientStateUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
                System.out.println(convertedString);
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .orderBy(true, true, convertedString)
                        .isNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }

//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }
        }else {
            if (orderPatientStateUo.getColumn() == null || orderPatientStateUo.getColumn().equals("")){
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .isNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }

//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }else {
                String convertedString = orderPatientStateUo.getColumn().replaceAll("[A-Z]", "_$0").toLowerCase();
                System.out.println(convertedString);
                QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                        .orderBy(true, false, convertedString)
                        .isNull("patient_analyse_id");
                if (orderPatientStateUo.getUserId() == null){
                }else {
                    wrapper.eq("user_id",orderPatientStateUo.getUserId());
                }
//  0查全部  1查未处理  2查已处理
                if (orderPatientStateUo.getState() == null){

                }else if (orderPatientStateUo.getState() == 1){
                    wrapper.eq("processing_state",0);
                }else if (orderPatientStateUo.getState() == 2){
                    wrapper.eq("processing_state",1);
                }
                Page<Objection> page = new Page<>(orderPatientStateUo.getPage(), orderPatientStateUo.getLimit());

                return objectionMapper.selectPage(page,wrapper);
            }
        }
    }

    @Override
    public Page<Objection> getlistcaseByUserID(UserSo userSo) {
        Page<Objection> page = new Page<>(userSo.getPage(), userSo.getLimit());
        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("user_id", userSo.getUserId())
                .isNotNull("patient_analyse_id");
        Page<Objection> objectionPage = objectionMapper.selectPage(page, wrapper);
        return objectionPage;
    }

    @Override
    public Page<Objection> getlistbankByUserID(UserSo userSo) {
        Page<Objection> page = new Page<>(userSo.getPage(), userSo.getLimit());
        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("user_id", userSo.getUserId());
        wrapper.isNotNull("question_id");
        wrapper.isNull("patient_analyse_id");
        Page<Objection> objectionPage = objectionMapper.selectPage(page, wrapper);
        return objectionPage;
    }

    @Override
    public void delByQuestionId(Long id) {
        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("question_id", id);
        List<Objection> objectionList = list(wrapper);
        if (objectionList == null || objectionList.size() == 0){
        }else {
            for (Objection objection:objectionList){
                removeById(objection.getId());
            }
        }

    }

    @Override
    public void delMoreQuestionIds(Long[] ids) {
        for (Long id:ids){
            QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                    .eq("question_id", id);
            List<Objection> objectionList = list(wrapper);
            if (objectionList == null || objectionList.size() == 0){
            }else {
                for (Objection objection:objectionList){
                    removeById(objection.getId());
                }
            }
        }
    }

    @Override
    public void delByUserId(Long id) {
        QueryWrapper<Objection> wrapper = new QueryWrapper<Objection>()
                .eq("user_id", id);
        List<Objection> objections = objectionService.list(wrapper);
        if (objections == null || objections.size() == 0){
        }else {
            for (Objection objection:objections){
                removeById(objection.getId());
            }
        }
    }
}