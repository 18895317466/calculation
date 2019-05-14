package com.mingxun.calculation.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by admin on 2017/8/14.
 */
public class ExcelUtils {

    private Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    private static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";
    private static int DEFAULT_COLOUMN_WIDTH = 17;

    private static GroupTemplate gt;

    private static DecimalFormat decimalFormat = new DecimalFormat("#0.##");

    private static NumberFormat numberFormat = NumberFormat.getInstance();
//    static {
//        numberFormat.setGroupingUsed(false);
//    }


    static {
        try {
            gt = new GroupTemplate(new StringTemplateResourceLoader(), Configuration.defaultConfiguration());
        } catch (IOException e) {
            throw new RuntimeException("error in init beetl group template", e);
        }
    }

    public static  <T> List<T> parseExcel(File excel, Converter<T> converter) throws IOException {
        Workbook wb = getWorkbook(excel);
        Sheet sheet = wb.getSheetAt(0);
        List<T> list = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null
                    || row.getCell(0).toString() == null
                    || "".equals(row.getCell(0).toString().trim())) {
                break;
            }
            List<String> data = new ArrayList<>();
            toValues(row, data);

            list.add(converter.convert(data));
        }
        return list;
    }

    public static  <T> List<T> parseExcel(String filename, InputStream in, Converter<T> converter) throws IOException {
        Workbook wb = getWorkbook(filename, in);
        Sheet sheet = wb.getSheetAt(0);
        List<T> list = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null
                    || row.getCell(0).toString() == null
                    || "".equals(row.getCell(0).toString().trim())) {
                break;
            }
            List<String> data = new ArrayList<>();
            toValues(row, data);
             list.add(converter.convert(data));
