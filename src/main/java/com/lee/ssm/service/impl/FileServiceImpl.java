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

            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                XSSFRow row = sheet.getRow(j);
                User u = new User();




                for (int r = 0; r < row.getLastCellNum(); r++) {
                    if (row.getCell(r).getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.println(row.getCell(r).getNumericCellValue());
                        u.setAge((int)row.getCell(r).getNumericCellValue());
                    }
                    if (row.getCell(r).getCellTypeEnum() == CellType.STRING) {
                        System.out.println(row.getCell(r).getStringCellValue());
                        u.setName(row.getCell(r).getStringCellValue());
                    }
                }
                users.add(u);
            }

        }


// this is dev
        // save to db
        this.userMapper.insert(users.get(0));
    }
}