package com.loosubindhiya.naughtybindhya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class welcomeactivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeactivity);


        Handler handler =new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                SharedPreferences settings = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE);
                if(settings.getString("USERNAME",null)!=null)
                {
                    Log.e("Username exists :", settings.getString("USERNAME",null));
                    Intent intent = new Intent(welcomeactivity.this, chatactivity.class);
                    intent.putExtra("USERNAME",settings.getString("USERNAME",null));
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(welcomeactivity.this,splashactivity.class);
                    startActivity(intent);
                }

            }
        },2300);
    }
}
