package com.frank.discover;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ZkDiscover {

    private ZkDiscover(){
    }

    public static ZkDiscover instance = new ZkDiscover();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static ZkDiscover getInstance(){
        return instance;
    }

    public void init(){
        // TODO 参数
        ZkDiscoverBinder zkDiscoverBinder = new ZkDiscoverBinder("", 30000);
        zkDiscoverBinder.connect("");
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                ServiceManager.getInstance().checkConnects();
            }
        }, 5000, 5000, TimeUnit.MILLISECONDS);
    }
}
