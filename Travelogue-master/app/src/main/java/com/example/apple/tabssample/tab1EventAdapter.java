package com.example.apple.tabssample;

import android.content.Context;
import android.content.Intent;
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
import com.example.apple.tabssample.Classes.Event;
import com.example.apple.tabssample.Fragments.TabFragment1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class tab1EventAdapter extends RecyclerView.Adapter<tab1EventAdapter.MyViewHolder> {

    private ArrayList<Event> dataSet;
    final private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView startDateTV, endDateTV, titleTV, locationTV;
        Button interestedBtn,goingBtn;

        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.startDateTV = (TextView) itemView.findViewById(R.id.single_event_time);
            this.titleTV = (TextView) itemView.findViewById(R.id.single_event_title);
            this.locationTV = (TextView) itemView.findViewById(R.id.single_event_location);
            this.imageViewIcon=(ImageView)itemView.findViewById(R.id.single_event_imageView);
            this.interestedBtn=(Button)itemView.findViewById(R.id.single_event_interested_btn);
            this.goingBtn=(Button)itemView.findViewById(R.id.single_event_going_btn);
        }
    }

    public tab1EventAdapter(ArrayList<Event> data,Context context) {
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event, parent, false);

        view.setOnClickListener(TabFragment1.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        TextView startDateTV=holder.startDateTV;
        TextView endDateTV;
        TextView titleTV=holder.titleTV;
        TextView locationTV=holder.locationTV;
        ImageView imageView = holder.imageViewIcon;

        startDateTV.setText(dataSet.get(listPosition).getStartDate()+" - "+dataSet.get(listPosition).getEndDate());
        titleTV.setText(dataSet.get(listPosition).getTitle());
        locationTV.setText(dataSet.get(listPosition).getLocation());
        Glide.with(context)
                .load(dataSet.get(listPosition).getPicture().get(0))
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, LikeAndComment.class);
                i.putExtra("uri",dataSet.get(listPosition).getPicture()+"");
                context.startActivity(i);
            }
        });
        holder.interestedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked",
                        Toast.LENGTH_SHORT).show();
                holder.interestedBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));
                final String currentUser= firebaseRef.getInstance().getCurrentUser();
                final String currentObjectId=dataSet.get(holder.getAdapterPosition()).getId();
                firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("going").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            holder.goingBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Black));
                            firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("going").child(currentUser).setValue(null);
                            firebaseRef.getInstance().getUsers().child(currentUser).child("going").child(currentObjectId).setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                firebaseRef.getInstance().getUsers().child(currentUser).child("interested").child(currentObjectId).setValue(true);
                firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("interested").child(currentUser).setValue(true);

            }
        });
        holder.goingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked",
                        Toast.LENGTH_SHORT).show();
               holder.goingBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Green));
                final String currentUser= firebaseRef.getInstance().getCurrentUser();
                final String currentObjectId=dataSet.get(holder.getAdapterPosition()).getId();
                firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("interested").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            holder.interestedBtn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.Black));
                            firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("interested").child(currentUser).setValue(null);
                            firebaseRef.getInstance().getUsers().child(currentUser).child("interested").child(currentObjectId).setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                firebaseRef.getInstance().getUsers().child(currentUser).child("going").child(currentObjectId).setValue(true);
                firebaseRef.getInstance().getMyRef().child("Events").child(currentObjectId).child("going").child(currentUser).setValue(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}