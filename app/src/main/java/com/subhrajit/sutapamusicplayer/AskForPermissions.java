package com.subhrajit.sutapamusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class AskForPermissions extends AppCompatActivity {

    ImageButton btnAskForAccess;
    String[]permissions={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_permissions);

        context = getApplicationContext();
        btnAskForAccess = findViewById(R.id.btnAskPermission);

        if (!checkPermission()){
            btnAskForAccess.setOnClickListener(v->{
                //Toast.makeText(this,"Clicking",Toast.LENGTH_LONG).show();
                if (!hasPermissions(AskForPermissions.this,permissions)){
                    ActivityCompat.requestPermissions(this,permissions,100);
                }
        });
        }else{
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkPermission(){
        int resultStorage= ContextCompat.checkSelfPermission(AskForPermissions.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultAudioRecord=ContextCompat.checkSelfPermission(AskForPermissions.this,Manifest.permission.RECORD_AUDIO);
        int resultAudioModify=ContextCompat.checkSelfPermission(AskForPermissions.this,Manifest.permission.MODIFY_AUDIO_SETTINGS);
        if (resultStorage== PackageManager.PERMISSION_GRANTED && resultAudioRecord==PackageManager.PERMISSION_GRANTED && resultAudioModify==PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }
    private boolean hasPermissions(Context context,String ...permissions){
        for (String permission:permissions){
            if (context != null && permissions != null){
                if (ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) != PackageManager.PERMISSION_DENIED){
                    return false;
                }
            }
        }
        return true;
    }

    //After Granting Permission it will load Next Activity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
        }

    }
}