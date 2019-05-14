package com.mingxun.calculation.manager;

import com.mingxun.calculation.dao.BaseStationDao;
import com.mingxun.calculation.model.BaseStation;
import com.mingxun.calculation.model.BaseStationTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.daoziwo.cloud.interfaces.Paging;

import java.util.List;

/**
 * Created by ouyang on 2019/5/7.
 */
@Component
public class BaseStationManager {

    @Autowired
    private BaseStationDao baseStationDao;

    public void save(BaseStation po){
        baseStationDao.save(po);
    }

    public List<BaseStation> list(){
        return baseStationDao.selectAll();
    }

    /**
     * 批量插入
     * @param list
     * @return
     */
    public int batchInsertData(List<BaseStation> list){
        return  baseStationDao.batchInsertData(list);
    }

    public Paging<BaseStation> findPaging(BaseStationTerm term){
            List<BaseStation> baseStationList = baseStationDao.findPaging(term);
            return new Paging<>(term, baseStationList);

    }

}
