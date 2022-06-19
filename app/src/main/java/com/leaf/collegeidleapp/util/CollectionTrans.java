package com.leaf.collegeidleapp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leaf.collegeidleapp.bean.Collection;
import com.leaf.collegeidleapp.bean.Review;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionTrans {
    private String argss;
    public  CollectionTrans() {

    }
    public CollectionTrans(String argss) {
        this.argss=argss;
    }


    public List<Collection> CollectionTrans(String args) {
        List<Collection> list =new ArrayList<>();
        String jsonArray=args;
        //需要使用的JSON的parseArray方法，将jsonArray解析为object类型的数组
        JSONArray objects = JSON.parseArray(jsonArray);
        for(int i=0;i<objects.size();i++){
            //通过数组下标取到object，使用强转转为JSONObject，之后进行操作
            JSONObject object = (JSONObject) objects.get(i);
            Collection collection =new Collection();

            Integer id = object.getInteger("id");
            Integer username = object.getInteger("username");
            String description=object.getString("description");
            String phone=object.getString("phone");
            String title=object.getString("title");
            String price=object.getString("price");
            byte[] picture= object.getBytes("picture");

            collection.setId(id);
            collection.setUsername(username);
            collection.setDescription(description);
            collection.setPhone(phone);
            collection.setTitle(title);
            collection.setPrice(price);
            collection.setPicture(picture);

            list.add(collection);
        }
        return list;
    }
}
