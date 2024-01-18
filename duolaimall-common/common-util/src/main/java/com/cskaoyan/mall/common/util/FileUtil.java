package com.cskaoyan.mall.common.util;

public class FileUtil {
    public  static String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return "." + filename.substring(index + 1);
        }
    }
}
