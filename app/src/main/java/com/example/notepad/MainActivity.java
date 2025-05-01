package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private static NoteDatabase noteDatabase;
    private NoteStorage noteStorage;
    private NotesAdapter notesAdapter;

    public static NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    public static RecyclerView notesRecyclerView;

    public static RecyclerView getNotesRecyclerView() {return notesRecyclerView;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteDatabase = NoteDatabase.getDatabase(this);
        setContentView(R.layout.activity_main);

        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> createNewNote());

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this);
        notesRecyclerView.setAdapter(notesAdapter);

        loadNotes();
    }

    private void createNewNote() {
        // Navigate to a new activity to create a new note
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void loadNotes() {
        noteStorage.getNoteList(notes -> runOnUiThread(() -> notesAdapter.setNotes(notes)));
    }
}