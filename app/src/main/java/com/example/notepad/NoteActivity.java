package com.example.notepad;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends AppCompatActivity {
    private EditText noteTitleText;
    private EditText noteEditText;
    private NoteStorage noteStorage;
    private int currentNoteId = -1;
    private boolean newNoteAdded = false; // Used when note is added and deleted without leaving activity
    private int currentNotePosition = -1;
    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        recyclerView = MainActivity.getNotesRecyclerView();
        notesAdapter = (NotesAdapter) recyclerView.getAdapter();

        noteTitleText = findViewById(R.id.noteTitleText);
        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if there is a note content to load
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String noteTitle = extras.getString("noteTitle");
            String noteContent = extras.getString("noteContent");
            currentNoteId = extras.getInt("noteId");
            currentNotePosition = extras.getInt("position");
            if (noteTitle != null) {
                noteTitleText.setText(noteTitle);
            }
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

        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String message = noteTitleText.getText().toString() + "\n\n" + noteEditText.getText().toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Share Note"));
        } else if (id == R.id.action_save) {
            saveNote();
            return true;
        } else if (id == R.id.action_clear) {
//            createNotes();
            noteEditText.setText("");
            return true;
        } else if (id == R.id.action_paste) {
            pasteNote();
        } else if (id == R.id.action_delete) {
            deleteNote();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = noteTitleText.getText().toString();
        String note = noteEditText.getText().toString();

        boolean saved = noteStorage.saveNote(title, note, currentNoteId, (noteId) -> {
            runOnUiThread(() -> {
                if (currentNoteId < 0) {
                    Note newNote = new Note(title, note);
                    newNote.id = noteId;
                    notesAdapter.getNotes().add(0, newNote);
                    currentNoteId = noteId;
                    newNoteAdded = true;
                    notesAdapter.notifyItemInserted(0);
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    notesAdapter.getNotes().get(currentNotePosition).title = title;
                    notesAdapter.getNotes().get(currentNotePosition).content = note;
                    notesAdapter.notifyItemChanged(currentNotePosition);
                }
            });
        });
        if (saved) {
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void pasteNote() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData == null) {
                return;
            }
            ClipData.Item item = clipData.getItemAt(0);
            String text = item.getText().toString();
            text = noteEditText.getText().toString() + text;
            noteEditText.setText(text);
        } else {
            Toast.makeText(this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote() {
        if (currentNoteId < 0) {
            Toast.makeText(this, "Cannot delete note. Note is not saved.", Toast.LENGTH_SHORT).show();
            return;
        }
        noteStorage.deleteNote(currentNoteId, () -> runOnUiThread(() -> {
            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
        }));
        notesAdapter.getNotes().remove(Math.max(currentNotePosition, 0));
        notesAdapter.notifyItemRemoved(currentNotePosition);
        notesAdapter.notifyItemRangeChanged(currentNotePosition, notesAdapter.getNotes().size() - currentNotePosition);

        finish();
    }

    /* Create testing notes */
    private void createNotes() {
        String[] paragraphs = {
                getResources().getString(R.string.paragraph1),
                getResources().getString(R.string.paragraph2),
                getResources().getString(R.string.paragraph3),
                getResources().getString(R.string.paragraph4),
                getResources().getString(R.string.paragraph5),
                getResources().getString(R.string.paragraph6),
                getResources().getString(R.string.paragraph7),
                getResources().getString(R.string.paragraph8),
                getResources().getString(R.string.paragraph9),
                getResources().getString(R.string.paragraph10),
                getResources().getString(R.string.paragraph11),
                getResources().getString(R.string.paragraph12),
                getResources().getString(R.string.paragraph13),
                getResources().getString(R.string.paragraph14),
                getResources().getString(R.string.paragraph15),
                getResources().getString(R.string.paragraph16),
                getResources().getString(R.string.paragraph17),
                getResources().getString(R.string.paragraph18),
                getResources().getString(R.string.paragraph19),
                getResources().getString(R.string.paragraph20)
        };

        for (String p : paragraphs) {
            noteStorage.saveNote(String.valueOf(Math.random()), p, -1, (n) -> {
                runOnUiThread(() -> System.out.println("Saved"));
            });
        }
    }
}