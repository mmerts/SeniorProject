package com.example.leadtheway.ui;

import android.provider.BaseColumns;

public final class NoteKeeperDatabaseContract {
    private NoteKeeperDatabaseContract(){}

    public static final class PlaceInfoEntry implements BaseColumns {

        public static final String TABLE_NAME ="place_info";
        public static final String COLUMN_PLACE_ID ="place_id";
        public static final String COLUMN_PLACE_TITLE = "place_title";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PLACE_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PLACE_TITLE + " TEXT NOT NULL)";
        public static final class NoteInfoEntry{
            public static final String TABLE_NAME ="note_info";
            public static final String COLUMN_NOTE_TITLE = "note_title";
            public static final String COLUMN_NOTE_TEXT = "note_text";
            public static final String COLUMN_PLACE_ID ="place_id";

            public static final String SQL_CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                    COLUMN_NOTE_TEXT + " TEXT, " +
                    COLUMN_PLACE_ID + " TEXT NOT NULL)";


        }

    }
}
