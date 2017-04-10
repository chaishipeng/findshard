package com.chai.findshard.api;

import java.util.List;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public interface ShardInstance {

    String[] getShards();

    void registerShardCallback(ShardCallback callback);

    void start();

}
