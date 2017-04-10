package com.chai.findshard.observer;

import com.chai.findshard.impl.observer.ZookeeperObserver;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public class ZkObserverTest {

    public static void main(String[] args) {
        ZookeeperObserver zookeeperObserver = new ZookeeperObserver();
        zookeeperObserver.setZkAddr("127.0.0.1:2181");
        zookeeperObserver.setZkLockPath("/findshard/test/lock");
        zookeeperObserver.setZkTimeout(5000);
        zookeeperObserver.setShards("a,b,c,d,e,f,g,h,i,j,k");
        zookeeperObserver.setZkInstancePath("/findshard/test/shardInstance");
        zookeeperObserver.start();

        System.out.println("启动完成");
        for (;;);
    }

}
