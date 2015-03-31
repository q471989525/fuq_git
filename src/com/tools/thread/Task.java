package com.tools.thread;

/**
 * 任务
 * @author ljg
 */
public interface Task {

    /**
     * 任务执行的方法
     * @param lock 该任务中可用的锁
     */
    public void run(java.util.concurrent.locks.ReadWriteLock lock);
}
