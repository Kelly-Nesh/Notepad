package com.example.notepad;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoteStorage {
    private static final String FILE_NAME = "note.txt";
    private final Context context;

    public NoteStorage(Context context) {
        this.context = context;
    }

    public boolean saveNote(String note) {
        if (note.isEmpty()) {
            Toast.makeText(context, "Cannot save empty note", Toast.LENGTH_SHORT).show();
            return false;
        }
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileOutputStream fos = null;

        if (file.exists()) {
            try {
                fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fos.write(note.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error saving note", Toast.LENGTH_SHORT).show();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Toast.makeText(context, "Note saved!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public String loadNote() {
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(FILE_NAME);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            String note = new String(buffer);
            return note;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error loading note", Toast.LENGTH_SHORT).show();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
