package com.example.leadtheway;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MuseumList extends ArrayAdapter<Museum> {

    private Activity context;
    private List<Museum> museumList;

    public MuseumList(Context context, List<Museum> museumList) {
        super(context, R.layout.list_layout, museumList);
        this.context = (Activity) context;
        this.museumList = museumList;
    }

    //display museum information into timetable fragment.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView timeSchedule = (TextView) listViewItem.findViewById(R.id.timeSchedule);
        Museum museum = museumList.get(position);
        textViewTitle.setText(museum.getTitle());
        timeSchedule.setText(museum.getTimeSchedule());

        return listViewItem;
    }
}
