package com.mingxun.calculation.model;

import lombok.Data;
import wang.daoziwo.cloud.interfaces.Term;

/**
 * Created by ouyang on 2019/5/10.
 */
@Data
public class BaseStationTerm extends Term {

    private String stationName;

    private Integer type;
}