//            System.out.println("\n\t lat "+ list.toArray());
        }
        return list;
    }

    private static void toValues(Row row, List<String> data) {
        try {
            for (int k = row.getFirstCellNum(); k < row.getPhysicalNumberOfCells(); k++) {
                int q=k;
                Cell cell = row.getCell(k);
                String value;
                if(cell == null ){
                    cell = row.getCell(k+2);
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                            Date theDate = cell.getDateCellValue();
                            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                            value = dff.format(theDate);
                        } else {
//                          // 将数字设置成文本格式
//
//                            String s = cell.getStringCellValue();
                            Double content = cell.getNumericCellValue();
                            String s = content.toString();
                            value = s ;
                            int index = s.indexOf('.');
//                            if (index > 0) {
//                                int i = Integer.valueOf(s.substring(index + 1));
//                                if (i > 0) {
//                                    value = s;
//                                } else {
//                                    value = s.substring(0, index);
//                                }
//                            } else {
//                                value = s;
//                            }
                        }

                        break;
                    default:
                        if("经度".equals(cell.toString()) || "纬度" .equals(cell.toString())){
                            value = "0";
                        }else{
                            value = cell.toString();
                        }
                        break;
                }
                data.add(value);
//            if (Strings.isNullOrEmpty(value))
//                break;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if(data.size() == 3){
            data.add(1,"0");
            data.add(2,"0");
        }
    }

    /**
     * @param template
     * @param root
     * @return
     * @throws IOException
     */
    public static Workbook export(String template, Map<String, Object> root) throws IOException {
        File f = new File(ExcelUtils.class.getResource("/excel").getFile(), template);
        if (f.getName().endsWith(".xls")) {
            return export(f, root);
        } else {
            return export2007(f, root);
        }
    }

    /**
     * @param root
     * @return
     * @throws IOException
     */
    public static Workbook export(File file, Map<String, Object> root) throws IOException {
        Workbook wb = new HSSFWorkbook(new FileInputStream(file));
        Sheet sheet = wb.getSheetAt(0);
        for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) {
                break;
            }
            String firstValue = row.getCell(0).getStringCellValue();
            if (null == firstValue || "".equals(firstValue)) {
                break;
            }

            if ("items.start".equals(firstValue)) {
                Row tempRow = sheet.getRow(++i);
                Collection<Object> items = (Collection<Object>) root.get("items");
                if (items != null) {
                    for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                        Cell cell = tempRow.getCell(k);
                        String value = cell.getStringCellValue();
                        value = parse(value, ImmutableMap.of("item", Iterables.getFirst(items, null), "position", 1));
                        if (k == 0) {
                            row.getCell(k).setCellValue(value);
                        } else {
                            Cell newCell = row.createCell(k);
                            CellStyle style = cell.getCellStyle();
                            if (style != null) {
                                cell.setCellStyle(style);
                            }
                            newCell.setCellValue(value);
                        }
                    }
                    int index = 1;
                    for (; index < items.size() - 1; index++) {
                        Object item = Iterables.get(items, index);
                        sheet.shiftRows(i, i + 1, 1, true, true);
                        i++;
                        Row newRow = sheet.createRow(i - 1);
                        for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                            Cell cell = newRow.createCell(k);
                            Cell oldCell = tempRow.getCell(k);
                            CellStyle style = oldCell.getCellStyle();
                            if (style != null) {
                                cell.setCellStyle(style);
                            }
                            String value = oldCell.getStringCellValue();
                            value = parse(value, ImmutableMap.of("item", item, "position", index));
                            cell.setCellValue(value);
                        }
                    }
                    if (items.size() >= 2) {
                        for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                            Cell cell = tempRow.getCell(k);
                            String value = cell.getStringCellValue();
                            value = parse(value, ImmutableMap.of("item", Iterables.getLast(items), "position", items.size()));
                            cell.setCellValue(value);
                        }
                    } else {
                        sheet.removeRow(tempRow);
                    }
                }

            } else {
                for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                    Cell c = row.getCell(j);
                    String value = c.getStringCellValue();
                    c.setCellValue(parse(value, root));
                }
            }
        }
        return wb;
    }

    private static Workbook getWorkbook(String filename, InputStream in) throws IOException {
        Workbook wb;
        if (filename.endsWith(".xls")) {
            wb = new HSSFWorkbook(in);
        } else {
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    private static Workbook getWorkbook(File file) throws IOException {
        return getWorkbook(file.getName(), new FileInputStream(file));
    }

    /**
     * @param root
     * @return
     * @throws IOException
     */
    public static Workbook export2007(File file, Map<String, Object> root) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = wb.getSheetAt(0);
        for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) {
                break;
            }
            String firstValue = row.getCell(0).getStringCellValue();
            if (null == firstValue || "".equals(firstValue)) {
                break;
            }

            if ("items.start".equals(firstValue)) {
                XSSFRow tempRow = sheet.getRow(++i);
                Collection<Object> items = (Collection<Object>) root.get("items");
                if (items != null) {
                    for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                        XSSFCell cell = tempRow.getCell(k);
                        String value = cell.getStringCellValue();
                        value = parse(value, ImmutableMap.of("item", Iterables.getFirst(items, null), "position", 1));
                        if (k == 0) {
                            row.getCell(k).setCellValue(value);
                        } else {
                            XSSFCell newCell = row.createCell(k);
                            XSSFCellStyle style = cell.getCellStyle();
                            if (style != null) {
                                cell.setCellStyle(style);
                            }
                            newCell.setCellValue(value);
                        }
                    }
                    int index = 1;
                    for (; index < items.size() - 1; index++) {
                        Object item = Iterables.get(items, index);
                        sheet.shiftRows(i, i + 1, 1, true, true);
                        i++;
                        XSSFRow newRow = sheet.createRow(i - 1);
                        for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                            XSSFCell cell = newRow.createCell(k);
                            XSSFCell oldCell = tempRow.getCell(k);
                            XSSFCellStyle style = oldCell.getCellStyle();
                            if (style != null) {
                                cell.setCellStyle(style);
                            }
                            String value = oldCell.getStringCellValue();
                            value = parse(value, ImmutableMap.of("item", item, "position", index));
                            cell.setCellValue(value);
                        }
                    }
                    if (items.size() >= 2) {
                        for (int k = tempRow.getFirstCellNum(); k < tempRow.getPhysicalNumberOfCells(); k++) {
                            XSSFCell cell = tempRow.getCell(k);
                            String value = cell.getStringCellValue();
                            value = parse(value, ImmutableMap.of("item", Iterables.getLast(items), "position", items.size()));
                            cell.setCellValue(value);
                        }
                    } else {
                        sheet.removeRow(tempRow);
                    }
                }

            } else {
                for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                    XSSFCell c = row.getCell(j);
                    String value = c.getStringCellValue();
                    c.setCellValue(parse(value, root));
                }
            }
        }
        return wb;
    }

    private static String parse(String expression, Map<String, Object> root) {
        Template template = gt.getTemplate(expression);
        template.binding(root);
        return template.render();
    }
