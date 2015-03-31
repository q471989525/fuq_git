package com.tools.hack;


/**
 * 接收管理员端消息，转发给客户端 接收客户端消息，转发给管理员
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 *
 * @author fuq 2013-3-16
 */
public class Service {
//http://blog.csdn.net/m13666368773/article/details/6980605

    StringBuilder sb = new StringBuilder();
    //创建线程池
    ExecutorService executeService = Executors.newFixedThreadPool(50);
    private Map<String, Socket> scMap = new HashMap<String, Socket>();

    public Map<String, Socket> getScMap() {
        return scMap;
    }

    public Service() {
    }

    public void init() {
        final Service s = new Service();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket server;
                try {
                    server = new ServerSocket(4719);
                    while (true) {
                        final Socket accept = server.accept();
                        String hostAddress = accept.getInetAddress().getHostAddress();
                        // scMap.put(hostAddress, accept);
                        executeService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    s.send(accept);
                                } catch (Exception ex) {
                                    System.err.println(ex);
                                }
                            }
                        });
                        executeService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    s.get(accept);
                                } catch (Exception ex) {
                                    System.err.println(ex);
                                }
                            }
                        });
                        System.out.println(hostAddress + "连接");
                    }
                } catch (IOException ex) {
                    System.err.print(ex);
                }
            }
        }).start();
    }

    /**
     * 向客户端发送命令
     *
     * @param accept
     * @throws Exception
     */
    public void send(Socket accept) throws Exception {
        PrintWriter os = new PrintWriter(accept.getOutputStream());

        while (true) {
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            String line;
            line = sin.readLine();
            os.println(line);
            
            os.flush();
        }
    }

    private void get(Socket accept) throws IOException {
//        BufferedReader is = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        while (true) {
            try {
                //String readLine = is.readLine();
                //if (readLine == null) {
                //  Thread.sleep(1000);
                //} else {
                InputStream in = accept.getInputStream();
                
                    int count = 0;
                        while (count == 0) {
                            
                            count = in.available();
                        }
                        byte[] b = new byte[count];
                        in.read(b);
                    System.out.println(printHexString(b));
                //}
            } catch (Exception ex) {
                System.err.print(ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final Service s = new Service();
        s.init();
        //连接后 map已经执行 连接后回调

    }
    
     //将指定byte数组以16进制
    public static String printHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        // int length = b.length;
        for (int i = 0; i < b.length; i++) {
            String stmp = Integer.toHexString(b[i] & 0xff);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase();
    }
}
