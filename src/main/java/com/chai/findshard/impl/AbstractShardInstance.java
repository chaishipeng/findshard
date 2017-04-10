package com.chai.findshard.impl;

import com.chai.findshard.api.ShardCallback;
import com.chai.findshard.api.ShardInstance;

import java.util.List;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public abstract class AbstractShardInstance implements ShardInstance {

    protected volatile String[] shards;

    protected ShardCallback callback;

    public void registerShardCallback(ShardCallback callback) {
        this.callback = callback;
    }
}
