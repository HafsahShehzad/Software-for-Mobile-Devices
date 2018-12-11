package com.example.apple.tabssample;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.tabssample.Classes.FollowingUsers;

import java.util.ArrayList;
import java.util.List;

public class FollowingUsersAdapter extends ArrayAdapter<FollowingUsers> {

    private Context mContext;
    private List<FollowingUsers> UserList = new ArrayList<>();

    public FollowingUsersAdapter(@NonNull Context context, ArrayList<FollowingUsers> list) {
        super(context, 0 , list);
        mContext = context;
        UserList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item_followers_and_following,parent,false);

        FollowingUsers currentUser = UserList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_user);
        image.setImageResource(currentUser.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textView_username);
        name.setText(currentUser.getmName());

        TextView followingORfollower = (TextView) listItem.findViewById(R.id.FollowingOrFollower);
        followingORfollower.setText(currentUser.getFollowingOrFollwer());

        return listItem;
    }


}
