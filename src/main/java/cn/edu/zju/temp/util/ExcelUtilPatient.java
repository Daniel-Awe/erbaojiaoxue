package cn.edu.zju.temp.util;

import cn.edu.zju.temp.entity.to.PatientTo;
import cn.edu.zju.temp.entity.to.QuestionTo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/11 15:06
 * @version: 1.0
 */
public class ExcelUtilPatient {
    // 定义范型，这样就能传对应的实体类来封装excel数据
    private PatientTo t;

    public ExcelUtilPatient(PatientTo t) {
        this.t = t;
    }

    /**
     *@ClassName: ExcelUtil
     *@Description: 传入excel解析excel文件，提取出信息
     *@Params: InputStream is, String fileName
     *@Return:
     *@Author xxw
     *@Date 2020/12/15
     */

    public List<Map> AnalysisExcel(InputStream is, String fileName) throws IOException {

        List<Map> list = new ArrayList<>();
        Workbook workbook = createWorkbookByExcelType(is, fileName); // 创建工作簿

        Sheet sheet = workbook.getSheetAt(0);
        Row row = null;

        // 获取最大行数
        int maxRowNum = sheet.getLastRowNum();

        // 获取第一行
        row = sheet.getRow(0);

        // 获取最大列数
        int maxColNum = row.getLastCellNum();

        List<String> arrayList = new ArrayList<>();

        Field[] declaredFields = t.getClass().getDeclaredFields();

        // 把需要封装的实体类的属性名存入arrayList
        for (int i = 0; i < declaredFields.length; i++) {
            String name = declaredFields[i].getName();
            arrayList.add(name);
        }

        // 循环遍历excel表格，把每条数据封装成 map集合，再放入list集合中
        for (int i = 0; i <= maxRowNum; i++) {
            Map<String, String> map = new HashMap<>();

            row = sheet.getRow(i);

            if (row != null){
                for (int j = 0; j < maxColNum; j++) {
                    String cellData = (String)getCellFormatValue(row.getCell(j));
                    map.put(arrayList.get(j), cellData); // map 封装
                }
                list.add(map); // map存入list
            }
        }
        return list;
    }


    //根据传入的文件名后缀判断是xls还是xlsx
    public static Workbook createWorkbookByExcelType(InputStream inputStream, String fileName){
        Workbook wb = null;
        if(fileName == null){
            return null;
        }
        String extString = fileName.substring(fileName.lastIndexOf("."));
        InputStream is = null;
        try {
            is = inputStream;
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is); // 2003版本 .xls
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is); // 2007版本 .xlsx
            }else{
                return wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    // 用于获取表格中的数据方法
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
