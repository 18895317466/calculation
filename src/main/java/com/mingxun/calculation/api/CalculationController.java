package com.mingxun.calculation.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mingxun.calculation.dto.ResultDto;
import com.mingxun.calculation.manager.BaseStationManager;
import com.mingxun.calculation.model.BaseStation;
import com.mingxun.calculation.model.BaseStationTerm;
import com.mingxun.calculation.services.CalculationServices;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wang.daoziwo.cloud.interfaces.Json;
import wang.daoziwo.cloud.interfaces.Paging;

import java.io.IOException;
import java.util.List;

/**
 * Created by ouyang on 2019/5/6.
 */
@RestController
public class CalculationController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CalculationController.class);

   @Autowired
   private CalculationServices calculationServices;

   @Autowired
   private BaseStationManager baseStationManager;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Json batchInsertData(@RequestParam("mobile2G") MultipartFile mobile2G) throws IOException {
        logger.info("\n\t size ");
        return calculationServices.batchInsertData(mobile2G);
    }

    @RequestMapping (value = "search")
    public Json search1(@RequestParam("distance") Integer distance,@RequestParam("type") Integer type) throws IOException {
        List<ResultDto> resultDtoList =calculationServices.search(distance,type);
        String str =JSON.toJSONString(resultDtoList, SerializerFeature.DisableCircularReferenceDetect);
        return Json.SUCCESS().setModel(str);
    }
    @RequestMapping (value = "find-page")
    public Json findPaging(@RequestParam(value = "stationName",required = false)String stationName,
                           @RequestParam(value = "type")Integer type,
                           @RequestParam(value = "pageNum",defaultValue = "1",required = false)Long pageNum,
                           @RequestParam(value = "pageSize",defaultValue = "10",required = false)Long pageSize ){
        BaseStationTerm term = new BaseStationTerm();
        term.setStationName(stationName);
        term.setType(type);
        term.setPageNum(pageNum);
        term.setPageSize(pageSize);
        Paging<BaseStation> paging = baseStationManager.findPaging(term);
        return Json.SUCCESS().setModel(paging).setMessage("查询成功");
    }
}
