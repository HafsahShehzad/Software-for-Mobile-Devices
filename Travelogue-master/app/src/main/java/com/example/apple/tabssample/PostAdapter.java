package com.example.apple.tabssample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apple.tabssample.Activities.LikeAndComment;
import com.example.apple.tabssample.Classes.Post;
import com.example.apple.tabssample.Fragments.PostTabFragment1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private ArrayList<Post> dataSet;
    final private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV, titleTV, locationTV, descriptionTV;
        Button likeBtn, shareBtn, commentBtn;
        TextView likesCount, commentsCount, sharesCount;

        ImageView imageViewIcon, userPicIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.likesCount = (TextView) itemView.findViewById(R.id.likes_count);
            this.commentsCount = (TextView) itemView.findViewById(R.id.comments_count);
            this.sharesCount = (TextView) itemView.findViewById(R.id.shares_count);
            this.dateTV = (TextView) itemView.findViewById(R.id.single_post_date);
            this.titleTV = (TextView) itemView.findViewById(R.id.single_post_title);
            this.descriptionTV = (TextView) itemView.findViewById(R.id.single_post_description);
            this.locationTV = (TextView) itemView.findViewById(R.id.single_post_location);
            this.userPicIcon = (ImageView) itemView.findViewById(R.id.single_post_userpic);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.likeBtn = (Button) itemView.findViewById(R.id.single_post_like_btn);
            this.commentBtn = (Button) itemView.findViewById(R.id.single_post_comment_btn);
            this.shareBtn = (Button) itemView.findViewById(R.id.single_post_share_btn);
        }
    }

    public PostAdapter(ArrayList<Post> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post, parent, false);

//        view.setOnClickListener(PostTabFragment1.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        //Toast.makeText(context, "Clicked",
        //Toast.LENGTH_SHORT).show();
//                holder.interestedBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));


        TextView dateTV = holder.dateTV;
        TextView endDateTV;
        TextView titleTV = holder.titleTV;
        TextView descriptionTV = holder.descriptionTV;
        TextView locationTV = holder.locationTV;
        ImageView imageView = holder.imageViewIcon;
        ImageView userIcon = holder.userPicIcon;
        dateTV.setText(dataSet.get(listPosition).getDate());
        descriptionTV.setText(dataSet.get(listPosition).getDescription());
        holder.titleTV.setText(dataSet.get(listPosition).getTitle());
        holder.locationTV.setText(dataSet.get(listPosition).getLocation());


//        startDateTV.setText(dataSet.get(listPosition).getStartDate()+" - "+dataSet.get(listPosition).getEndDate());
//        titleTV.setText(dataSet.get(listPosition).getTitle());
//        locationTV.setText(dataSet.get(listPosition).getLocation());
        firebaseRef.getInstance().getMyRef().child("Posts").child(dataSet.get(listPosition).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("likes").exists()) {
                        holder.likesCount.setText(dataSnapshot.child("likes").getChildrenCount() + " Likes");
                    } else {
                        holder.likesCount.setText(0 + " Likes");
                    }
                    if (dataSnapshot.child("likes").exists()) {
                        holder.commentsCount.setText(dataSnapshot.child("comments").getChildrenCount() + " Comments");
                    } else {
                        holder.commentsCount.setText(0 + " Comments");
                    }
                    if (dataSnapshot.child("likes").exists()) {
                        holder.sharesCount.setText(dataSnapshot.child("shares").getChildrenCount() + " Shares");
                    } else {
                        holder.sharesCount.setText(0 + " Shares");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference userProfilePicRef = firebaseRef.getInstance().getMyRef().child("User").child(dataSet.get(listPosition).getPublisher()).child("profile_picture");
        userProfilePicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Glide.with(context)
                            .load(dataSnapshot.getValue().toString())
                            .into(holder.userPicIcon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Glide.with(context)
                .load(dataSet.get(listPosition).getPicture().get(0))
                .into(holder.imageViewIcon);

        final String currentUser = firebaseRef.getInstance().getCurrentUser();
        final String currentObjectId = dataSet.get(holder.getAdapterPosition()).getId();

        if(currentUser == null || currentObjectId == null || currentUser.isEmpty() || currentUser.isEmpty())
        {
            return;
        }

        firebaseRef.getInstance().getUsers().child(currentUser).child("posts").child("liked").child(currentObjectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.likeBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));
                    firebaseRef.getInstance().getUsers().child(currentUser).child("posts").child("liked").child(currentObjectId).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                holder.likeBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));

                firebaseRef.getInstance().getMyRef().child("Posts").child(currentObjectId).child("likes").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            holder.likeBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));
                            firebaseRef.getInstance().getMyRef().child("User").child(currentUser).child("posts").child("liked").child(currentObjectId).setValue(true);
                            firebaseRef.getInstance().getMyRef().child("Posts").child(currentObjectId).child("likes").child(currentUser).setValue(true);
                        } else {
                            holder.likeBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Black));
                            firebaseRef.getInstance().getMyRef().child("User").child(currentUser).child("posts").child("liked").child(currentObjectId).setValue(null);
                            firebaseRef.getInstance().getMyRef().child("Posts").child(currentObjectId).child("likes").child(currentUser).setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
//        holder.goingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Clicked",
//                        Toast.LENGTH_SHORT).show();
//                holder.goingBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));
//                final String currentUser= firebaseRef.getInstance().getCurrentUser();
//                final String currentObjectId=dataSet.get(holder.getAdapterPosition()).getId();
//                firebaseRef.getInstance().getEvents().child(currentObjectId).child("interested").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()) {
//                            holder.interestedBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Black));
//                            firebaseRef.getInstance().getEvents().child(currentObjectId).child("interested").child(currentUser).setValue(null);
//                            firebaseRef.getInstance().getUsers().child(currentUser).child("interested").child(currentObjectId).setValue(null);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//                firebaseRef.getInstance().getUsers().child(currentUser).child("going").child(currentObjectId).setValue(true);
//                firebaseRef.getInstance().getEvents().child(currentObjectId).child("going").child(currentUser).setValue(true);
//            }
//        });

        holder.imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikeAndComment.class);
                intent.putExtra("uri", dataSet.get(listPosition).getPicture().get(0));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}