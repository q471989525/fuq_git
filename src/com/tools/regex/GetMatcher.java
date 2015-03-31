package com.tools.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * 利用多线程获取正则表达式的Matcher，防止匹配时间过长 卡死
 * @author Administrator
 */
public class GetMatcher {

    private static Logger logger = Logger.getLogger(GetMatcher.class);
    private static final String a = "";


    /**
     * 匹配正则表达式
     * @param seedInfoFilter 正则表达式
     * @param context 需要匹配的内容
     * @return 匹配之后的Matcher
     */
    public static Matcher getMatcher(final String seedInfoFilter, final String context) {
        final List<Matcher> list = new ArrayList<Matcher>(1);
        list.add(null);
        //该线程匹配正则表达式
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (a) {
                    Pattern p = Pattern.compile(seedInfoFilter,Pattern.CASE_INSENSITIVE);
                    Matcher m1 = p.matcher(context);
                    if (!m1.matches()) {
                        m1 = null;
                    }
                    list.set(0,m1);
                    a.notifyAll();
                }
            }
        });

        //该线程在等待指定时间之后，若匹配线程没完成，直接杀死
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 10);
                    t.stop();
                } catch (InterruptedException ex) {
                    logger.error("", ex);
                }
            }
        });

        t.setDaemon(true);
        t1.setDaemon(true);

        synchronized (a) {//主线程
            try {
                t.start();
                t1.start();
                a.wait(1000);
                t1.stop();
            } catch (InterruptedException ex) {
                logger.error("", ex);
            }
        }
        return list.get(0);
    }
}
