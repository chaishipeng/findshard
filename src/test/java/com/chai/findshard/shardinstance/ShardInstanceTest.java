package com.chai.findshard.shardinstance;

import com.chai.findshard.api.ShardCallback;
import com.chai.findshard.impl.ZookeeperShardInstance;

/**
 * Created by chaishipeng on 2017/4/10.
 */
public class ShardInstanceTest {

    public static void main(String[] args) {

        ZookeeperShardInstance zookeeperShardInstance = new ZookeeperShardInstance();
        zookeeperShardInstance.setZkAddr("127.0.0.1:2181");
        zookeeperShardInstance.setZkTimeout(5000);
        zookeeperShardInstance.setZkInstancePath("/findshard/test/shardInstance");
        zookeeperShardInstance.setShardCallback(new ShardCallback() {
            public void change(String[] data) {
                for (String str : data) {
                    System.out.print(str + " ");
                }
                System.out.println();
            }
        });
        zookeeperShardInstance.start();

        for (;;);


    }

}
