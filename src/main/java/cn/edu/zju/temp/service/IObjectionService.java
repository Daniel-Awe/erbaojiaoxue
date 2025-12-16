package cn.edu.zju.temp.service;

import cn.edu.zju.temp.entity.Objection;
import cn.edu.zju.temp.entity.so.ResultObjectionSo;
import cn.edu.zju.temp.entity.so.UserSo;
import cn.edu.zju.temp.entity.to.ObjectionTo;
import cn.edu.zju.temp.entity.uo.OrderPatientStateUo;
import cn.edu.zju.temp.entity.uo.OrderPatientUo;
import cn.edu.zju.temp.entity.uo.OrderUo;
import cn.edu.zju.temp.util.PageQuerySo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IObjectionService extends IService<Objection> {

    Page<Objection> getlistcase(ObjectionTo objectionTo);

    Page<Objection> getlistbank(ObjectionTo objectionTo);

    Boolean solveObjection(ResultObjectionSo resultObjectionSo);

    Page<Objection> getAllObjectionOrderBy(OrderPatientStateUo orderPatientStateUo);

    Page<Objection> getAllObjectionBankOrderBy(OrderPatientStateUo orderPatientStateUo);

    Page<Objection> getlistcaseByUserID(UserSo userSo);

    Page<Objection> getlistbankByUserID(UserSo userSo);

    void delByQuestionId(Long id);

    void delMoreQuestionIds(Long[] ids);

    void delByUserId(Long id);
}
