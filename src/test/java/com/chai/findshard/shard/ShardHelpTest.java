package com.chai.findshard.shard;

import com.chai.findshard.impl.shard.ShardHelp;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by chaishipeng on 2017/4/10.
 */
public class ShardHelpTest {

    public static void main(String[] args) {
        ShardHelp shardHelp = new ShardHelp(new String[]{"0","1","2","3","4","5","6"});
        Map<String, String[]> shardMaps = Maps.newHashMap();
        shardMaps.put("test", new String[]{"0","1"});
        shardMaps.put("test2", new String[]{"4","5","6"});
        shardMaps.put("test3", new String[]{"3", "2"});
        shardMaps.put("test4", new String[]{});
        List<ShardHelp.Item> items = shardHelp.shard(shardMaps);
        System.out.println(items);
    }

}
