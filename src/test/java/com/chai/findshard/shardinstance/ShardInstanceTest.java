package com.chai.findshard.shardinstance;

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
        zookeeperShardInstance.start();

        while(true){
            String[] shards = zookeeperShardInstance.getShards();
            if (shards != null) {
                for (String shard : shards) {
                    System.out.print(shard + " ");
                }
                System.out.println();
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
