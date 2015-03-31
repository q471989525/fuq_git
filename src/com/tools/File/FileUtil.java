package com.tools.File;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fuq
 */
public class FileUtil {

    /**
     * 读取文
     *
     * @param file
     */
    public void MyFileReader(File file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 文件写入
     *
     * @param file 要写入的文件
     * @param str 要写人的数据
     */
    public void MyFileWrit(File file, String str) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            bw.write(str);
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileUtil().MyFileWrit(new File("C:\\test.txt"), "测试 要怎么样");
        new FileUtil().MyFileReader(new File("C:\\test.txt"));

    }
}
