package com.tools.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*压缩文件、夹*/
public class Zip {

    private String currentZipFilePath;
    private String sourceFilePath;
    private ZipOutputStream zos;
    private FileInputStream fis;

    // 在当前条目中写入具体内容
    private void writeToEntryZip(String filePath) {
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        byte[] buff = new byte[1024];
        int len = 0;
        try {
            while ((len = fis.read(buff)) != -1) {
                zos.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 添加文件条目
    private void addFileEntryZip(String fileName) {
        try {
            zos.putNextEntry(new ZipEntry(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDirectoryEntryZip(String directoryName) {
        try {
            zos.putNextEntry(new ZipEntry(directoryName + "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 遍历文件夹
//    public void listMyDirectory(String filePath) {
//        File f = new File(filePath);
//        File[] files = f.listFiles();
//        if (files != null) {
//            for (File currentFile : files) {
//                // 设置条目名称(此步骤非常关键)
//                String entryName = currentFile.getAbsolutePath().split(":")[1].substring(1);
//                // 获取文件物理路径
//                String absolutePath = currentFile.getAbsolutePath();
//                if (currentFile.isDirectory()) {
//                    addDirectoryEntryZip(entryName);
//                    //进行递归调用
//                    listMyDirectory(absolutePath);
//
//                } else {
//                    addFileEntryZip(entryName);
//                    writeToEntryZip(absolutePath);
//                }
//            }
//        }
//    }
    /**
     * 压缩
     * @param sourceFilePath 输入文件
     * @param currentZipFilePath 输出文件
     */
    public void mainWorkFlow(String sourceFilePath, String currentZipFilePath) {
        //listMyDirectory(this.sourceFilePath);
        this.currentZipFilePath = currentZipFilePath;
        this.sourceFilePath = sourceFilePath;
        try {
            zos = new ZipOutputStream(new FileOutputStream(currentZipFilePath));
            //设定文件压缩级别
            zos.setLevel(9);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addFileEntryZip(this.sourceFilePath);
        writeToEntryZip(this.sourceFilePath);
        if (zos != null) {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压GZ文件
     * @param inputFile
     * @param outFile
     * @throws Exception
     */
    public static void decompressionGZ(File inputFile, File outFile) throws Exception {
        GZIPInputStream gzi = new GZIPInputStream(
                new FileInputStream(inputFile));
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(outFile));

        int b;
        do {
            b = gzi.read();
            if (b == -1) {
                break;
            }
            bos.write(b);
        } while (true);
        gzi.close();
        bos.close();
    }

    public static void main(String[] args) {
        new Zip().mainWorkFlow("c:\\hadoop.log", "c:\\a.zip");
    }
}
