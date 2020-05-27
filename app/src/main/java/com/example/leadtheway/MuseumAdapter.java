package com.example.leadtheway;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MuseumAdapter extends RecyclerView.Adapter<MuseumAdapter.MuseumViewHolder>{

    ArrayList<Museum> museumArray;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    int [] idArray = {4,2,3,1};
    int sizeOfIdArray = 4;
    public MuseumAdapter(){
        FirebaseUtil.openFbReference("places");//hangi tablodan çekeceksen onun referensı
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference.child("Amsterdam").child("Museum");
        museumArray = FirebaseUtil.museumArray;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Museum museum = dataSnapshot.getValue(Museum.class);
                Log.d("Museum: ",museum.getTitle());
                museumArray.add(museum);
                notifyItemInserted(museumArray.size()-1);//notify the app Service item has been inserted so that user interface will be updated.
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
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    //is called when a RecyclerView needs a new ViewHolder.
    @Override
    public MuseumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.museum_row,parent,false);
        return new MuseumViewHolder(itemView);
    }
    //is called to display the data.
    @Override
    public void onBindViewHolder(@NonNull MuseumViewHolder holder, int position) {
            Museum museum =  museumArray.get(position);
            holder.bind(museum);
    }

    @Override
    public int getItemCount() {
        return museumArray.size();
    }

    // Bind Retrieved Data
    public class MuseumViewHolder extends RecyclerView.ViewHolder {// describe how to bind data
        TextView museumTitle;
        TextView expectedTime;
        TextView openCloseTime;

        public MuseumViewHolder(@NonNull View itemView) {
            super(itemView);
            museumTitle = (TextView) itemView.findViewById(R.id.museumTitle);
            expectedTime = (TextView)itemView.findViewById(R.id.museumExpectedTime);
            openCloseTime = (TextView)itemView.findViewById(R.id.museumOpenClose);
        }
        //binding the data is passed to the layout of our row
        public void bind(Museum museum){
            museumTitle.setText(museum.getTitle());
            expectedTime.setText(museum.getExpectedTime());
            openCloseTime.setText(museum.getOpenClosedTime());
        }
    }


}
