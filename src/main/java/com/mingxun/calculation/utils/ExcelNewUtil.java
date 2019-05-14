package com.mingxun.calculation.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mingxun.calculation.dto.ResultDto;
import com.mingxun.calculation.model.BaseStation;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
/*
 * 使用方式示例：
 *
 * public void export(){
 * 		List<Map<String, Object>> list=null;//表内容集合，从数据库查，需要合并的列要进行分组，否则需要做合并的时候可能达不到理想结果
 * 		List<Map<String, Object>> listmap=new ArrayList<Map<String, Object>>();
 * 		Map<String,Object> map=new LinkedHashMap<String,Object>();
 * 		//标题
 *     	map.put("head_C11", findYear+"年单位通车年限统计");
 *      listmap.add(map);
 *      //第一行列头（_*表示表头从第几列开始，默认0，_C*表示合并*列，_R*表示合并*行）
 *      map=new LinkedHashMap<String,Object>();
 *      map.put("column1_C2_R2", "管养单位");
 *      map.put("column2_R2", "通车里程（km）");
 *      map.put("column3_C8", "通车时间（年）");
 *      listmap.add(map);
 *      //第二行列头
 *      map=new LinkedHashMap<String,Object>();
 *      map.put("column4_3", "10年以上");
 *      map.put("column5_4", "占比%");
 *      map.put("column6_5", "5-10年");
 *      map.put("column7_6", "占比%");
 *      map.put("column8_7", "3-5年");
 *      map.put("column9_8", "占比%");
 *      map.put("column10_9", "3年以下");
 *      map.put("column11_10", "占比%");
 *      listmap.add(map);
 *      //sql语句查询的顺序
 *      String[] colOrder={"nature","remark","mileage","tenMileage","tenZb","fiveToTenMileage","fiveToTenZb","threeToFiveMileage","threeToFiveZb","threeLessThanMileage","threeLessThanZb"};
 *      //可能需要做跨行合并的行，将某一列中相同内容的行进行合并
 *      String[] mergeCols={"nature"};
 *      exportXlsx(findYear+"年单位通车年限统计",listmap,list,mergeCols,colOrder);
 * }
 *
 *  private void exportXlsx(String fileName,List<Map<String, Object>> headListMap,List<Map<String, Object>> dataListMap,String[] mergeCols,String[] colOrder) {
 *          try {
 *  			XSSFWorkbook wb = new XSSFWorkbook();
 *  			Map<String,Object> map=new HashMap<String,Object>();
 *  			XSSFSheet sheet1 = wb.createSheet("统计");
 *
 *  			//创建表头
 *  			ExcelNewUtil.createExcelHeader(wb, sheet1, headListMap);
 *  			//填入表内容
 *  			ExcelNewUtil.fillExcel(headListMap.size(),mergeCols,colOrder,wb,sheet1,dataListMap);
 *
 *  			response.setContentType("application/x-download; charset=utf-8");//
 *  			response.setHeader("Content-disposition", "attachment; filename="+new String( (fileName+".xlsx").getBytes("gb2312"), "ISO8859-1" ));
 *  			//导出
 *  			wb.write(response.getOutputStream());
 *
 *  			response.getOutputStream().close();
 *
 *  		} catch (FileNotFoundException e) {
 *  			e.printStackTrace();
 *  		} catch (IOException e) {
 *  			e.printStackTrace();
 *  		}
 *
 *  	}
 *
 * */
/**
 * Excel合并单元格的导出Tool
 * @ClassName: ExcelNewUtil
 * @Description: TODO(适合在需要做合并单元格的情况下，减少重复代码。普通导出建议原方法导出)
 * @author 周玉波
 * @date 2018年7月10日 上午8:45:44
 *
 */
