package com.chai.findshard.impl.observer;

import com.chai.findshard.impl.shard.ShardHelp;
import com.chai.findshard.impl.util.IntegerUtils;
import com.chai.findshard.impl.util.ListUtils;
import com.chai.findshard.impl.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public class ZookeeperObserver implements Observer {

    private CuratorFramework curatorFramework;

    private String zkAddr;

    private int zkTimeout;

    private String zkLockPath;

    private String zkInstancePath;

    private String shards;

    private String[] sumShard;

    private volatile boolean isMaster;

    public void start() {
        initZkClient();
        applyMaster();
    }

    private void initZkClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAddr)
                .sessionTimeoutMs(zkTimeout)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
    }

    public void applyMaster() {
        boolean isMaster = getLock();
        if (!isMaster) {
            registerLockListener();
        }
    }

    private boolean getLock() {
        try {
            String res = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(zkLockPath, "LOCK".getBytes());
            if (res !=null && res.trim().length() > 0) {
                isMaster = true;
                Thread obThread = new Thread(new Runnable() {
                    public void run() {
                        startObserver();
                    }
                });
                obThread.setName("FindShard-ObserverThread");
                obThread.setDaemon(true);
                obThread.start();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startObserver() {
        while(isMaster){
            checkShard();
            try {
                Thread.currentThread().sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkShard() {
        List<String> instancePaths = null;
        try {
            instancePaths = curatorFramework.getChildren().forPath(zkInstancePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ListUtils.isEmpty(instancePaths)) {
            return;
        }
        shard(instancePaths);
    }

    private void shard(List<String> instancePaths){
        Map<String, String[]> path2ShardsMap = Maps.newHashMap();
        for(String path0:instancePaths) {
            String path = zkInstancePath + "/" + path0;
            try {
                byte[] data = curatorFramework.getData().forPath(path);
                if (data == null) {
                    continue;
                }
                String numberStr = new String(data);
                String shards[] = StringUtils.splitStr(numberStr, ",");
                path2ShardsMap.put(path, shards);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<ShardHelp.Item> items = getShardHelp().shard(path2ShardsMap);
        for (ShardHelp.Item item : items) {
            if (!item.isChange()){
                continue;
            }
            try {
                curatorFramework.setData().forPath(item.getKey(), item.getStringItems(",").getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ShardHelp getShardHelp(){
        ShardHelp shardHelp = new ShardHelp(this.sumShard);
        return shardHelp;
    }

    private void registerLockListener() {
        TreeCache treeCache = new TreeCache(curatorFramework, zkLockPath);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED){
                    applyMaster();
                }
            }
        });
        try {
            treeCache.start();
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

    public void setZkLockPath(String zkLockPath) {
        this.zkLockPath = zkLockPath;
    }

    public void setZkInstancePath(String zkInstancePath) {
        this.zkInstancePath = zkInstancePath;
    }

    public void setShards(String shards) {
        sumShard = shards.split(",");
    }
}
