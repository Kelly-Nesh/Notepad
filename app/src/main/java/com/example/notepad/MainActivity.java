package com.example.notepad;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static NoteDatabase noteDatabase;
    private EditText noteEditText;
    private NoteStorage noteStorage;
    private ExecutorService executorService;
    private int currentNoteId = -1;
    private FloatingActionButton fab;

    public static NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(2);
        noteDatabase = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "note_database").build();
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });
        loadNotes();
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
            return true;
        } else if (id == R.id.action_clear) {
            noteEditText.setText("");
            return true;
        } else if (id == R.id.action_paste) {
            pasteFromClipboard();
        } else if (id == R.id.action_delete) {
            deleteNote();
        }
        return super.onOptionsItemSelected(item);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private void createNewNote() {
        noteEditText.setText("");
        currentNoteId = -1;
    }

    private void loadNotes() {
        noteStorage.getNoteList(new NoteStorage.GetNoteListCallback() {
            @Override
            public void onNoteListLoaded(List<Integer> noteIds) {
                if (!noteIds.isEmpty()) {
                    // if there are notes, load the last one.
                    currentNoteId = noteIds.get(noteIds.size() - 1);
                    noteStorage.loadNote(currentNoteId, new NoteStorage.LoadNoteCallback() {
                        @Override
                        public void onNoteLoaded(String noteContent) {
                            runOnUiThread(() -> {
                                noteEditText.setText(noteContent);
                            });
                        }
                    });
                }
            }
        });
    }

    private void pasteFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData == null) {
                return;
            }
            ClipData.Item item = clipData.getItemAt(0);
            String text = item.getText().toString();
            noteEditText.append(text);
        } else {
            Toast.makeText(this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote() {
        noteEditText.setText("");
        noteStorage.deleteNote(currentNoteId);
    }

}