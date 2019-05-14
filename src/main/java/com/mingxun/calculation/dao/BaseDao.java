package com.mingxun.calculation.dao;

import wang.daoziwo.cloud.interfaces.Term;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangpeng
 * @date 2018/10/12 14:34:39
 */
public interface BaseDao<T, PK extends Serializable> {
    /**
     * id返回
     * @return
     */
    Integer nextId();

    /**
     * 插入
     * @param po
     */
    void save(T po);

    /**
     * 更新
     * @param po
     */
    void update(T po);

    /**
     * 根据主键id获取
     * @param id
     * @return
     */
    T get(PK id);

    /**
     * 分页查询
     * @param term
     * @return
     */
    List<T> findPaging(Term term);
}
