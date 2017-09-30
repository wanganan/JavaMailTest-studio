package com.laundrylangshop.javamailtest_studio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.laundrylangshop.javamailtest_studio.mail.Mail;
import com.laundrylangshop.javamailtest_studio.mail.QQMail;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否有这个权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //2、申请权限: 参数二：权限的数组；参数三：请求码
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 250);
            } else {
                exportMail();
            }
        } else {
            exportMail();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 250) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               exportMail();
            } else {
                Toast.makeText(MainActivity.this,"请打开存储权限",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 导出邮件
     */
    private void exportMail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Mail mail = new Mail();
                    mail.setSubject("就问你我帅不帅");
                    mail.setContent("你大爷的，帅的我眼睛都瞎了");

                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/附件/洗衣郎-国庆、中秋放假通知2017.pdf");
                        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/附件/FairyTail.jpg");
                        ArrayList<File> files = new ArrayList<>();
                        files.add(file1);
                        files.add(file2);
                        mail.setAttachmentsFiles(files);
                    }
                    QQMail.send(mail);
                } catch (Exception e) {
                    Log.e("MainActivity",e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
