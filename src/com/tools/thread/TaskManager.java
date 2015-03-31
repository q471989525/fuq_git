package com.tools.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

/**
 * 任务管理调度 解决多任务运行资源争用问题以及任务安排问题
 * @author ljg
 */
public class TaskManager {

    private ReadWriteLock lock;
    private Executor executor;
    private final Map<Task, Integer> TASKS = new HashMap();
    public final static int TASK_STATUS_NOTEXISTS = -1;
    public final static int TASK_STATUS_QUEUE = 0;
    public final static int TASK_STATUS_RUNNING = 1;
    public final static int TASK_STATUS_COMPLETEANDQUEUE = 2;

    /**
     * 
     * @param lock 执行任务的锁 通过该类执行的任务都将通过该锁来进行同步控制
     * @param executor 执行任务的类 详细参考java.util.concurrent.Executor说明 实际的类根据执行任务方式的不同而不同
     * @see java.util.concurrent.Executor
     */
    public TaskManager(ReadWriteLock lock, Executor executor) {
        this.lock = lock;
        this.executor = executor;
    }

    /**
     * 运行任务一次
     * @param task 任务 
     */
    public void runTask(final Task task) {
        addTask(task);
        executor.execute(new Runnable() {

            public void run() {
                changeTaskStatus(task, TASK_STATUS_RUNNING);
                task.run(lock);
                removeTask(task);
            }
        });
    }

    /**
     * 当已有任务中不存在该任务时运行任务
     * @param task 
     */
    public synchronized void runTaskIfNotExist(final Task task) {
        int taskStatus = getTaskStatus(task);
        if (taskStatus == TASK_STATUS_NOTEXISTS) {
            runTask(task);
        }
    }

    /**
     * 在指定延迟后运行任务一次 构造函数中的Executor必须是ScheduledExecutorService的实现类
     * @param task
     * @param delay 
     */
    public void runTask(final Task task, long delay) {
        ScheduledExecutorService es = (ScheduledExecutorService) executor;
        addTask(task);
        es.schedule(new Runnable() {

            public void run() {
                changeTaskStatus(task, TASK_STATUS_RUNNING);
                task.run(lock);
                removeTask(task);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 在指定延迟后以指定周期运行任务 构造函数中的Executor必须是ScheduledExecutorService的实现类
     * @param task 任务
     * @param delay 初次执行延迟
     * @param period 连续执行之间的周期
     */
    public void runTaskAtFixedRate(final Task task, long delay, long period) {
        ScheduledExecutorService es = (ScheduledExecutorService) executor;
        addTask(task);
        es.scheduleAtFixedRate(new Runnable() {

            public void run() {
                changeTaskStatus(task, TASK_STATUS_RUNNING);
                task.run(lock);
                changeTaskStatus(task, TASK_STATUS_COMPLETEANDQUEUE);
            }
        }, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 在指定延迟后以指定间隔运行任务 构造函数中的Executor必须是ScheduledExecutorService的实现类
     * @param task 任务
     * @param delay 初次执行延迟
     * @param period 一次执行终止和下一次执行开始之间的延迟
     */
    public void runTaskWithFixedDelay(final Task task, long delay, long period) {
        ScheduledExecutorService es = (ScheduledExecutorService) executor;
        addTask(task);
        es.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                changeTaskStatus(task, TASK_STATUS_RUNNING);
                task.run(lock);
                changeTaskStatus(task, TASK_STATUS_COMPLETEANDQUEUE);
            }
        }, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 终止任务 只有构造函数中Executor为ExecutorService实现类的时候才可终止
     * @param b 是否强制终止已经运行的任务
     */
    public void cancel(boolean b) {
        if (executor instanceof ExecutorService) {
            if (b) {
                ((ExecutorService) executor).shutdownNow();
                TASKS.clear();
            } else {
                ((ExecutorService) executor).shutdown();
            }
        }
    }

    private synchronized void addTask(Task task) {
        if (TASKS.containsKey(task)) {
            throw new RuntimeException("task already exits");
        }
        TASKS.put(task, TASK_STATUS_QUEUE);
    }

    public int getTaskStatus(Task task) {
        Integer status = TASKS.get(task);
        return status == null ? TASK_STATUS_NOTEXISTS : status;
    }

    private synchronized void changeTaskStatus(Task task, int status) {
        if (TASKS.containsKey(task)) {
            TASKS.put(task, status);
        } else {
            throw new RuntimeException("task not exists!");
        }
    }

    private void removeTask(Task task) {
        TASKS.remove(task);
    }

    /**
     * 创建以线程池方式执行任务的TaskManager
     * @param threads 线程个数
     * @return 
     */
    public static TaskManager createTaskManagerWithThreadPool(int threads) {
        return new TaskManager(new ReentrantReadWriteLock(true), Executors.newScheduledThreadPool(threads));
    }

    /**
     * 创建以单个线程方式执行任务的TaskManager
     * @return 
     */
    public static TaskManager createTaskManagerWithSingleThread() {
        return new TaskManager(new ReentrantReadWriteLock(true), Executors.newSingleThreadScheduledExecutor());
    }
}
