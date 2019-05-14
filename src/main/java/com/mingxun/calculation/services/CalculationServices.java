package com.mingxun.calculation.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mingxun.calculation.dto.*;
import com.mingxun.calculation.manager.BaseStationManager;
import com.mingxun.calculation.model.BaseStation;
import com.mingxun.calculation.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wang.daoziwo.cloud.interfaces.Json;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ouyang on 2019/5/6.
 */
@Component
public class CalculationServices {

    private Logger logger = LoggerFactory.getLogger(CalculationServices.class);

    @Autowired
    private BaseStationManager baseStationManager;

    private  final double EARTH_RADIUS = 6378.137;//地球半径,单位千米

    private  final double PI = 3.14159265;
    private  double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 距离计算
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public  double algorithm(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1); // 纬度
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;//两点纬度之差
        double b = rad(longitude1) - rad(longitude2); //经度之差
        double s = 2 * Math.asin(Math
                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
        s = s * 6378137.0;//弧长乘地球半径（半径为米）

        s = Math.round(s * 100d) / 100d;//精确距离的数值
        //s = s/1000;//将单位转换为km，如果想得到以米为单位的数据 就不用除以1000
        //四舍五入 保留一位小数
        //DecimalFormat df = new DecimalFormat("#.0");
        return s ;
    }



    /**
     * 根据 点和半径  获取经纬度范围
     * @param lat
     * @param lon
     * @param distance 单位米
     * @return
     */
    public  double[] getAround(double lat,double lon,int distance){

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901*1609)/360.0;
        double raidusMile = distance;

