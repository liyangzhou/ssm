package com.lee.ssm.resource;


import com.lee.ssm.dao.UserDao;
import com.lee.ssm.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author yz.li
 */
@RestController
@RequestMapping("file")
@Api(value = "user resource")
public class FileResource {

    @Autowired
    private FileService fileService;



    @ApiOperation("上传文件")
    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                InputStream stream = file.getInputStream();

                this.fileService.saveExcelToDB(fileName, stream);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok("success");

        } else {
            return ResponseEntity.ok("上传失败，因为文件为空.");
        }

    }
}
