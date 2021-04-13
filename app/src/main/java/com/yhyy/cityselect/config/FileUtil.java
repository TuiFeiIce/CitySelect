package com.yhyy.cityselect.config;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtil {
    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File saveLogFile(Context context, String fileName) {
        String filePath = "";
        if (filePath == null || filePath.trim().length() == 0) {
            filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/YHYY/";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fullFile = new File(filePath, fileName);
        return fullFile;
    }
}
