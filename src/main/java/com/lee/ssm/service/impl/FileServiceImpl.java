package com.lee.ssm.service.impl;

import com.lee.ssm.mapper.UserMapper;
import com.lee.ssm.model.User;
import com.lee.ssm.service.FileService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yz.li
 * @date: 2018/3/27
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveExcelToDB(String fileName, InputStream stream) {
        // 解析Excel
        // 创建对Excel工作簿文件的引用­
        XSSFWorkbook wookbook = null;
        try {
            wookbook = new XSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sheetNum = wookbook.getNumberOfSheets();
        XSSFSheet sheet = null;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < sheetNum; i++) {
            sheet = wookbook.getSheetAt(i);
            System.out.println("sheet name is :[" + sheet.getSheetName() + "]");

            // 固定列获取方式
            /*for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                XSSFRow row = sheet.getRow(j);
                User u = new User();


                for (int r = 0; r < row.getLastCellNum(); r++) {
                    if (row.getCell(r).getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.println(row.getCell(r).getNumericCellValue());
                        u.setAge((int) row.getCell(r).getNumericCellValue());
                    }
                    if (row.getCell(r).getCellTypeEnum() == CellType.STRING) {
                        System.out.println(row.getCell(r).getStringCellValue());
                        u.setName(row.getCell(r).getStringCellValue());
                    }
                }
                users.add(u);
            }*/

            // 反射机制自动获取对应的内容
            users = readFromExcel(sheet, User.class);

        }

        // save to db
        for (User u : users) {
            this.userMapper.insert(u);
        }
    }

    public List readFromExcel(XSSFSheet sheet, Class clazz) {

        List result = new ArrayList();

        // 获得该类的所有属性
        Field[] fields = clazz.getDeclaredFields();

        // 获取表格的总行数
        int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        if (rowCount < 1) {
            return result;
        }
        // 获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();
        // 读取表头信息,确定需要用的方法名---set方法
        // 用于存储方法名
        String[] methodNames = new String[columnCount]; // 表头列数即为需要的set方法个数
        // 用于存储属性类型
        String[] fieldTypes = new String[columnCount];
        // 获得表头行对象
        XSSFRow titleRow = sheet.getRow(0);
        // 遍历表头列
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            // 某一列的内容
            String data = titleRow.getCell(columnIndex).toString();
            // 使其首字母大写
            String Udata = Character.toUpperCase(data.charAt(0)) + data.substring(1, data.length());

            methodNames[columnIndex] = "set" + Udata;
            // 遍历属性数组
            for (int i = 0; i < fields.length; i++) {
                // 属性与表头相等
                if (data.equals(fields[i].getName())) {
                    // 将属性类型放到数组中
                    fieldTypes[columnIndex] = fields[i].getType().getName();
                }
            }
        }
        // 逐行读取数据 从1开始 忽略表头
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            // 获得行对象
            XSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                Object obj = null;
                // 实例化该泛型类的对象一个对象
                try {
                    obj = clazz.newInstance();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                // 获得本行中各单元格中的数据
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    String data = row.getCell(columnIndex).toString();
                    // 获取要调用方法的方法名
                    String methodName = methodNames[columnIndex];
                    Method method = null;
                    try {
                        // 这部分可自己扩展
                        if (fieldTypes[columnIndex].equals("java.lang.String")) {
                            // 设置要执行的方法--set方法参数为String
                            method = clazz.getDeclaredMethod(methodName, String.class);
                            // 执行该方法
                            method.invoke(obj, data);
                        } else if (fieldTypes[columnIndex].equals("java.lang.Integer")) {
                            // 设置要执行的方法--set方法参数为int
                            method = clazz.getDeclaredMethod(methodName, Integer.class);
                            double dataDouble = Double.parseDouble(data);
                            int dataInt = (int) dataDouble;

                            method.invoke(obj, dataInt); // 执行该方法
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                result.add(obj);
            }
        }
        return result;
    }
}