package com.example.notepad;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile NoteDatabase INSTANCE;

    public static NoteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class,
                        "note_database").addMigrations(DbMigrations.MIGRATION_1_2).build();
            }
        }
        return INSTANCE;
    }

    ;
}
