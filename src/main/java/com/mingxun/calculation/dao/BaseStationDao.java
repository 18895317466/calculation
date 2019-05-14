package com.mingxun.calculation.dao;

import com.mingxun.calculation.model.BaseStation;
import com.mingxun.calculation.model.BaseStationTerm;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BaseStationDao extends BaseDao<BaseStation, Integer> {

    void save(BaseStation po);

    List<BaseStation> selectAll();

    int batchInsertData(@Param(value = "list") List<BaseStation> list);

    List<BaseStation> findPaging(BaseStationTerm term);

}
