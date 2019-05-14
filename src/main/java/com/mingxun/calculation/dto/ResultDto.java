package com.mingxun.calculation.dto;

import com.mingxun.calculation.model.BaseStation;
import lombok.Data;

import java.util.List;

/**
 * Created by ouyang on 2019/5/6.
 */
@Data
public class ResultDto {

    private Integer id;

    private String stationName;

    private double lat;

    private double lng;

    /**
     * 室内室外
     */
    private String door;


    /**
     * 厂商名称
     */
    private String manufacturer;
    /**
     *
     */
    private Integer stationType;


    private Integer distance;

    private List<BaseStation> yd2GList;

    private List<BaseStation> yd4GList;

    private List<BaseStation> lt4GList;

    private List<BaseStation> dx4GList;

}
