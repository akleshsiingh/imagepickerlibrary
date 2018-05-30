package com.androidfizz.imagepicker2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidfizz.imagepickerlibrary.IOnFilesSelected;
import com.androidfizz.imagepickerlibrary.ImageModel;
import com.androidfizz.imagepickerlibrary.Picker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void filePicker() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
                return;
            }
        }

        Picker
                .with(this)
                .maxImage(3)
                .fileSelected(new IOnFilesSelected() {

                    @Override
                    public void onSelected(List<ImageModel> result) {
                        for(ImageModel a:result) Log.e("IMAGE ",""+a);
                    }

                    @Override
                    public void onFail(String error) {
                        Toast.makeText(MainActivity.this, "Fail " + error, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    public void click(View view) {
        filePicker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            case 22:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    filePicker();

                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
