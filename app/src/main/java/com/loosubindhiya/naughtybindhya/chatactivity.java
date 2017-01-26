package com.loosubindhiya.naughtybindhya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatactivity extends AppCompatActivity {

    ListViewCompat chatListView;
    TextInputEditText chatBox;
    AppCompatImageButton sendButton;
    private ArrayList<Map<String,Object>> messageArrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logoutId:
                SharedPreferences settings = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= settings.edit();

                editor.clear();
                editor.commit();

                Intent intent=new Intent(chatactivity.this, splashactivity.class);
                startActivity(intent);
                break;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        
        final String username = getIntent().getStringExtra("USERNAME");


        chatListView = (ListViewCompat) findViewById(R.id.chatListView);
        chatBox=(TextInputEditText)findViewById(R.id.chatBoxText);
        sendButton=(AppCompatImageButton)findViewById(R.id.sendButton);
        messageArrayList=new ArrayList<Map<String,Object>>();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TextMessages");
        databaseReference.keepSynced(true);

        FirebaseAuth mAuth; mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e("Success","Logged in");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failed","Login failure");
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(!chatBox.getText().toString().equals(""))
                  {
                      Map<String, Object> chatMap=new HashMap<String, Object>();
                      chatMap.put("Message", chatBox.getText().toString());
                      chatMap.put("Sender", username);

                      databaseReference.push().setValue(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              chatBox.setText("");
                          }
                      });
                  }
            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<Map<String,Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String,Object> messageMap = dataSnapshot.getValue(genericTypeIndicator);
                Log.e("Data",messageMap.toString());
                messageArrayList.add(messageMap);

                ChatAdapter chatAdapter = new ChatAdapter(getApplicationContext(),messageArrayList,username,isConnectedToNetwork() );
                chatListView.setAdapter(chatAdapter);
                chatListView.setSelection(chatAdapter.getCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

