package com.tools.other;

import java.io.*;

/**
 * 运行系统命令
 * @author ljg
 */
public class RunCmd {

    /**
     * 运行命令，返回exitValue
     * @param cmd
     * @return int
     */
    public static int run(String cmd) throws Exception {
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();
        br.close();
        int i = p.exitValue();
        p.destroy();
        return i;
    }

    /**
     * 运行命令，返回exitValue
     * @param args
     * @return
     * @throws Exception 
     */
    public static int run(String[] args) throws Exception {
        //Process p = Runtime.getRuntime().exec(args);
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //p.getInputStream().close();
        //p.getErrorStream().close();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            //System.out.println(line);
        }
        p.waitFor();
        br.close();
        int i = p.exitValue();
        p.destroy();
        return i;
    }
}
