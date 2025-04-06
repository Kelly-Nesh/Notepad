package com.example.notepad;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private EditText noteEditText;
    private NoteStorage noteStorage;
    private int currentNoteId = -1;
    private int currentNotePosition = -1;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        RecyclerView recyclerView = MainActivity.getNotesRecyclerView();
        notesAdapter = (NotesAdapter) recyclerView.getAdapter();

        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if there is a note content to load
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String noteContent = extras.getString("noteContent");
            currentNoteId = extras.getInt("noteId");
            currentNotePosition = extras.getInt("position");
            if (noteContent != null) {
                noteEditText.setText(noteContent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            String note = noteEditText.getText().toString();
            noteStorage.saveNote(note, currentNoteId);
            List<Note> notes = notesAdapter.getNotes();
            if (currentNoteId < 0) {
                notes.add(new Note(note));
                notesAdapter.notifyItemInserted(notes.size());
            } else {
                notes.get(currentNotePosition).content = note;
                notesAdapter.notifyItemChanged(currentNotePosition);
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}