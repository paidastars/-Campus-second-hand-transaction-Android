package com.leaf.collegeidleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leaf.collegeidleapp.adapter.ReviewAdapter;
import com.leaf.collegeidleapp.bean.Review;
import com.leaf.collegeidleapp.util.ReviewTrans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 商品信息评论/留言类
 * @author autumn_leaf
 */
public class ReviewCommodityActivity extends AppCompatActivity {

    TextView title,description,price,phone;
    ImageView ivCommodity;
    ListView lvReview;
    EditText etComment;
    int position,id;
    byte[] picture;

    ReviewTrans readReviews=new ReviewTrans();
    LinkedList<Review> list =new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_commodity);
        ivCommodity = findViewById(R.id.iv_commodity);
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        price = findViewById(R.id.tv_price);
        phone = findViewById(R.id.tv_phone);

        //获取跳转前的信息
        Bundle b = getIntent().getExtras();
        if( b != null) {
            picture = b.getByteArray("picture");
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivCommodity.setImageBitmap(img);
            title.setText(b.getString("title"));
            description.setText(b.getString("description"));
            price.setText((b.getString("price"))+"元");
            phone.setText(b.getString("phone"));
            position = b.getInt("position");
            id=b.getInt("id");
        }

        //返回
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击收藏按钮
        ImageButton ibMyLove = findViewById(R.id.ib_my_love);
        ibMyLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            RequestBody formBody = new FormBody.Builder()
                                    .add("id", String.valueOf(id))
                                    .add("username",username)
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://100.2.145.64:8001/addcollection")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String s = response.body().string();
                            if (s.equals("1")) {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        etComment.setText("");
                                        Toast.makeText(getApplicationContext(),"已添加至我的收藏!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(),"添加失败!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        etComment = findViewById(R.id.et_comment);
        lvReview = findViewById(R.id.list_comment);

        String pos=String.valueOf(position);
        //提交评论点击事件
        Button btnReview = findViewById(R.id.btn_submit);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查是否为空
                if(CheckInput()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    //获取当前时间
                    Date date = new Date(System.currentTimeMillis());
                    String username = getIntent().getStringExtra("username");

                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                RequestBody formBody = new FormBody.Builder()
                                        .add("username", username)
                                        .add("currentTime",simpleDateFormat.format(date))
                                        .add("content",etComment.getText().toString())
                                        .add("id",String.valueOf(id))
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://100.2.145.64:8001/getReview")
                                        .post(formBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String s = response.body().string();
                                if (s.equals("1")) {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            etComment.setText("");
                                            Toast.makeText(getApplicationContext(),"评论成功!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Toast.makeText(getApplicationContext(),"评论失败!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });


        final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext());


        new Thread(){
            @Override
            public void run() {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("id",String.valueOf(id))
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://100.2.145.64:8001/reviewShow")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String s = response.body().string();

                    list=readReviews.ReviewTrans(s);
                    //添加位置
                    for(Review review:list){
                        review.setPosition(position);
                    }
                    adapter.setData(list);
                    lvReview.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //刷新页面
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            RequestBody formBody = new FormBody.Builder()
                                    .add("id",String.valueOf(id))
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://100.2.145.64:8001/reviewShow")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String s = response.body().string();

                            list=readReviews.ReviewTrans(s);
                            //添加位置
                            for(Review review:list){
                                review.setPosition(position);
                            }
                            adapter.setData(list);
                            lvReview.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    /**
     * 检查输入评论是否为空
     * @return true
     */
    public boolean CheckInput() {
        String comment = etComment.getText().toString();
        if (comment.trim().equals("")) {
            Toast.makeText(this,"评论内容不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
