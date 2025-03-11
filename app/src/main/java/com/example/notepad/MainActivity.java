package com.example.notepad;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "note.txt";
    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);

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
            saveNote(note);
            return true;
        } else if (id == R.id.action_clear) {
            noteEditText.setText("");
            return true;
            }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote(String note) {
        if (note.isEmpty()) {
            Toast.makeText(this, "Cannot save empty note", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getFilesDir(), FILE_NAME);
        FileOutputStream fos = null;

        if (file.exists()) {
            try {
                fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fos.write(note.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } else {
            noteEditText.setText("");
        }
    }

    private void loadNote() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            String note = new String(buffer);
            noteEditText.setText(note);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading note", Toast.LENGTH_SHORT).show();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}