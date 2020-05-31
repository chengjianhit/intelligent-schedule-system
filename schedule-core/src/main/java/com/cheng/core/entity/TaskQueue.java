package com.cheng.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue<T> {

    /**
     * 通过 ReentrantLock实现生产者、消费者模式，也可以使用BlockingQueue (LinkedBlockingQueue)阻塞队列实现
     */

    private ReentrantLock lock = new ReentrantLock(true);

    /**
     * not full condition
     */
    private Condition notFull = lock.newCondition();

    /**
     * not empty condition
     */
    private Condition notEmpty = lock.newCondition();


    /**
     * 保存的数据
     */
    List<T> dataList = null;

    private int size;

    public TaskQueue(int size) {
        this.size = size;
        dataList = new ArrayList<>(size);
    }

    /**
     * Provider
     * @param t
     * @throws Exception
     */
    public void put(T t) throws Exception{
        checkNotNull(t);
        lock.lock();
        try {
            while (dataList.size() >= size){
                notFull.await();
            }
            dataList.add(t);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }


    /**
     * add to the queue
     *
     * @param t
     * @throws Exception
     */
    public void putFirst(T t) throws Exception {
        checkNotNull(t);
        lock.lock();
        try {
            while (dataList.size() >= size) {
                notFull.await();
            }
            dataList.add(0, t);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Consumer
     *
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T take() throws Exception {
        lock.lock();
        try {
            while (dataList.size() == 0) {
                notEmpty.await();
            }
            T remove = (T) dataList.remove(0);
            notFull.signal();
            return remove;
        } finally {
            lock.unlock();
        }
    }



    private void checkNotNull(T e) {
        if (e == null) {
            throw new RuntimeException();
        }
    }
}