        Double dpmLat = 1/degree;
        Double radiusLat = dpmLat*raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree*Math.cos(latitude * (PI/180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng*raidusMile;
        Double  minLng= longitude - radiusLng;
        Double  maxLng= longitude + radiusLng;
        return new double[]{minLat,minLng,maxLat,maxLng};
    }


    public Json upload( MultipartFile mobile2G) throws IOException {
        Integer type =0 ;
        if(mobile2G.getOriginalFilename().contains("移动2G")){
            type=1;
        }else if(mobile2G.getOriginalFilename().contains("移动4G")){
            type=2;
        }else if(mobile2G.getOriginalFilename().contains("电信4G")){
            type=3;
        }else if(mobile2G.getOriginalFilename().contains("联通4G")){
            type=4;
        }else{
            return Json.SUCCESS().setMessage("导入文件名字错误");
        }
        logger.info("\n\t upload "+mobile2G.getOriginalFilename());
        List<BaseStation> mobile2GList = ExcelUtils.parseExcel(mobile2G.getOriginalFilename(), mobile2G.getInputStream(), row -> {
            BaseStation mobile2GDto = new BaseStation(row.get(0),Double.parseDouble(row.get(1)),Double.parseDouble(row.get(2)), row.get(3), row.get(4));
            return mobile2GDto;
        });
       if(mobile2GList.size() > 0){
           for(int i=1;i<mobile2GList.size();i++){
               mobile2GList.get(i).setStationType(type);
               baseStationManager.save(mobile2GList.get(i));
           }
       }
        return Json.SUCCESS().setMessage("导入成功");
    }

    public List<ResultDto>  search( Integer distance,Integer type) throws IOException {
        List<ResultDto> resultDtoList = new ArrayList<>();
        List<BaseStation> list = baseStationManager.list();
        Map<Integer, List<BaseStation>> groupBy = list.stream().collect(Collectors.groupingBy(BaseStation::getStationType));
        List<BaseStation> YD2G = groupBy.get(1);
        List<BaseStation> YD4G = groupBy.get(2);
        List<BaseStation> DX4G = groupBy.get(3);
        List<BaseStation> LT4G = groupBy.get(4);
        if(type == 1){
            YD2G.stream().forEach(p->{
                    ResultDto resultDto = new ResultDto();
                    BeanUtils.copyProperties(p,resultDto);
                    if(p.getLat() > 0 && p.getLng() > 0) {
                        List<BaseStation> YD4GList = new ArrayList<>();
                        List<BaseStation> DX4GList = new ArrayList<>();
                        List<BaseStation> LT4GList = new ArrayList<>();
                        double[] getAround = getAround(p.getLat(), p.getLng(), distance);
                        YD4GList = dealData(getAround, YD4G, p.getLat(), p.getLng(), distance);
                        DX4GList = dealData(getAround, DX4G, p.getLat(), p.getLng(), distance);
                        LT4GList = dealData(getAround, LT4G, p.getLat(), p.getLng(), distance);
                        resultDto.setYd4GList(YD4GList);
                        resultDto.setDx4GList(DX4GList);
                        resultDto.setLt4GList(LT4GList);
                    }else{
                        resultDto.setYd4GList(new ArrayList<>());
                        resultDto.setDx4GList(new ArrayList<>());
                        resultDto.setLt4GList(new ArrayList<>());
                    }
                    resultDtoList.add(resultDto);
            });
        }
        else if(type ==2 ){
            YD4G.stream().forEach(p->{
                ResultDto resultDto = new ResultDto();
                BeanUtils.copyProperties(p,resultDto);
                if(p.getLat() > 0 && p.getLng() > 0){
                    List<BaseStation> YD2GList = new ArrayList<>();
                    List<BaseStation> DX4GList = new ArrayList<>();
                    List<BaseStation> LT4GList = new ArrayList<>();
                    double[] getAround =  getAround(p.getLat(),p.getLng(),distance);
                     YD2GList = dealData(getAround,YD2G,p.getLat(),p.getLng(),distance);
                     DX4GList = dealData(getAround,DX4G,p.getLat(),p.getLng(),distance);
                     LT4GList = dealData(getAround,LT4G,p.getLat(),p.getLng(),distance);
                    resultDto.setYd2GList(YD2GList);
                    resultDto.setDx4GList(DX4GList);
                    resultDto.setLt4GList(LT4GList);
                }else{
                    resultDto.setYd2GList(new ArrayList<BaseStation>());
                    resultDto.setDx4GList(new ArrayList<BaseStation>());
                    resultDto.setLt4GList(new ArrayList<BaseStation>());
                }
                resultDtoList.add(resultDto);
            });
        }else if(type == 3 ){
            LT4G.stream().forEach(p->{
                ResultDto resultDto = new ResultDto();
                BeanUtils.copyProperties(p,resultDto);
                if(p.getLat() > 0 && p.getLng() > 0){
                    List<BaseStation> YD2GList = new ArrayList<>();
                    List<BaseStation> YD4GList = new ArrayList<>();
                    List<BaseStation> LT4GList = new ArrayList<>();
                    double[] getAround =  getAround(p.getLat(),p.getLng(),distance);
                    YD2GList = dealData(getAround,YD2G,p.getLat(),p.getLng(),distance);
                    YD4GList = dealData(getAround,YD4G,p.getLat(),p.getLng(),distance);
                    LT4GList = dealData(getAround,LT4G,p.getLat(),p.getLng(),distance);
                    resultDto.setYd2GList(YD2GList);
                    resultDto.setYd4GList(YD4GList);
                    resultDto.setLt4GList(LT4GList);
                }else{
                    resultDto.setYd2GList(new ArrayList<BaseStation>());
                    resultDto.setYd4GList(new ArrayList<BaseStation>());
                    resultDto.setLt4GList(new ArrayList<BaseStation>());
                }
                resultDtoList.add(resultDto);
            });
        }else{
            DX4G.stream().forEach(p->{
                ResultDto resultDto = new ResultDto();
                BeanUtils.copyProperties(p,resultDto);
                if(p.getLat() > 0 && p.getLng() > 0){
                    List<BaseStation> YD2GList = new ArrayList<>();
                    List<BaseStation> YD4GList = new ArrayList<>();
                    List<BaseStation> DX4GList = new ArrayList<>();
                    double[] getAround =  getAround(p.getLat(),p.getLng(),distance);
                    YD2GList = dealData(getAround,YD2G,p.getLat(),p.getLng(),distance);
                    YD4GList = dealData(getAround,YD4G,p.getLat(),p.getLng(),distance);
                    DX4GList = dealData(getAround,DX4G,p.getLat(),p.getLng(),distance);
                    resultDto.setYd2GList(YD2GList);
                    resultDto.setYd4GList(YD4GList);
                    resultDto.setDx4GList(DX4GList);
                }else{
                    resultDto.setYd2GList(new ArrayList<BaseStation>());
                    resultDto.setYd4GList(new ArrayList<BaseStation>());
                    resultDto.setDx4GList(new ArrayList<BaseStation>());
                }
                resultDtoList.add(resultDto);
            });
        }
        return resultDtoList ;
    }

    private  final int OPENID_NUM = 800; //经实践，800一批插入相对较快，这个可以随便定义
    public Json batchInsertData( MultipartFile mobile2G) throws IOException {
        Integer type =0 ;
        if(mobile2G.getOriginalFilename().contains("移动2G")){
            type=1;
        }else if(mobile2G.getOriginalFilename().contains("移动4G")){
            type=2;
        }else if(mobile2G.getOriginalFilename().contains("电信4G")){
            type=3;
        }else if(mobile2G.getOriginalFilename().contains("联通4G")){
            type=4;
        }else{
            return Json.SUCCESS().setMessage("导入文件名字错误");
        }
        logger.info("\n\t upload "+mobile2G.getOriginalFilename());
        List<BaseStation> mobile2GList = ExcelUtils.parseExcel(mobile2G.getOriginalFilename(), mobile2G.getInputStream(), row -> {
            BaseStation mobile2GDto = new BaseStation(row.get(0),Double.parseDouble(row.get(1)),Double.parseDouble(row.get(2)), row.get(3), row.get(4));
            return mobile2GDto;
        });

        if(mobile2GList.size() > 0){
            int listSize=mobile2GList.size();
            int toIndex=OPENID_NUM;
            for(int i=1;i<mobile2GList.size();i+=OPENID_NUM){
                if(i+OPENID_NUM>listSize){//作用为toIndex最后没有800条数据则剩余几条newList中就装几条
                    toIndex=listSize-i;
                }
                List<BaseStation> newList = mobile2GList.subList(i,i+toIndex);
                for(int q=0 ;q<newList.size();q++) {
                    newList.get(q).setStationType(type);
                }

                baseStationManager.batchInsertData(newList);
            }
        }
        return Json.SUCCESS().setMessage("导入成功");
    }

    /**
     * 判断 经纬度 范围 和 计算 其他运营商基站 的距离
     * @param getAround
     * @param baseStationList
     * @param lat
     * @param lng
     * @param distance
     */
    public List<BaseStation> dealData(double[] getAround, List<BaseStation> baseStationList, double lat, double lng, Integer distance){
        List<BaseStation> baseStationList1 = new ArrayList<>();
         //minLat,minLng,maxLat,maxLng
        List<BaseStation> baseStationList2 = new ArrayList<>();
        if(baseStationList.size() > 0 ){
            baseStationList2= baseStationList.stream().
                    filter(
                            (BaseStation a)->a.getLat()>getAround[0] && a.getLat()<getAround[2] && a.getLng()>getAround[1] && a.getLng()<getAround[3]
                    ).collect(Collectors.toList());
        }
        if(baseStationList2.size() >0){
            baseStationList2.stream().forEach(p->{
                double dis = judgeLatlngAndCalculation(lng,lat,p.getLng(),p.getLat(),distance);
                if(dis >= 0){
                    BaseStation baseStation = new BaseStation();
                    BeanUtils.copyProperties(p,baseStation);
                    baseStation.setDistance(dis);
                    baseStationList1.add(baseStation);
                }
            });
        }
        return baseStationList1;
    }


    /**
     * 计算距离
     * @param lat
     * @param lng
     * @param lat1
     * @param lng1
     * @param distance
     * @return
     */
    public double judgeLatlngAndCalculation(double lat,double lng,double lat1,double lng1,Integer distance){
        if(lat1 >0 && lng1 > 0){
           // double dis = getDistance(lat,lng,lat1,lng1);
            double dis =algorithm(lng,lat,lng1,lat1);
            if(dis < distance){
                return dis;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
}
