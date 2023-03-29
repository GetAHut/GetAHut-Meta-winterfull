package com.winterfull.utils;

import java.io.File;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
public class FileCommonUtils {

    public static final String DOT = ".";

    public static final String CONNECTOR = "-";
    /**
     * 根据源文件创建新文件路径
     * @param sourceFile
     * @param traceId
     * @param suffix
     * @return
     */
    public static String filePathAppend(File sourceFile, String type, String traceId, String suffix){
        String filePath = sourceFile.getAbsolutePath();
        if (filePath.contains(DOT)){
            filePath = filePath.substring(0, filePath.lastIndexOf(DOT));
            return filePath + CONNECTOR + type + CONNECTOR + traceId + DOT + suffix;
        }
        // TODO
        throw new RuntimeException(String.format("[traceId = %s] filePath error", traceId));
    }

}
