package org.pbccrc.api.core.dao.util;

import java.util.*;

import org.pbccrc.api.base.bean.SmsCondition;

public class SmsWorkQueue {  
    private final int nThreads;// 线程池的大小  
    private final PoolWorker[] threads;// 用数组实现线程池  
    private final LinkedList queue;// 任务队列  
  
    public SmsWorkQueue(int nThreads) {  
        this.nThreads = nThreads;  
        queue = new LinkedList();  
        threads = new PoolWorker[nThreads];  
  
        for (int i = 0; i < nThreads; i++) {  
            threads[i] = new PoolWorker();  
            threads[i].start();// 启动所有工作线程  
        }  
    }  
  
    public void execute(Runnable r) {// 执行任务  
        synchronized (queue) {  
            queue.addLast(r);  
            queue.notify();  
        }  
    }  
  
    private class PoolWorker extends Thread {// 工作线程类  
        public void run() {  
            Runnable r;  
            while (true) {  
                synchronized (queue) {  
                    while (queue.isEmpty()) {// 如果任务队列中没有任务，等待  
                        try {  
                            queue.wait();  
                        } catch (InterruptedException ignored) {  
                        }  
                    }  
                    r = (Runnable) queue.removeFirst();// 有任务时，取出任务  
                }  
                try {  
                    r.run();// 执行任务  
                } catch (RuntimeException e) {  
                    // You might want to log something here  
                }  
            }  
        }  
    }  
  
//    public static void main(String args[]) {  
//        SmsWorkQueue wq = new SmsWorkQueue(2);// 10个工作线程  
//        Mytask r[] = new Mytask[20];// 20个任务  
//  
//        for (int i = 0; i < 20; i++) {  
//            r[i] = new Mytask(i);  
//            wq.execute(r[i]);  
//        }  
//    }  
}  
  
class Mytask implements Runnable {
	SmsCondition condition = null;
	
	public Mytask(SmsCondition smsCondition) {
		this.condition = smsCondition;
	}

// 任务接口  
    public void run() {  
        String name = Thread.currentThread().getName();  
        try {
        	System.out.println(condition.getSeq()+"：我开始执行啦");
            Thread.sleep(10000);// 模拟任务执行的时间  
        } catch (InterruptedException e) {  
        }  
        System.out.println(name + " executed OK");  
    }  
}  