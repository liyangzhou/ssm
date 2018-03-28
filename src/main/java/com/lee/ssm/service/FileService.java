package com.lee.ssm.service;

import java.io.InputStream;

public interface FileService {
    /**
     *
     * @param fileName
     * @param stream
     */
    void saveExcelToDB(String fileName, InputStream stream);
}
