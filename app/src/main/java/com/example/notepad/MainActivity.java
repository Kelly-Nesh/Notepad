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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String note = noteStorage.loadNote();
        noteEditText.setText(note);
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
            boolean saved = noteStorage.saveNote(note);
            if (!saved) {
                noteEditText.setText("");
            }
            return true;
        } else if (id == R.id.action_clear) {
            noteEditText.setText("");
            return true;
        } else if (id == R.id.action_paste) {
            pasteFromClipboard();
        }
        return super.onOptionsItemSelected(item);
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

}