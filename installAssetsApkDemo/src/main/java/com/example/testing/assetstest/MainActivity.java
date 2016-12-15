package com.example.testing.assetstest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {
    String fileName = "ts.apk";
    File sdCardPath = Environment.getExternalStorageDirectory();
    private boolean isWriteSDComplete;
    private Button mBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.button);
        if(isExistSDCard()){
            assetsToSd();
        }else{
            Toast.makeText(this,"不存在sd卡",Toast.LENGTH_SHORT).show();
        }
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWriteSDComplete){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(sdCardPath, fileName)),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"等待写入sd卡",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isExistSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else{
            return false;
        }
    }

    private void assetsToSd() {
        File testFile = new File(sdCardPath, fileName);
        try {
            InputStream in = getAssets().open(fileName);
            FileOutputStream  out = new FileOutputStream(testFile,true);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            isWriteSDComplete=true;
            mBtn.setText("写入成功，开始安装");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", "================================"+e.toString());
        }
    }
}