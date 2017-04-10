package com.chai.findshard.impl;

import com.chai.findshard.impl.util.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Created by chaishipeng on 2017/4/10.
 */
public class ZookeeperShardInstance extends AbstractShardInstance {

    private CuratorFramework curatorFramework;

    private String zkAddr;

    private int zkTimeout;

    private String zkInstancePath;

    private String realPath;

    public void start() {
        initZkClient();
    }

    private void initZkClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAddr)
                .sessionTimeoutMs(zkTimeout)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();

        try {
            realPath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(zkInstancePath + "/instance", "".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        registerNodeCache();
    }

    private void registerNodeCache(){
        final NodeCache nodeCache = new NodeCache(curatorFramework, realPath, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                String data = new String(nodeCache.getCurrentData().getData());
                String[] datas = StringUtils.splitStr(data, ",");
                shards = datas;
            }
        });
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    public void setZkTimeout(int zkTimeout) {
        this.zkTimeout = zkTimeout;
    }

    public void setZkInstancePath(String zkInstancePath) {
        this.zkInstancePath = zkInstancePath;
    }
}
