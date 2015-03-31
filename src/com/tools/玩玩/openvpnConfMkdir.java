/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tools.玩玩;

import java.io.File;

/**
 * 创建openvpn目录文件
 * @author fuq
 */
public class openvpnConfMkdir {
    public static void main(String[] args) {
        File file=new File("/Users/fuq/vpn");
        File[] listFiles = file.listFiles();
        for (File listFile : listFiles) {
            String name = listFile.getName();
            System.out.println(name);
            File nfile=new File(listFile.getParent()+File.separator+name+".tblk");
            nfile.mkdir();
            listFile.renameTo(new File(nfile+File.separator+name));
        }
    }
   
}
