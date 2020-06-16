package com.example.leadtheway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leadtheway.NoteActivity;
import com.example.leadtheway.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotesFragment extends Fragment {

    private NoteRecyclerAdapter noteRecyclerAdapter;
    private View root;



    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.activity_note_list, container, false);
        Toast.makeText(getActivity(), "Press + button to add note..", Toast.LENGTH_LONG).show();
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoteActivity.class));
            }
        });

        initializeDisplayContent();


        return root;
    }

    private void initializeDisplayContent() {
        final RecyclerView recyclerNotes = root.findViewById(R.id.list_notes);

        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(getContext());
        recyclerNotes.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        noteRecyclerAdapter = new NoteRecyclerAdapter(getContext(),notes);

        recyclerNotes.setAdapter(noteRecyclerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        noteRecyclerAdapter.notifyDataSetChanged();
    }
}
