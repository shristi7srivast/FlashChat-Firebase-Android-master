package com.srishristi.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatList extends BaseAdapter {
    private Activity mActivity;
    private String mDisplayName;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mDataSnapshots;
    private ChildEventListener mChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mDataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    public ChatList(Activity activity,DatabaseReference ref,String name){
       mActivity=activity;
        mDatabaseReference=ref.child("message");
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDisplayName=name;
        mDataSnapshots=new ArrayList<>();


    }
    class ViewHolder{
        TextView author;
        TextView body;
        LinearLayout.LayoutParams mParams;
    }
    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot=mDataSnapshots.get(position);

        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.author=(TextView) convertView.findViewById(R.id.author);
            holder.body=(TextView) convertView.findViewById(R.id.message);
            holder.mParams=(LinearLayout.LayoutParams)holder.author.getLayoutParams();
            convertView.setTag(holder);
        }
        final InstantMessage message=getItem(position);
        final ViewHolder viewHolder= (ViewHolder) convertView.getTag();
        boolean IsMe=message.getAuthor().equals(mDisplayName);
        setChatRowDataApperence(IsMe, viewHolder);
        String author=message.getAuthor();
        viewHolder.author.setText(author);
        String msg=message.getMessage();
        viewHolder.body.setText(msg);

        return convertView;
    }
    public void setChatRowDataApperence(boolean IsItMe,ViewHolder holder){
    if(IsItMe){
        holder.mParams.gravity= Gravity.END;
        holder.author.setTextColor(Color.RED);
        holder.body.setBackgroundResource(R.drawable.bubble1);
    }
    else {
        holder.mParams.gravity=Gravity.START;
        holder.author.setTextColor(Color.BLUE);
        holder.body.setBackgroundResource(R.drawable.bubble2);
    }
    holder.author.setLayoutParams(holder.mParams);
    holder.body.setLayoutParams(holder.mParams);
    }
    public void cleanUp(){
        mDatabaseReference.removeEventListener(mChildEventListener);



    }
}
