package com.loosubindhiya.naughtybindhya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class splashactivity extends Activity {


    private ImageView sadImageView,dineshImageView,bindhiyaImageView;
    private TextView sadTextView, naugthTextView,taptoLogin;
    private Animation moveupAnimation, movedownAnimation, moveRightAnimation, moveLeftAnimation, moveLeftLeave, moveRightLeave;
    private Boolean netConnection = Boolean.FALSE;
    private Button loginButton;
    private String userName=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        taptoLogin=(TextView)findViewById(R.id.taptoLogin);
        loginButton =(Button)findViewById(R.id.enterButton);
        sadImageView =(ImageView)findViewById(R.id.sadImageID);
        sadTextView = (TextView) findViewById(R.id.sadText);
        naugthTextView = (TextView)findViewById(R.id.naughtyTextView);
        dineshImageView=(ImageView)findViewById(R.id.dinesmImageID);
        bindhiyaImageView=(ImageView)findViewById(R.id.bindhiyaImageID);
        moveupAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveup);
        movedownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movedown);
        moveRightAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveright);
        moveLeftAnimation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveleft);
        moveLeftLeave= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveleft_leave);
        moveRightLeave=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveright_leave);

        dineshImageView.setVisibility(View.INVISIBLE);
        bindhiyaImageView.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        taptoLogin.setVisibility(View.INVISIBLE);


        SharedPreferences settings = getSharedPreferences("USERDETAILS",Context.MODE_PRIVATE);
        if(settings.getString("USERNAME",null)!=null)
        {
            Log.e("Username exists :", settings.getString("USERNAME",null));
            Intent intent = new Intent(splashactivity.this, chatactivity.class);
            intent.putExtra("USERNAME",settings.getString("USERNAME",null));
            startActivity(intent);
        }
        else
        {
            Log.e("Username error :", "NULL");
        }




        dineshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.VISIBLE);
                userName= "Dinesh";
                loginButton.setText("Continue as Dinesh");
            }
        });

        bindhiyaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.VISIBLE);
                userName="Bindhiya";
                loginButton.setText("Continue as Bindhiya");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= settings.edit();

                editor.putString("USERNAME",userName);
                editor.commit();

                String text = settings.getString("USERNAME", null);
                Log.e("Username is SP", text+" ");

                Intent intent =new Intent(splashactivity.this, chatactivity.class);
                intent.putExtra("USERNAME",userName);
                startActivity(intent);
            }
        });


        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(networkToggle())
                        {
                            if(isConnectedToNetwork())
                            {
                                sadImageView.setVisibility(View.INVISIBLE);
                                sadTextView.setVisibility(View.INVISIBLE);
                                naugthTextView.startAnimation(moveupAnimation);

                                bindhiyaImageView.setVisibility(View.VISIBLE);
                                dineshImageView.setVisibility(View.VISIBLE);

                                dineshImageView.startAnimation(moveRightAnimation);
                                bindhiyaImageView.startAnimation(moveLeftAnimation);

                                taptoLogin.setVisibility(View.VISIBLE);

                            }
                            else
                            {

                                taptoLogin.setVisibility(View.INVISIBLE);
                                dineshImageView.startAnimation(moveLeftLeave);
                                bindhiyaImageView.startAnimation(moveRightLeave);
                                naugthTextView.startAnimation(movedownAnimation);
                                bindhiyaImageView.setVisibility(View.INVISIBLE);
                                dineshImageView.setVisibility(View.INVISIBLE);
                                loginButton.setVisibility(View.INVISIBLE);
                                sadImageView.setVisibility(View.VISIBLE);
                                sadTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

            }
        },0,500);

    }


    private boolean networkToggle()
    {
        if(netConnection == isConnectedToNetwork())
        {
            return false;
        }
        else
        {
            netConnection = isConnectedToNetwork();
            return true;
        }
    }


    private boolean isConnectedToNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected())
        {
            return true;
        }

        return false;
    }
}
