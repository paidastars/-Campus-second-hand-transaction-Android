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
 * 修改密码活动类
 */
public class ModifyPwdActivity extends AppCompatActivity {

    TextView tvStuNumber;
    EditText etOriginPwd,etNewPwd,etConfirmPwd;
    StudentTrans studentTrans =new StudentTrans();
    LinkedList<User> list=new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        //取消事件
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStuNumber = findViewById(R.id.tv_stu_number);
        tvStuNumber.setText(this.getIntent().getStringExtra("stu_number"));
        etOriginPwd = findViewById(R.id.et_original_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_new_pwd);
        Button btnModify = findViewById(R.id.btn_modify_pwd);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先保证输入合法
                if(CheckInput()) {
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
                                for(User user : list) {
                                        if(!etOriginPwd.getText().toString().equals(user.getPassword())) {
                                            //提示初始密码输入错误
                                            Toast.makeText(getApplicationContext(),"初始密码输入错误!",Toast.LENGTH_SHORT).show();
                                        }else {
                                            //执行修改密码操作
                                            String password = etNewPwd.getText().toString();
                                            try {
                                                RequestBody formBody2 = new FormBody.Builder()
                                                        .add("username", username)
                                                        .add("password",password)
                                                        .build();
                                                OkHttpClient client2 = new OkHttpClient();
                                                Request request2 = new Request.Builder()
                                                        .url("http://100.2.145.64:8001/updatepassword")
                                                        .post(formBody2)
                                                        .build();
                                                Response response2 = client2.newCall(request2).execute();
                                                String b = response2.body().string();
                                                if(b.equals(1)) {
                                                    runOnUiThread(new Runnable()
                                                    {
                                                        @Override
                                                        public void run()
                                                        {
                                                            Toast.makeText(getApplicationContext(), "修改密码成功",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else {
                                                    runOnUiThread(new Runnable()
                                                    {
                                                        @Override
                                                        public void run()
                                                        {
                                                            Toast.makeText(getApplicationContext(), "修改密码失败",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            finish();
                                        }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }

    //判断输入的合法性
    public boolean CheckInput() {
        String OriginalPwd = etOriginPwd.getText().toString();
        String NewPwd = etNewPwd.getText().toString();
        String NewConfirmPwd = etConfirmPwd.getText().toString();
        if(OriginalPwd.trim().equals("")) {
            Toast.makeText(this,"原始密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(NewPwd.trim().equals("")) {
            Toast.makeText(this,"新密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(NewConfirmPwd.trim().equals("")) {
            Toast.makeText(this,"确认新密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!NewPwd.trim().equals(NewConfirmPwd.trim())) {
            Toast.makeText(this,"两次密码输入不一致!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
