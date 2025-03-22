package com.example.notepad;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class NoteDao {
    @Insert
    abstract void insert(Note note);

    @Update
    abstract void update(Note note);

    @Delete
    abstract void delete(Note note);

    @Query("SELECT * FROM notes")
    abstract List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId")
    abstract Note getNoteById(int noteId);
}
