package com.mingxun.calculation.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by ouyang on 2019/5/7.
 */
@Data
public class BaseStation {

    private Integer id ;

    private String stationName;

    private Double lng;

    private Double lat;

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

    private Date creteDate;

    private double distance;

    public BaseStation() {
    }

    public BaseStation(String stationName, Double lng, Double lat, String door, String manufacturer) {
        this.stationName = stationName;
        this.lng = lng;
        this.lat = lat;
        this.door = door;
        this.manufacturer = manufacturer;
    }
}
