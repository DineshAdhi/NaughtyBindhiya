package com.loosubindhiya.naughtybindhya;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dineshadhithya on 1/26/17.
 */

public class ChatAdapter extends BaseAdapter{

    ArrayList<Map<String, Object>> chatList;
    Context context;
    LayoutInflater layoutInflater;
    String CurrentUser;
    boolean isConnectedtoNetwork;

    ChatAdapter(Context context, ArrayList<Map<String, Object>> chatList, String CurrentUser, boolean isConnectedtoNetwork)
    {
        this.chatList=chatList;
        this.context = context;
        this.CurrentUser= CurrentUser;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {

        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=new ViewHolder();


        if(chatList.get(position).get("Sender").toString().equals(CurrentUser))
        {
            convertView = layoutInflater.inflate(R.layout.messagelayout_outgoing,null);
            viewHolder.messageTextView=(AppCompatTextView)convertView.findViewById(R.id.messageTextView);
            viewHolder.messageTextView.setText(chatList.get(position).get("Message").toString());
            viewHolder.profileImageView=(ImageView)convertView.findViewById(R.id.profileImageView);
            viewHolder.tickImage=(ImageView)convertView.findViewById(R.id.tickImageView);

            if(chatList.get(position).get("Sender").equals("Bindhiya"))
            {
                viewHolder.profileImageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.bindhiyaprofileimage));
            }
            else
            {
                viewHolder.profileImageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.dineshprofileimage));
            }

            if(isConnectedtoNetwork)
            {
                viewHolder.tickImage.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            convertView = layoutInflater.inflate(R.layout.messagelayout_incoming,null);
            viewHolder.messageTextView=(AppCompatTextView)convertView.findViewById(R.id.messageTextView);
            viewHolder.profileImageView=(ImageView)convertView.findViewById(R.id.profileImageView);
            viewHolder.messageTextView.setText(chatList.get(position).get("Message").toString());

            if(chatList.get(position).get("Sender").equals("Bindhiya"))
            {
                viewHolder.profileImageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.bindhiyaprofileimage));
            }
            else
            {
                viewHolder.profileImageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.dineshprofileimage));
            }
        }

        return convertView;

    }





    private static class ViewHolder
    {
        AppCompatTextView userNameTextView, messageTextView;
        ImageView profileImageView,tickImage;
    }
}


