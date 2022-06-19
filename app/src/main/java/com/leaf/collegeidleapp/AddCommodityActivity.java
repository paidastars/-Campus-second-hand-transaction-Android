package com.leaf.collegeidleapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 物品发布界面Activity类
 */
public class AddCommodityActivity extends AppCompatActivity {

    TextView tvStuId;
    ImageButton ivPhoto;
    EditText etTitle,etPrice,etPhone,etDescription;
    Spinner spType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commodity);
        //取出学号
        tvStuId = findViewById(R.id.tv_student_id);
        tvStuId.setText(this.getIntent().getStringExtra("user_id"));
        //返回按钮点击事件
        Button btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选取照片
        ivPhoto = findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //验证是否许可权限
                    for (String str : permissions) {
                        if (AddCommodityActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            AddCommodityActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                            return;
                        } else {
                            // 在新的Intent里面打开，并且传递CHOOSE_PHOTO选项
                            selectPicture();
                        }
                    }
                }
            }
        });
        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etPhone = findViewById(R.id.et_phone);
        etDescription = findViewById(R.id.et_description);
        spType = findViewById(R.id.spn_type);
        String result = "{\"data\": \"0\"}";
        Button btnPublish = findViewById(R.id.btn_publish);
        //发布按钮点击事件
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查合法性
                if(CheckInput()) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String url="http://100.2.145.64:8001/uploadAudio";
                                try {
                                    uploadImage(url,ImagePath);
                                } catch (IOException e) {
                                    Looper.prepare();
                                    e.printStackTrace();
                                    Looper.loop();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //创建信息对象
                                Message message = Message.obtain();
                                Bundle bundle = new Bundle();
                                bundle.putString("data",result);
                                message.setData(bundle);//向主线程发信息
                                addTrackHandler.sendMessage(message);

                                RequestBody formBody = new FormBody.Builder()
                                        .add("title", etTitle.getText().toString())
                                        .add("category", spType.getSelectedItem().toString())
                                        .add("price", etPrice.getText().toString())
                                        .add("phone", etPhone.getText().toString())
                                        .add("description", etDescription.getText().toString())
                                        .add("username", tvStuId.getText().toString())
                                        .build();
                                OkHttpClient client = new OkHttpClient();

                                //发送账号信息
                                Request request = new Request.Builder()
                                        .url("http://100.2.145.64:8001/upload")
                                        .post(formBody)
                                        .build();
                                Response response = null;
                                response = client.newCall(request).execute();
                                String s = response.body().string();
//                            int res = Integer.valueOf(s);
                                System.out.println(s);
                                Log.d(s, "获取返回值");
                                if (s.equals("1")) {

                                    Intent intent = new Intent();
                                    intent.setClass(AddCommodityActivity.this, MainActivity.class);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "商品信息发布成功!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    if (s.equals("2")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "商品信息发布失败!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

    }

    /**
     * 上传图片
     * @param url
     * @param imagePath 图片路径
     * @return 新图片的路径
     */
    // 请在点击事件中调用此函数
    int REQUEST_CODE_SELECT_PIC = 120;
    private String ImagePath;

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 这个函数会自动调用下面的函数
        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_PIC) {

            // 获取选择的图片
            Uri selectedImage = data.getData();
            int width = 0;
            int height = 0;

            String[] filePathColumn = new String[]{ MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, null,
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            // 获取到图片的路径
            String selectedImagePath = cursor.getString(columnIndex);
            System.out.println(selectedImagePath);
            // 调取底层处理
            //DealImage(selectedImagePath);
            // 解码该路径的图片，得到bitmap 图片

            Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

            width = bm.getWidth();
            height = bm.getHeight();
            {
                // 字符串的操作
                int star = selectedImagePath.toString().lastIndexOf("/");
                int end = selectedImagePath.toString().lastIndexOf(".");
                // 文件路径 ; 文件全路径+名字
                ImagePath = selectedImagePath;

            }
            if (selectedImage != null) {
                //将图片显示到ImageView中
               ivPhoto.setImageBitmap(bm);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String uploadImage(String url, String imagePath) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        return jsonObject.optString("image");
    }

    Handler addTrackHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String result = "";
            try {
                result = message.getData().getString("data");
                Toast.makeText(AddCommodityActivity.this, "发布成功", Toast.LENGTH_SHORT).show();

            }catch (Exception e){

            }
            //Toast.makeText(MainActivity.this, "调用成功"+result, Toast.LENGTH_SHORT).show();//测试弹框
            return true;
        }
    });
    


    /**
     * 检查输入是否合法
     */
    public boolean CheckInput() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String type = spType.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String description = etDescription.getText().toString();
        if (title.trim().equals("")) {
            Toast.makeText(this,"商品标题不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.trim().equals("")) {
            Toast.makeText(this,"商品价格不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.trim().equals("请选择类别")) {
            Toast.makeText(this,"商品类别未选择!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.trim().equals("")) {
            Toast.makeText(this,"手机号码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.trim().equals("")) {
            Toast.makeText(this,"商品描述不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
