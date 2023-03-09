package com.eyas.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

@Configuration
public class UseTask {

    //带返回值的线程
    @Async
    public Future<Integer> showSum(int i, int j)  {
        int x=i+j;
        System.out.println("当前执行的是第"+i+"线程");
        return new AsyncResult<Integer>(x);
    }


    @Async
    public void aa(int i, Long delay){
//        try {
//            Thread.currentThread().sleep(delay);
            System.out.println("i=" + i);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Async
    public void bb(int j){
        System.out.println("j=" + j);
    }

}