@Component
public class ExcelNewUtil {
    /**
     * 设置表头
     * @param wb
     * @param sheet
     * @param headListMap （_*表示列头从第几列开始，默认0，_C*表示合并*列，_R*表示合并*行）
     * CellRangeAddress(起始行号，结束行号，起始列号，结束列号)
     * 合并行或列
     */
    public  void createExcelHeader(XSSFWorkbook wb,XSSFSheet sheet,List<Map<String, Object>> headListMap){
        sheet.setDefaultColumnWidth(7);
        XSSFRow sr = null;
        XSSFCell sc = null;

        int j=0;//行
        //遍历表头集合
        for (int i = 0; i < headListMap.size(); i++) {
            Map<String, Object> map=headListMap.get(i);
            //创建行
            sr = sheet.createRow(i);
            sc = null;

            int k=0;//列
            int front=0;//上一次的单元格位置
            //表头
            for (String key : map.keySet()) {
                //如果是标题
                if(key.startsWith("head")){
                    int r=0,c=0;
                    //判断是否跨行或者跨列
                    if(key.indexOf("_R")>-1||key.indexOf("_C")>-1){
                        String[] keys=key.split("_");//分组
                        for (int l = 0; l < keys.length; l++) {
                            //跨行
                            if(keys[l].startsWith("R")){
                                //实际值从0开始，因此减一
                                r=Integer.parseInt(keys[l].replaceAll("R", ""))-1;
                            }
                            //跨列
                            else if(keys[l].startsWith("C")){
                                c=Integer.parseInt(keys[l].replaceAll("C", ""))-1;
                            }
                        }
                    }
                    //创建单元格
                    sc = sr.createCell(k);
                    sc.setCellValue(map.get(key).toString());
                    //合并列、合并行
                    sheet.addMergedRegion(new CellRangeAddress(0, r, 0, c));
                    //设置标题样式
                    sc.setCellStyle(createHeaderStyle(wb));
                    //设置高度
                    sr.setHeight((short) 800);
                }
                //列头
                else{
                    int r=0,c=0;
                    //判断是否跨行或者跨列
                    if(key.indexOf("_R")>-1||key.indexOf("_C")>-1){
                        String[] keys=key.split("_");//分组
                        for (int l = 0; l < keys.length; l++) {
                            //跨行
                            if(keys[l].startsWith("R")){
                                r=Integer.parseInt(keys[l].replaceAll("R", ""))-1;
                            }
                            //跨列
                            else if(keys[l].startsWith("C")){
                                c=Integer.parseInt(keys[l].replaceAll("C", ""))-1;
                                //跨列会让跨过的单元格没有边框线，设置边框线
                                setCellBorder(k+1,k+c,sr,createBorderStyle(wb));
                                //设置单元格宽度
                                sheet.setColumnWidth(k+c, 10 * 256);
                            }
                        }

                    }
                    //如果前面有跨行的列，第二行需要单元格位置，
                    //如上一行第一列为column_R2，垮了两行，下面那行如果有列，则需要设置列起始位置，如column_1（单元格位置从0开始），从第二列开始
                    else{
                        if(key.indexOf("_")>-1){
                            k=Integer.parseInt(key.split("_")[1]);
                            //判断当前位置与前一次位置是否是连续的，非连续则证明中间有跨行的列，需要设置被跨行的单元格边框线
                            if(k-front>1){
                                setCellBorder(front,k-1,sr,createBorderStyle(wb));
                            }
                            //赋值本次坐标
                            front=k;
                        }
                    }
                    //创建单元格
                    sc = sr.createCell(k);
                    sc.setCellValue(map.get(key).toString());
                    //设置跨行或跨列
                    sheet.addMergedRegion(new CellRangeAddress(j, j+r, k, k+c));
                    //设置单元格宽度
                    sheet.setColumnWidth(k, 10 * 256);
                    //设置单元格高度
                    sr.setHeight((short) 600);
                    //设置单元格样式
                    sc.setCellStyle(createBorderStyle(wb));
                    k=k+c;
                    k++;
                }
            }
            j++;
        }

    }
    private  String[] setRowStyle={"全省合计","全省总计","总计","小计"};//如果包含这些的行，字体样式设为粗体
    /**
     * 表内容
     * @param startRow 开始行
//     * @param mergeCols 需要做跨行合并的列（数据库查询出的列名）
//     * @param colOrder 列顺序（数据库查询出的列名）Map无序，需要设定排列顺序
     * @param wb
     * @param sheet
     * @param list 表内容集合
     */
    public  void fillExcel(int startRow,XSSFWorkbook wb,XSSFSheet sheet,List<ResultDto> list,Integer type) {
        @SuppressWarnings("deprecation")
        XSSFRow sr = null;
        XSSFCell sc = null;
        String mer="";//记录首次出现的行
        // 记录行数
        int contentNum = 1 ;
        for (int i = 0; i <list.size(); i++) {
            int [] array ;
            if(type ==1){
                array = new int[]{list.get(i).getYd4GList().size(),list.get(i).getDx4GList().size(),list.get(i).getLt4GList().size()};
            }else if( type == 2){
                array = new int[]{list.get(i).getYd2GList().size(),list.get(i).getDx4GList().size(),list.get(i).getLt4GList().size()};
            }else if( type == 3){
                array = new int[]{list.get(i).getYd2GList().size(),list.get(i).getYd4GList().size(),list.get(i).getLt4GList().size()};
            }else {
                array = new int[]{list.get(i).getYd2GList().size(),list.get(i).getYd4GList().size(),list.get(i).getDx4GList().size()};
            }

            Arrays.sort(array);
            int rowsNum = 1;
            if(array[array.length-1]>1) rowsNum = array[array.length-1];
            for(int j=1;j <= rowsNum;j++){
                //创建行
                sr = sheet.createRow(contentNum);
                if(j < rowsNum){
                    contentNum++;
                }
                //创建单元格
                if(j == 1){
                    sc = sr.createCell(0);
                    sc.setCellValue(list.get(i).getStationName());
                    sc = sr.createCell(1);
                    sc.setCellValue(list.get(i).getLng());
                    sc = sr.createCell(2);
                    sc.setCellValue(list.get(i).getLat());
                    sc = sr.createCell(3);
                    sc.setCellValue(list.get(i).getDoor());
                    sc = sr.createCell(4);
                    sc.setCellValue(list.get(i).getManufacturer());
                }
                if(type ==1 ){
                    List<BaseStation> Yd4GList = list.get(i).getYd4GList();
                    if(Yd4GList.size() > 0 && j< Yd4GList.size()  ){
                        setCells1(Yd4GList.get(j-1),Yd4GList.size(),j,sr,sc);
                    }

                    List<BaseStation> Dx4GList = list.get(i).getDx4GList();
                    if(Dx4GList.size() > 0 && j< Dx4GList.size()){
                        setCells2(Dx4GList.get(j-1),Dx4GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Lt4GList = list.get(i).getYd4GList();
                    if(Lt4GList.size() > 0 && j< Lt4GList.size()){
                        setCells3(Lt4GList.get(j-1),Lt4GList.size(),j,sr,sc);
                    }

                }else if( type == 2){

                    List<BaseStation> Yd2GList = list.get(i).getYd2GList();
                    if(Yd2GList.size() > 0 && j< Yd2GList.size()){
                        setCells1(Yd2GList.get(j-1),Yd2GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Dx4GList = list.get(i).getDx4GList();
                    if(Dx4GList.size() > 0 && j< Dx4GList.size()){
                        setCells2(Dx4GList.get(j-1),Dx4GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Lt4GList = list.get(i).getLt4GList();
                    if(Lt4GList.size() > 0 && j< Lt4GList.size()){
                        setCells3(Lt4GList.get(j-1),Lt4GList.size(),j,sr,sc);
                    }
                }else if( type == 3){
                    List<BaseStation> Yd4GList = list.get(i).getYd4GList();
                    if(Yd4GList.size() > 0 &&  j< Yd4GList.size()){
                        setCells1(Yd4GList.get(j-1),Yd4GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Yd2GList = list.get(i).getYd2GList();
                    if(j < Yd2GList.size() && Yd2GList.size() > 0 ){
                        setCells2(Yd2GList.get(j-1),Yd2GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Lt4GList = list.get(i).getLt4GList();
                    if(j< Lt4GList.size() && Lt4GList.size() > 0){
                        setCells3(Lt4GList.get(j-1),Lt4GList.size(),j,sr,sc);
                    }
                }else{
                    List<BaseStation> Yd4GList = list.get(i).getYd4GList();
                    if(j< Yd4GList.size() && Yd4GList.size() > 0 ){
                        setCells1(Yd4GList.get(j-1),Yd4GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Dx4GList = list.get(i).getDx4GList();
                    if(Dx4GList.size() > 0 && j< Dx4GList.size()){
                        setCells2(Dx4GList.get(j-1),Dx4GList.size(),j,sr,sc);
                    }
                    List<BaseStation> Yd2GList = list.get(i).getYd2GList();
                    if(j< Yd2GList.size() &&  Yd2GList.size() > 0){
                        setCells3(Yd2GList.get(j-1),Yd2GList.size(),j,sr,sc);
                    }
                }
            }
//               //设置单元格样式
//               sc.setCellStyle(createBorderStyle(wb));

        }
    }


    public void setCells1( BaseStation baseStation,int size,int j,XSSFRow sr, XSSFCell sc){
        if(j< size){
            sc = sr.createCell(5);
            sc.setCellValue(baseStation.getStationName());
            sc = sr.createCell(6);
            sc.setCellValue(baseStation.getLng());
            sc = sr.createCell(7);
            sc.setCellValue(baseStation.getLat());
            sc = sr.createCell(8);
            sc.setCellValue(baseStation.getDoor());
            sc = sr.createCell(9);
            sc.setCellValue(baseStation.getManufacturer());
            sc = sr.createCell(10);
            sc.setCellValue(baseStation.getDistance());
        }
    }
    public void setCells2( BaseStation baseStation,int size,int j,XSSFRow sr, XSSFCell sc){
        if(j< size){
            sc = sr.createCell(11);
            sc.setCellValue(baseStation.getStationName());
            sc = sr.createCell(12);
            sc.setCellValue(baseStation.getLng());
            sc = sr.createCell(13);
            sc.setCellValue(baseStation.getLat());
            sc = sr.createCell(14);
            sc.setCellValue(baseStation.getDoor());
            sc = sr.createCell(15);
            sc.setCellValue(baseStation.getManufacturer());
            sc = sr.createCell(16);
            sc.setCellValue(baseStation.getDistance());
        }
    }

    public void setCells3( BaseStation baseStation,int size,int j,XSSFRow sr, XSSFCell sc){
        if(j< size ){
            sc = sr.createCell(17);
            sc.setCellValue(baseStation.getStationName());
            sc = sr.createCell(18);
            sc.setCellValue(baseStation.getLng());
            sc = sr.createCell(19);
            sc.setCellValue(baseStation.getLat());
            sc = sr.createCell(20);
            sc.setCellValue(baseStation.getDoor());
            sc = sr.createCell(21);
            sc.setCellValue(baseStation.getManufacturer());
            sc = sr.createCell(22);
            sc.setCellValue(baseStation.getDistance());
        }
    }
    /**
     * 设置单元格
     * @param wb
     * @return
     */
    public  CellStyle createAlignStyle(XSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //字体居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中

        return style;
    }
    /**
     * 设置边框样式
     * @param wb
     * @return
     */
    public  CellStyle createBorderStyle(XSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //字体居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中

        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);//设置字体大小
        style.setFont(font);
        return style;
    }
    /**
     * 设置边框样式 粗体
     * @param wb
     * @return
     */
    public  CellStyle createBorderBoldStyle(XSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //字体居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中

        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);//设置字体大小
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        style.setFont(font);
        return style;
    }
    /**
     * 设置边框
     * @param start 开始列
     * @param end 结束列
     * @param row 行
     * @param style
     */
    public  void setCellBorder(int start, int end, XSSFRow row,  CellStyle style) {
        for(int i=start;i<=end;i++){
            XSSFCell cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(style);
        }
    }
    /**
     * 设置边框
     * @param start 开始列
     * @param end 结束列
     * @param row 行
     * @param style
     */
    public  void setCellBorder1(int start, int end, XSSFRow row,  CellStyle style) {
        for(int i=end+1;i>=start;i--){
            XSSFCell cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(style);
        }
    }
    /**
     * 设置边框
//     * @param start 开始列
//     * @param end 结束列
     * @param row 行
     * @param val 值
     * @param style
     */
    public  void setCellBorder(int i, XSSFRow row,  CellStyle style, String val) {
        XSSFCell cell = row.createCell(i);
        cell.setCellValue(val);
        cell.setCellStyle(style);
    }
    /**
     * 创建头部样式
     * @param wb
     * @return
     */
    public  CellStyle createHeaderStyle(XSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
		/*style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
*/		style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //字体居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
        XSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short) 16);//设置字体大小
        style.setFont(font);
        return style;
    }
    /**
     * 创建头部样式
     * @param wb
     * @param size 字体大小
     * @return
     */
    public  CellStyle createHeaderStyle(XSSFWorkbook wb,int size){
        CellStyle style = wb.createCellStyle();
		/*style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
*/		style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //字体居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
        XSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short) size);//设置字体大小
        style.setFont(font);
        return style;
    }
}
