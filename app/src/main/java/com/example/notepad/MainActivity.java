package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static NoteDatabase noteDatabase;
    private NoteStorage noteStorage;
    private ExecutorService executorService;
    private NotesAdapter notesAdapter;
    private List<Note> notes = new ArrayList<>();

    public static NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(2);
        noteDatabase = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "note_database").build();
        setContentView(R.layout.activity_main);

        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this);
        notesRecyclerView.setAdapter(notesAdapter);

        loadNotes();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private void createNewNote() {
        // Navigate to a new activity to create a new note
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void loadNotes() {
        noteStorage.getNoteList(new NoteStorage.GetNoteListCallback() {
            @Override
            public void onNoteListLoaded(List<Note> notes) {
                runOnUiThread(() -> {
                    notesAdapter.setNotes(notes);
                });
            }
        });
    }

    public interface NoteListCallback {
        void onNoteListUpdated(List<Note> notes);
    }
}