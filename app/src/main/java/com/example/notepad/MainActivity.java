package com.example.notepad;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "note.txt";
    private EditText noteEditText;
    private Button saveButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);

        loadNote();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = noteEditText.getText().toString();
                if (!note.isEmpty()) {
                    saveNote(note);
                    Toast.makeText(MainActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Cannot save empty note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                noteEditText.setText("");
            }
        });
    }

    private void saveNote(String note) {
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