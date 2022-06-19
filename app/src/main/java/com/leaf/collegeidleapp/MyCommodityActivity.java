package com.leaf.collegeidleapp;


import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.leaf.collegeidleapp.adapter.MyCommodityAdapter;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.TransJson;


import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 我的发布物品Activity类
 */
public class MyCommodityActivity extends AppCompatActivity {

    ListView lvMyCommodity;
    TextView tvStuId;

    MyCommodityAdapter adapter;
    List<Commodity> commodities = new ArrayList<>();
    TransJson transJson=new TransJson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commodity);
        TextView tvBack = findViewById(R.id.tv_back);
        //点击返回销毁当前界面
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStuId = findViewById(R.id.tv_stu_id);
        tvStuId.setText(this.getIntent().getStringExtra("stu_id"));
        lvMyCommodity = findViewById(R.id.lv_my_commodity);

        adapter = new MyCommodityAdapter(getApplicationContext());

        new Thread(){
            @Override
            public void run() {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("username", tvStuId.getText().toString())
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://100.2.145.64:8001/mycommodity")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String s = response.body().string();

                    commodities=transJson.ttransJSON(s);

                    adapter.setData(commodities);

                    lvMyCommodity.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //长按点击事件
        lvMyCommodity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //注意,这里的content不能写getApplicationContent();
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCommodityActivity.this);

                builder.setTitle("提示:").setMessage("确认删除此商品项吗?").setIcon(R.drawable.icon_user).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Commodity commodity = (Commodity) adapter.getItem(position);

                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    RequestBody formBody = new FormBody.Builder()
                                            .add("id", commodity.getId().toString())
                                            .build();
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("http://100.2.145.64:8001/delcommodity")
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
                                                Toast.makeText(MyCommodityActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else {
                                        runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Toast.makeText(MyCommodityActivity.this,"删除失败!",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }).show();
                return false;
            }
        });
        //刷新界面点击事件
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setData(commodities);
                lvMyCommodity.setAdapter(adapter);
            }
        });
    }
}
