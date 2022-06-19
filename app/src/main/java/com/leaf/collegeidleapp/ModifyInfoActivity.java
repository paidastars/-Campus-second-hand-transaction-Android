package com.leaf.collegeidleapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leaf.collegeidleapp.bean.User;
import com.leaf.collegeidleapp.util.StudentTrans;

import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 修改个人信息Activity类
 * @author : autumn_leaf
 */
public class ModifyInfoActivity extends AppCompatActivity {

    EditText etStuName,etMajor,etPhone,etQq,etAddress;
    LinkedList<User> list =new LinkedList<>();
    StudentTrans studentTrans =new StudentTrans();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        Button btnBack = findViewById(R.id.btn_back);
        //返回按钮点击事件
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //利用bundle传递学号
        final TextView tvStuNumber = findViewById(R.id.tv_stu_number);
        tvStuNumber.setText(this.getIntent().getStringExtra("stu_number2"));
        etStuName = findViewById(R.id.et_stu_name);
        etMajor = findViewById(R.id.et_stu_major);
        etPhone = findViewById(R.id.et_stu_phone);
        etQq = findViewById(R.id.et_stu_qq);
        etAddress = findViewById(R.id.et_stu_address);

        String username = tvStuNumber.getText().toString();
        new Thread(){
            @Override
            public void run() {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("username", username)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://100.2.145.64:8001/myuser")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String s = response.body().string();
                    list=studentTrans.StudentTrans("["+s+"]");
                    System.out.println(list);
                    if(list != null) {
                        for(User user : list) {
                            etStuName.setText(user.getName());
                            etMajor.setText(user.getMajor());
                            etPhone.setText(user.getPhone());
                            etQq.setText(user.getQq());
                            etAddress.setText(user.getAddress());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Button btnSaveInfo = findViewById(R.id.btn_save_info);
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断输入不为空
                if(CheckInput()) {

                    String username = tvStuNumber.getText().toString();
                    String name = etStuName.getText().toString();
                    String major = etMajor.getText().toString();
                    String phone = etPhone.getText().toString();
                    String qq = etQq.getText().toString();
                    String address = etAddress.getText().toString();

                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                RequestBody formBody = new FormBody.Builder()
                                        .add("username", username)
                                        .add("name",name)
                                        .add("major",major)
                                        .add("phone",phone)
                                        .add("qq",qq)
                                        .add("address",address)
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://100.2.145.64:8001/updateuser")
                                        .post(formBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(getApplicationContext(),"用户信息保存成功!",Toast.LENGTH_SHORT).show();
                    //销毁当前界面
                    finish();
                }
            }
        });
    }

    //检查输入是否为空
    public boolean CheckInput() {
        String StuName = etStuName.getText().toString();
        String StuMajor = etMajor.getText().toString();
        String StuPhone = etPhone.getText().toString();
        String StuQq = etQq.getText().toString();
        String StuAddress = etAddress.getText().toString();
        if(StuName.trim().equals("")) {
            Toast.makeText(this,"姓名不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuMajor.trim().equals("")) {
            Toast.makeText(this,"专业不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuPhone.trim().equals("")) {
            Toast.makeText(this,"联系方式不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuQq.trim().equals("")) {
            Toast.makeText(this,"QQ号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuAddress.trim().equals("")) {
            Toast.makeText(this,"地址不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
