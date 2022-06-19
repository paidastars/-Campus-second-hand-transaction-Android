package com.leaf.collegeidleapp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leaf.collegeidleapp.bean.User;

import java.util.ArrayList;
import java.util.LinkedList;


public class StudentTrans {
    private String argss;
    public  StudentTrans() {

    }
    public StudentTrans(String argss) {
        this.argss=argss;
    }

    public LinkedList<User> StudentTrans(String args) {
        LinkedList<User> list = new LinkedList<>();

        JSONArray objects = JSON.parseArray(args);
        for(int i=0;i<objects.size();i++){
            //通过数组下标取到object，使用强转转为JSONObject，之后进行操作
            JSONObject object = (JSONObject) objects.get(i);
            User user=new User();

            Integer username=object.getInteger("username");
            String password = object.getString("password");
            String name = object.getString("name");
            String major= object.getString("major");
            String phone= object.getString("phone");
            String qq= object.getString("qq");
            String address= object.getString("address");

            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setMajor(major);
            user.setPhone(phone);
            user.setQq(qq);
            user.setAddress(address);

            list.add(user);
        }
        return list;
    }
}
