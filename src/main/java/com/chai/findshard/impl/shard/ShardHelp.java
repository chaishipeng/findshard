package com.chai.findshard.impl.shard;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by chaishipeng on 2017/4/10.
 */
public class ShardHelp {

    private LinkedList<String> sumShard = Lists.newLinkedList();

    private LinkedList<Item> bigThanMinList = Lists.newLinkedList();

    private double max;

    private double min;

    public ShardHelp(String[] sumShard){
        for (String str : sumShard) {
            this.sumShard.add(str);
        }
    }

    public List<Item> shard(Map<String, String[]> shardMap){
        max = Math.ceil((double)sumShard.size()/shardMap.size());
        min = Math.floor((double)sumShard.size()/shardMap.size());

        List<Item> itemList = Lists.newArrayList();
        for (String key : shardMap.keySet()) {
            String[] shards = shardMap.get(key);
            Item item = new Item();
            for (String shard : shards) {
                item.addInitItem(shard);
                sumShard.remove(shard);
            }
            item.key = key;
            itemList.add(item);
        }

        Collections.sort(itemList, new Comparator<Item>() {
            public int compare(Item o1, Item o2) {
                return o2.getSize() - o1.getSize();
            }
        });


        for (Item item : itemList){
            while(item.getSize() > max){
                String str = item.removeItem();
                sumShard.addLast(str);
                addBigThanMinList(item);
            }
            while(item.getSize() < min){
                String sss;
                if (sumShard.size() <= 0) {
                    sss = getItemInBigThanMinList();
                } else {
                    sss = sumShard.removeLast();
                }
                if (sss != null) {
                    item.addItem(sss);
                }
            }
        }

        bigThanMinList.clear();
        while (sumShard.size() > 0){
            for (Item item : itemList){
                if (item.getSize() < max){
                    String sss = sumShard.removeLast();
                    item.addItem(sss);
                    if (sumShard.size() <= 0) {
                        break;
                    }
                }
            }

        }

        return itemList;
    }

    private void addBigThanMinList(Item item){
        if (item.getSize() > min && !bigThanMinList.contains(item)){
            bigThanMinList.addLast(item);
        }
    }

    private String getItemInBigThanMinList(){
        if (bigThanMinList.size() <= 0) {
            return null;
        }
        Item item = bigThanMinList.getFirst();
        String str = item.removeItem();
        if (item.getSize() <= min) {
            bigThanMinList.removeFirst();
        }
        return str;
    }

    public class Item {
        private boolean isChange;
        private String key;
        private int size;
        private LinkedList<String> stringItems = Lists.newLinkedList();

        public void addInitItem(String str){
            stringItems.addLast(str);
        }

        public void addItem(String str){
            stringItems.addLast(str);
            isChange = true;
        }

        public String removeItem() {
            if (stringItems.size() <= 0) {
                return null;
            }
            String str = stringItems.getLast();
            stringItems.removeLast();
            isChange = true;
            return str;
        }

        public int getSize() {
            return stringItems.size();
        }

        public boolean isChange() {
            return isChange;
        }

        public String getKey() {
            return key;
        }

        public String getStringItems(String split) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0 ; i < stringItems.size() ; i++) {
                if (i > 0) {
                    stringBuilder.append(split);
                }
                stringBuilder.append(stringItems.get(i));
            }
            return stringBuilder.toString();
        }

        @Override
        public String toString() {
            return "Item{" +
                    "stringItems=" + stringItems +
                    ", key='" + key + '\'' +
                    ", isChange=" + isChange +
                    '}';
        }
    }

}
