
package com.tools.thread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 多线程管理，循环中的任务分配
 * copyright &copy;2011 www.pgia.net <br/>
 * copyright 磐基讯息 2012-2-15<br/>
 * @author fuq
 * @version 1.00
 */
public class ThreadTaskManager {

  public static void main(String[] args) {
        TaskManager ctmw = TaskManager.createTaskManagerWithThreadPool(10);//异步执行10个线程
        for (int i = 0; i < 100; i++) {//分配100个任务
            ctmw.runTask(new a(i));
        }
        ctmw.cancel(false);//终止
    }
}

class a implements Task {

    private int ii;

    a(int i) {
        ii = i;
    }

    @Override
    public void run(ReadWriteLock lock) {
        System.out.println(ii);
        try {
            Thread.sleep(10*ii);
        } catch (InterruptedException ex) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}