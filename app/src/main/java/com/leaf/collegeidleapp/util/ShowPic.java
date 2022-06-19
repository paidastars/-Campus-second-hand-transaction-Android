package com.leaf.collegeidleapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowPic {
    private String path;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }


    public ShowPic(String argss) {
        this.path=path;
    }

    public void showPic(String path)
    {
        new Thread(){
            private HttpURLConnection conn;
            public void run() {
                // 连接服务器 get 请求 获取图片
                try {
                    //创建URL对象
                    URL url = new URL(path);
                    // 根据url 发送 http的请求
                    conn = (HttpURLConnection) url.openConnection();
                    // 设置请求的方式
                    conn.setRequestMethod("GET");
                    //设置超时时间
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //关闭连接
                conn.disconnect();
            }
        }.start();
    }
}
