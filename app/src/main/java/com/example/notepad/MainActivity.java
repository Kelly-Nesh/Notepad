package com.example.notepad;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private EditText noteEditText;
    private NoteStorage noteStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        noteStorage = new NoteStorage(this);

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
            }
        return super.onOptionsItemSelected(item);
    }

}