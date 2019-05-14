package com.mingxun.calculation.api;

import com.mingxun.calculation.dto.ResultDto;
import com.mingxun.calculation.services.CalculationServices;
import com.mingxun.calculation.utils.ExcelNewUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wang.daoziwo.cloud.interfaces.Json;


import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by ouyang on 2019/5/8.
 */
@RestController
public class ExcelController extends  BaseController{

    private Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private ExcelNewUtil excelNewUtil;
    @Autowired
    private CalculationServices calculationServices;


    @RequestMapping("export")
    public Json export(@RequestParam("distance")Integer distance,
                          @RequestParam("type")Integer type,
                          HttpServletResponse response) throws IOException {
        List<ResultDto> list =calculationServices.search(distance,type);
        if(type == 1 ){
            exportYD2G(list,type,response);
        }else if(type == 2 ){
            exportYD4G(list,type,response);
        }else if( type == 3 ){
            exportDX4G(list,type,response);
        }else{
            exportLT4G(list,type,response);
        }
        return Json.SUCCESS().setMessage("导出成功");
    }


    public void exportYD2G( List<ResultDto> resultList,Integer type,HttpServletResponse response){
        List<Map<String, Object>> list=null;//表内容集合，从数据库查，需要合并的列要进行分组，否则需要做合并的时候可能达不到理想结果
        List<Map<String, Object>> listmap=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new LinkedHashMap<String,Object>();
        map=new LinkedHashMap<String,Object>();
        map.put("column4_0", "OMC基站名");
        map.put("column4_1", "经度");
        map.put("column4_2", "纬度");
        map.put("column4_3", "室内室外");
        map.put("column5_4", "厂商名称");
        map.put("column6_5", "所属E-NODEB");
        map.put("column7_6", "经度");
        map.put("column8_7", "纬度");
        map.put("column9_8", "覆盖类型");
        map.put("column10_9", "厂家名称");
        map.put("column10_10", "距离");
        map.put("column4_11", "eNodeBName");
        map.put("column4_12", "经度");
        map.put("column4_13", "纬度");
        map.put("column4_14", "站型");
        map.put("column5_15", "厂商");
        map.put("column10_16", "距离");
        map.put("column6_17", "基站名称");
        map.put("column7_18", "经度");
        map.put("column8_19", "纬度");
        map.put("column9_20", "宏站/室分");
        map.put("column10_21", "厂家");
        map.put("column10_22", "距离");
        listmap.add(map);
        exportXlsx("通信信息移动2G",listmap,resultList,type,response);
    }
    public void exportYD4G( List<ResultDto> resultList,Integer type,HttpServletResponse response){
        List<Map<String, Object>> list=null;//表内容集合，从数据库查，需要合并的列要进行分组，否则需要做合并的时候可能达不到理想结果
        List<Map<String, Object>> listmap=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new LinkedHashMap<String,Object>();
        map=new LinkedHashMap<String,Object>();
        map.put("column6_1", "所属E-NODEB");
        map.put("column7_2", "经度");
        map.put("column8_3", "纬度");
        map.put("column9_4", "覆盖类型");
        map.put("column10_5", "厂家名称");
        map.put("column6_5", "OMC基站名");
        map.put("column7_6", "经度");
        map.put("column8_7", "纬度");
        map.put("column9_8", "室内室外");
        map.put("column10_9", "厂家名称");
        map.put("column10_10", "距离");
        map.put("column4_11", "eNodeBName");
        map.put("column4_12", "经度");
        map.put("column4_13", "纬度");
        map.put("column4_14", "站型");
        map.put("column5_15", "厂商");
        map.put("column10_16", "距离");
        map.put("column6_17", "基站名称");
        map.put("column7_18", "经度");
        map.put("column8_19", "纬度");
        map.put("column9_20", "宏站/室分");
        map.put("column10_21", "厂家");
        map.put("column10_22", "距离");
        listmap.add(map);
        exportXlsx("通信信息移动4G",listmap,resultList,type,response);
    }
    public void exportDX4G( List<ResultDto> resultList,Integer type,HttpServletResponse response){
        List<Map<String, Object>> list=null;//表内容集合，从数据库查，需要合并的列要进行分组，否则需要做合并的时候可能达不到理想结果
        List<Map<String, Object>> listmap=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new LinkedHashMap<String,Object>();

        map=new LinkedHashMap<String,Object>();
        map.put("column4_0", "eNodeBName");
        map.put("column4_1", "经度");
        map.put("column4_2", "纬度");
        map.put("column4_3", "站型");
        map.put("column5_4", "厂商");
        map.put("column6_5", "所属E-NODEB");
        map.put("column7_6", "经度");
        map.put("column8_7", "纬度");
        map.put("column9_8", "覆盖类型");
        map.put("column10_9", "厂家名称");
        map.put("column10_10", "距离");
        map.put("column4_11", "OMC基站名");
        map.put("column4_12", "经度");
        map.put("column4_13", "纬度");
        map.put("column4_14", "室内室外");
        map.put("column5_15", "厂商名称");
        map.put("column10_16", "距离");
        map.put("column6_17", "基站名称");
        map.put("column7_18", "经度");
        map.put("column8_19", "纬度");
        map.put("column9_20", "宏站/室分");
        map.put("column10_21", "厂家");
        map.put("column10_22", "距离");
        listmap.add(map);
        exportXlsx("通信信息电信4G",listmap,resultList,type,response);
    }
    public void exportLT4G( List<ResultDto> resultList,Integer type,HttpServletResponse response){
        List<Map<String, Object>> list=null;//表内容集合，从数据库查，需要合并的列要进行分组，否则需要做合并的时候可能达不到理想结果
        List<Map<String, Object>> listmap=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new LinkedHashMap<String,Object>();
        map=new LinkedHashMap<String,Object>();
        map.put("column4_0", "基站名称");
        map.put("column4_1", "经度");
        map.put("column4_2", "纬度");
        map.put("column4_3", "宏站/室分");
        map.put("column5_4", "厂家");

        map.put("column6_5", "所属E-NODEB");
        map.put("column7_6", "经度");
        map.put("column8_7", "纬度");
        map.put("column9_8", "覆盖类型");
        map.put("column10_9", "厂家名称");
        map.put("column10_10", "距离");

        map.put("column4_11", "eNodeBName");
        map.put("column4_12", "经度");
        map.put("column4_13", "纬度");
        map.put("column4_14", "站型");
        map.put("column5_15", "厂商");
        map.put("column10_16", "距离");

        map.put("column6_17", "OMC基站名");
        map.put("column7_18", "经度");
        map.put("column8_19", "纬度");
        map.put("column9_20", "室内室外");
        map.put("column10_21", "厂商名称");
        map.put("column10_22", "距离");
        listmap.add(map);
        exportXlsx("通信信息联通4G",listmap,resultList,type,response);
    }
    private void exportXlsx(String fileName, List<Map<String, Object>> headListMap, List<ResultDto> resultList,Integer type,HttpServletResponse response) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            Map<String,Object> map=new HashMap<String,Object>();
            XSSFSheet sheet1 = wb.createSheet("通信信息");

            //创建表头
            excelNewUtil.createExcelHeader(wb, sheet1, headListMap);
            //填入表内容
            excelNewUtil.fillExcel(headListMap.size(),wb,sheet1,resultList,type);

            response.setContentType("application/x-download; charset=utf-8");//
            response.setHeader("Content-disposition", "attachment; filename="+new String( (fileName+".xlsx").getBytes("gb2312"), "ISO8859-1" ));
            //导出
            wb.write(response.getOutputStream());

            response.getOutputStream().close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
