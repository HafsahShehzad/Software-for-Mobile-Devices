package com.example.apple.tabssample.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.apple.tabssample.Activities.LikeAndComment;
import com.example.apple.tabssample.Activities.PostActivity;
import com.example.apple.tabssample.Activities.createPost;
import com.example.apple.tabssample.Classes.PictureComment;
import com.example.apple.tabssample.Classes.Likes;
import com.example.apple.tabssample.Classes.Share;
import com.example.apple.tabssample.PostAdapter;
import com.example.apple.tabssample.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.tabssample.Classes.Post;
import com.example.apple.tabssample.Activities.ViewPost;
import com.example.apple.tabssample.firebaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

public class PostTabFragment1 extends Fragment {
    private EventListener listener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Post> data;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static Activity activity123;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity123 = getActivity();

        View RootView = inflater.inflate(R.layout.post_tab_fragment_1, container, false);
        myOnClickListener = new PostTabFragment1.MyOnClickListener(getContext());

        recyclerView = (RecyclerView) RootView.findViewById(R.id.posttab1RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<Post>();

        firebaseRef.getInstance().getMyRef().child("Posts").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data.clear(); ///Avoid duplicate entrie on likes on post
                Vector<String> pics = new Vector<>();
//                Vector<String> going = new Vector<>();
//                Vector<String> interested = new Vector<>();
//                Vector<String> shares = new Vector<>();

                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot c : childSnap.child("pictures").getChildren()) {
                        pics.add(c.getValue() + "");
                    }
//                    for (DataSnapshot c : childSnap.child("going").getChildren()) {
//                        going.add(c.getValue() + "");
//                    }
//                    for (DataSnapshot c : childSnap.child("interested").getChildren()) {
//                        interested.add(c.getValue() + "");
//                    }
//                    for (DataSnapshot c : childSnap.child("shares").getChildren()) {
//                        shares.add(c.getValue() + "");
//                    }
                   Post e = new Post(childSnap.child("id").getValue() + "",
                            childSnap.child("date").getValue() + "",
                            childSnap.child("location").getValue() + "",
                            childSnap.child("title").getValue() + "",
                            childSnap.child("description").getValue() + "",
                           childSnap.child("publisher").getValue() + "",
                            pics,
                            new Vector<Likes>(),
                           new Vector<PictureComment>(),
                           new Vector<Share>());


                    data.add(e);
//                    Toast.makeText(getContext(), "Clicked000000", Toast.LENGTH_SHORT).show();

//                    Log.v("tmz",""+ chidSnap.getKey()); //displays the key for the node
//                    Log.v("tmz",""+ chidSnap.child("market_name").getValue());   //gives the value for given keyname
//
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        removedItems = new ArrayList<Integer>();

        adapter = new PostAdapter(data, getContext());
        recyclerView.setAdapter(adapter);

        RootView.findViewById(R.id.add_post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               {
                    Intent i = new Intent(getActivity(), createPost.class);
                    getActivity().startActivity(i);
                }
            }
        });

        return RootView;
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            LikeAndComment(v);
        }

        public void LikeAndComment(View v) {


            if (activity123 instanceof PostActivity) {
                Intent i = new Intent((PostActivity) activity123, LikeAndComment.class);
                i.putExtra("postObject", data.get(recyclerView.getChildAdapterPosition(v)));
                ((PostActivity) activity123).startActivity(i);
                //((MainActivity) activity123).viewEvent();
            }
        }
    }
}



