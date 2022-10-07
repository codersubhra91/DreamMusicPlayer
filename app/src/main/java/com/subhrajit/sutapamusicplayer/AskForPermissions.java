package com.subhrajit.sutapamusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.system.Os;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class AskForPermissions extends AppCompatActivity {
    ImageButton btnAskForAccess;
    TextView androidVersion, showText;
    Context context;
    final String OS_RELEASE = Build.VERSION.RELEASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_permissions);

        context = getApplicationContext();
        btnAskForAccess = findViewById(R.id.btnAskPermission);
        androidVersion = findViewById(R.id.txtAndroidVersion);
        showText = findViewById(R.id.txtMessage);
        androidVersion.setText("OS: Android "+ OS_RELEASE);

        if (Integer.parseInt(OS_RELEASE) >= 11){
            showText.setText("Your OS version is Android "+OS_RELEASE+" which requires all file access into external to get all media files. Please grant access by clicking below icon");
            showText.setSelected(true);
        }else{
             showText.setText("Your OS version is Android "+OS_RELEASE+" Please grant access by clicking below Icon");
             showText.setSelected(true);
        }
        showText.setSelected(true);

        if (!isPermissionGranted(this)){
            btnAskForAccess.setOnClickListener(v->{
                askForPermission();
            });
        }else{
            Intent intent = new Intent (this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void askForPermission() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try{
                Intent intent = new Intent (Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivityForResult(intent,101);

            }catch (Exception ex){
                ex.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,101);
            }

        }else{
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 101);
        }

    }

    public boolean isPermissionGranted (Context ccontext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else{
            int readExternalStorage = ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    //After Granting Permission it will load Next Activity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        }

    }
}
