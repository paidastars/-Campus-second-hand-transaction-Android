package com.leaf.collegeidleapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册界面Activity类
 * @author : autumn_leaf
 */
public class RegisterActivity extends AppCompatActivity {

    EditText tvStuNumber,tvStuPwd,tvStuConfirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnCancel = findViewById(R.id.btn_cancel);
        //返回到登录界面
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStuNumber = findViewById(R.id.et_username);
        tvStuPwd = findViewById(R.id.et_password);
        tvStuConfirmPwd = findViewById(R.id.et_confirm_password);
        //注册点击事件
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先确保不为空
                if(CheckInput()) {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                RequestBody formBody = new FormBody.Builder()
                                        .add("username", tvStuNumber.getText().toString())
                                        .add("password", tvStuPwd.getText().toString())
                                        .build();
                                OkHttpClient client = new OkHttpClient();

                                //发送账号信息
                                Request request = new Request.Builder()
                                        .url("http://100.2.145.64:8001/register")
                                        .post(formBody)
                                        .build();
                                Response response = null;
                                response = client.newCall(request).execute();
                                String s = response.body().string();
//                            int res = Integer.valueOf(s);
                                System.out.println(s);
                                Log.d(s, "获取返回值");
                                //根据后端反应确定是否登陆成功
                                if (s.equals("1"))
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Toast.makeText(getApplicationContext(), "账号已存在",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    if (s.equals("2")){
                                        runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Toast.makeText(getApplicationContext(), "注册成功",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //销毁当前界面
                    finish();
                }
            }
        });
    }

    //判断输入是否符合规范
    public boolean CheckInput() {
        String username = tvStuNumber.getText().toString();
        String password = tvStuPwd.getText().toString();
        String confirm_password = tvStuConfirmPwd.getText().toString();
        if(username.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"用户名不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirm_password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"确认密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.trim().equals(confirm_password.trim())) {
            Toast.makeText(RegisterActivity.this,"两次密码输入不一致!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
