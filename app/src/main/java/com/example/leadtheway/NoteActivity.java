package com.example.leadtheway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.leadtheway.ui.DataManager;
import com.example.leadtheway.ui.NoteInfo;
import com.example.leadtheway.ui.PlaceInfo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION = "com.example.leadtheway.ui.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;

    private NoteInfo mNote;

    private boolean mIsNewNote;
    private Spinner spinnerPlaces;
    private EditText textNoteTitle;
    private EditText textNoteText;

    private boolean mIsCancelling;
    private String originalNotePlaceId;
    private String originalNoteTitle;
    private String originalNoteText;
    private int notePosition;
    private int position;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        spinnerPlaces = findViewById(R.id.spinner_places);

        List<PlaceInfo> courses = DataManager.getInstance().getPlaces();

        ArrayAdapter<PlaceInfo> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaces.setAdapter(adapter);


        readDisplayStateValues();
        SaveOriginalNoteValues();

        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text);
        //if it is not new note display note in the page.
        if(!mIsNewNote)
        displayNote(spinnerPlaces, textNoteTitle, textNoteText);

    }

    private void SaveOriginalNoteValues() {
        if(mIsNewNote){
            return;
        }
        originalNotePlaceId = mNote.getPlace().getPlaceId();
        originalNoteTitle = mNote.getTitle();
        originalNoteText = mNote.getText();
    }
    // display note
    private void displayNote(Spinner spinnerPlaces, EditText textNoteTitle, EditText textNoteText) {
       List<PlaceInfo> places  = DataManager.getInstance().getPlaces();
       int placeIndex =  places.indexOf(mNote.getPlace());

       spinnerPlaces.setSelection(placeIndex);

       textNoteTitle.setText(mNote.getTitle());
       textNoteText.setText(mNote.getText());
    }
    // when user click to the note read item position of note.If plus button pressed create new note.
    private void readDisplayStateValues() {
        Intent intent = getIntent();
        position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if (mIsNewNote) {
            createNewNote();
        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }
    //Create new note and add to the arraylist.
    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();
        mNote = dm.getNotes().get(notePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sendMail) {
            sendMail();
            return true;
        }else if(id == R.id.cancelNote){
            cancelNote();
            return true;
        }else if(id ==R.id.deleteNote){
            DataManager.getInstance().removeNote(position);
            startActivity(new Intent(NoteActivity.this, NoteListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(notePosition);
            }else{
                storePreviousNoteValues();
            }
        }else{
        saveNote();
    }
    }

    //store notes info if note activity pass to the Gmail.
    private void storePreviousNoteValues() {
        PlaceInfo place = DataManager.getInstance().getPlace(originalNotePlaceId);
        mNote.setPlace(place);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    //save not when back button pressed.
    private void saveNote() {
        mNote.setPlace((PlaceInfo) spinnerPlaces.getSelectedItem());
        mNote.setTitle(textNoteTitle.getText().toString());
        mNote.setText(textNoteText.getText().toString());

    }
    //if user edit note and did not want to delete user cancel the edit
    private void cancelNote() {
        mIsCancelling = true;
        finish();
    }
    //send note as a mail to the someone else via Gmail.
    private void sendMail() {
        PlaceInfo place = (PlaceInfo) spinnerPlaces.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = "Checkout what I visit in the travel \""+place.getTitle()+"\"\n"+textNoteText.getText();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);

        startActivity(intent);
    }


}
