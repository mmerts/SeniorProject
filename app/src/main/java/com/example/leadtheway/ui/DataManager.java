package com.example.leadtheway.ui;

import com.example.leadtheway.FirebaseUtil;
import com.example.leadtheway.Museum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static com.example.leadtheway.FirebaseUtil.mDatabaseReference;

/*
Manage the note's data behind the android
 */
public class DataManager {

    FirebaseAuth firebaseAuth;

    private static DataManager ourInstance = null;

    private List<PlaceInfo> mPlaces = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();



    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializePlaces();

        }
        return ourInstance;
    }
    public int createNewNote() {
        NoteInfo note = new NoteInfo(null, null, null);
        mNotes.add(note);

        return mNotes.size() - 1;
    }

    public String getCurrentUserName() {
        return "Mustafa Mert Süerkan";
    }

    public String getCurrentUserEmail() {
        return "mmert.suerkan@tedu.edu.tr";
    }

    public List<NoteInfo> getNotes() {
        return mNotes;
    }



    public int findNote(NoteInfo note) {
        for(int index = 0; index < mNotes.size(); index++) {
            if(note.equals(mNotes.get(index)))
                return index;
        }

        return -1;
    }

    public void removeNote(int index) {
        mNotes.remove(index);
    }

    public List<PlaceInfo> getPlaces() {
        return mPlaces;
    }

    public PlaceInfo getPlace(String id) {
        for (PlaceInfo place : mPlaces) {
            if (id.equals(place.getPlaceId()))
                return place;
        }
        return null;
    }

    public List<NoteInfo> getNotes(PlaceInfo place) {
        ArrayList<NoteInfo> notes = new ArrayList<>();
        for(NoteInfo note:mNotes) {
            if(place.equals(note.getPlace()))
                notes.add(note);
        }
        return notes;
    }

    public int getNoteCount(PlaceInfo place) {
        int count = 0;
        for(NoteInfo note:mNotes) {
            if(place.equals(note.getPlace()))
                count++;
        }
        return count;
    }

    private DataManager() {
    }

    //region Initialization code

    private void initializePlaces() {
        List<Museum> placeArray = TimetableFragment.getMuseumList();
        for(int i=0;i<placeArray.size();i++) {
            mPlaces.add(new PlaceInfo(Integer.toString(placeArray.get(i).getId()), placeArray.get(i).getTitle()));
        }

    }


}
