package com.example.apple.tabssample.Fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.tabssample.Activities.ViewEvent;
import com.example.apple.tabssample.Classes.Event;
import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.example.apple.tabssample.tab1EventAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

public class TabFragment2 extends Fragment {
    private EventListener listener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Event> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static Activity activity123 ;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity123 = getActivity();

        View RootView = inflater.inflate(R.layout.tab_fragment_1, container, false);
        myOnClickListener = new TabFragment2.MyOnClickListener(getContext());

        recyclerView = (RecyclerView) RootView.findViewById(R.id.tab1RecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<Event>();
        firebaseRef.getInstance().getMyRef().child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vector<String> pics = new Vector<>();
                Vector<String> going = new Vector<>();
                Vector<String> interested = new Vector<>();
                Vector<String> shares = new Vector<>();

                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    for(DataSnapshot c: childSnap.child("pictures").getChildren())
                    {
                        pics.add(c.getValue()+"");
                    }
                    for(DataSnapshot c: childSnap.child("going").getChildren())
                    {
                        going.add(c.getValue()+"");
                    }
                    for(DataSnapshot c: childSnap.child("interested").getChildren())
                    {
                        interested.add(c.getValue()+"");
                    }
                    for(DataSnapshot c: childSnap.child("shares").getChildren())
                    {
                        shares.add(c.getValue()+"");
                    }

                    Event e=new Event(childSnap.child("id").getValue()+"",
                            childSnap.child("startDate").getValue()+"",
                            childSnap.child("endDate").getValue()+"",
                            childSnap.child("title").getValue()+"",
                            childSnap.child("description").getValue()+"",
                            childSnap.child("location").getValue()+"",
                            pics,
                            interested,
                            going,
                            shares);


                    data.add(e);
                    Log.d("",e.getTitle());
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

        adapter = new tab1EventAdapter(data,getContext());
        recyclerView.setAdapter(adapter);

        return RootView;
    }
    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            viewEvent(v);
        }

        public void viewEvent(View v){


            if(activity123 instanceof MainActivity) {
                Intent i= new Intent((MainActivity) activity123,ViewEvent.class);
                i.putExtra("eventObject",data.get( recyclerView.getChildAdapterPosition(v)));
                ((MainActivity) activity123).startActivity(i);
                ((MainActivity) activity123).viewEvent();
            }
        }
        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.single_event_title);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < data.size(); i++) {
                if (selectedName.equals(data.get(i).getTitle())) {
                    //  selectedItemId = MyData.id_[i];
                }
            }
            //removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }




}