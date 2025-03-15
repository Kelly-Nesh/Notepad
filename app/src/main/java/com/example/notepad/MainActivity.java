package com.example.notepad;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private EditText noteEditText;
    private NoteStorage noteStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadNote();
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
            noteStorage.saveNote(note);
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

    private void loadNote() {
        String note = noteStorage.loadNote();
        noteEditText.setText(note);
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
        noteStorage.deleteNote();
        Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
    }

}