//
//    public static void main(String[] args) throws IOException {
//        String fileName = "/Users/huwhy/Documents/test.xlsx";
//        Map<String, Object> root = ImmutableMap.of("items", Lists.newArrayList("111", "222", "333", "444"));
//        Workbook wb = export2007(new File(fileName), root);
//        FileOutputStream out = new FileOutputStream(new File("/Users/huwhy/Documents/test2.xlsx"));
//        wb.write(out);
//        out.flush();
//        out.close();
//    }

    public interface Converter<T> {
        T convert(List<String> row);
    }


    /**
     * 导出Excel
     *
     * @param
     * @param headMap
     * @param jsonArray
     * @param datePattern
     * @param colWidth
     * @param out
     */
    public void exportExcel(Map<String, String> headMap, JSONArray jsonArray, String datePattern,
                            int colWidth, OutputStream out) {
        if (datePattern == null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        //创建一个工作簿对象
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        workbook.setCompressTempFiles(true);

        //设置标题行样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setBoldweight((short) 70);
        titleStyle.setFont(font);

        //设置单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);

        //创建一个工作表
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();

        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;
        int[] arrColWidth = new int[headMap.size()];
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext(); ) {
            String filedName = iter.next();
            properties[ii] = filedName;
            headers[ii] = headMap.get(filedName);
            int bytes = filedName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        int rowIndex = 0;
        if (jsonArray.isEmpty()) {
            SXSSFRow headerRow = (SXSSFRow) sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
        }
        for (Object obj : jsonArray) {
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) {
                    sheet = (SXSSFSheet) workbook.createSheet();
                }
                SXSSFRow headerRow = (SXSSFRow) sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }
                rowIndex = 1;
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);

            for (int i = 0; i < properties.length; i++) {
                SXSSFCell newCell = (SXSSFCell) dataRow.createCell(i);
                Object o = jo.get(properties[i]);
                String cellValue = "";
                if (o == null) {
                    cellValue = "";
                } else if (o instanceof Date) {
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                } else {
                    cellValue = o.toString();
                }
                newCell.setCellValue(cellValue);
                /*newCell.setCellStyle(cellStyle);*/
            }
            rowIndex++;
        }
        try {
            workbook.write(out);
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void batchExport(List<BatchExportDto> list, String datePattern, int colWidth, OutputStream out) {
//
//        if (datePattern == null) {
//            datePattern = DEFAULT_DATE_PATTERN;
//        }
//        //工作簿   指定最多访问多少行。  创建excel
//        SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
//        //设置临时文件是否被压缩
//        workbook.setCompressTempFiles(true);
//        //创建一个新的单元格样式并添加到工作薄的样式表中
//        CellStyle titleStyle = workbook.createCellStyle();
//        //设置单元格的水平对齐方式 。
//        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        //单元格内容的中对齐
//        titleStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
//
//        //创建一个新的字体并添加 到工作薄的字体样式表中；
//        Font font = workbook.createFont();
//        font.setFontHeightInPoints((short) 20);
//        font.setBoldweight((short) 70);
//        titleStyle.setFont(font);
//
//        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//        Font headerFont = workbook.createFont();
//        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        cellStyle.setFont(headerFont);
//
//        for(int m = 0, size = list.size() ; m < size; m++ ){
//            Map<String, String> headMap = list.get(m).getHeadMap();
//            JSONArray jsonArray = list.get(m).getJsonArray();
//
//            //创建工作表
//            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(list.get(m).getSheetTitle());
//
//            int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;
//            int[] arrColWidth = new int[headMap.size()];
//            String[] properties = new String[headMap.size()];
//            String[] headers = new String[headMap.size()];
//            int ii = 0;
//            for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext(); ) {
//                String filedName = iter.next();
//                properties[ii] = filedName;
//                headers[ii] = headMap.get(filedName);
//                int bytes = filedName.getBytes().length;
//                arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
//                sheet.setColumnWidth(ii, arrColWidth[ii] * 200);
//                // sheet.setColumnWidth(ii,120);
//                ii++;
//            }
//            int rowIndex = 0;
//
//            if (jsonArray.isEmpty()) {
//                SXSSFRow headerRow = (SXSSFRow) sheet.createRow(0);
//                for (int i = 0; i < headers.length; i++) {
//                    headerRow.createCell(i).setCellValue(headers[i]);
//                }
//            }
//            for (Object obj : jsonArray) {
//                if (rowIndex == 65535 || rowIndex == 0) {
//                    if (rowIndex != 0) {
//                        sheet = (SXSSFSheet) workbook.createSheet();
//                    }
//                    SXSSFRow headerRow = (SXSSFRow) sheet.createRow(0);
//                    for (int i = 0; i < headers.length; i++) {
//                        headerRow.createCell(i).setCellValue(headers[i]);
//                    }
//                    rowIndex = 1;
//                }
//                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
//                SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
//
//                for (int i = 0; i < properties.length; i++) {
//                    SXSSFCell newCell = (SXSSFCell) dataRow.createCell(i);
//                    Object o = jo.get(properties[i]);
//                    String cellValue = "";
//                    if (o == null) {
//                        cellValue = "";
//                    } else if (o instanceof Date) {
//                        cellValue = new SimpleDateFormat(datePattern).format(o);
//                    } else {
//                        cellValue = o.toString();
//                    }
//                    newCell.setCellValue(cellValue);
//                /*newCell.setCellStyle(cellStyle);*/
//                }
//                rowIndex++;
//            }
//
//            }
//            try {
//                workbook.write(out);
//                workbook.dispose();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//    }


    public void outPut(HttpServletResponse response,
                       ByteArrayOutputStream out,
                       String title) {
        try {
            byte[] content = out.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(is.available());
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    public void download(HttpServletRequest request, HttpServletResponse response, String path , String filename, String realName) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="
                    + filename);
            if (!Strings.isNullOrEmpty(realName)) {
                response.setHeader("Content-Disposition", "attachment;fileName="
                        + URLEncoder.encode(realName, "utf-8"));
            }

            if (Strings.isNullOrEmpty(path)) {
                path = request.getSession().getServletContext().getRealPath("/") + "excel/";
            }
            System.out.println("path=" + path);
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + filename));
            OutputStream os = response.getOutputStream();
            try {
                byte[] b = new byte[2048];
                b.toString().getBytes("utf-8");
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
            } finally {
                os.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
