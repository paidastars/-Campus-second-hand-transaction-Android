package com.leaf.collegeidleapp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leaf.collegeidleapp.bean.Review;
import java.util.LinkedList;


public class ReviewTrans {
    private String argss;
    public  ReviewTrans() {

    }
    public ReviewTrans(String argss) {
        this.argss=argss;
    }


    public LinkedList<Review> ReviewTrans(String args) {

        LinkedList<Review> list =new LinkedList<>();

        String jsonArray=args;
        //需要使用的JSON的parseArray方法，将jsonArray解析为object类型的数组
        JSONArray objects = JSON.parseArray(jsonArray);
        for(int i=0;i<objects.size();i++){
            //通过数组下标取到object，使用强转转为JSONObject，之后进行操作
            JSONObject object = (JSONObject) objects.get(i);
            Review review=new Review();

            String username = object.getString("username");
            String currentTime=object.getString("currentTime");
            String content=object.getString("content");
            Integer position=object.getInteger("position");

            review.setUsername(username);
            review.setCurrentTime(currentTime);
            review.setContent(content);
            review.setPosition(position);

            list.add(review);
        }
        return list;
    }
}
