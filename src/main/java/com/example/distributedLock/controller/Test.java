/*
 * Copyright (c) 2014 laidian. All Rights Reserved.
 * @author LYH
 * @date  2019-12-26 16:11
 */
package com.example.distributedLock.controller;

import com.example.distributedLock.DistributedLockException;
import com.example.distributedLock.impl.DistributedRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author LYH
 * @date 2019/12/26 16:11
 */
@RestController
public class Test {

    @Autowired
    DistributedRedisLock distributedRedisLock;

    public static int money = 12;


    @RequestMapping("/test")
    public String test() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0;i < 10;i ++) {
            executorService.execute(() -> {
                try {
                    if(distributedRedisLock.tryLock("lyh", TimeUnit.SECONDS, 1, 5)){
                        if (money == 0) {
                            return;
                        }
                        money-=2;
                        distributedRedisLock.unlock("lyh");
                        System.out.println(money);
                    }
                } catch (DistributedLockException e) {
                    e.printStackTrace();
                }
            });
        }

        return "Success";
    }

}