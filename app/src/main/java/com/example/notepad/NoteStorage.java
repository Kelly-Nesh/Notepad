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

    public void saveNote(String note) {
        if (note.isEmpty()) {
            Toast.makeText(context, "Cannot save empty note", Toast.LENGTH_SHORT).show();
        }
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileOutputStream fos = null;
        boolean fullySaved = false;

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error creating file", Toast.LENGTH_SHORT).show();
            }
        }

        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(note.getBytes());
            fullySaved = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving note", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    if (fullySaved) {
                        Toast.makeText(context, "Note saved!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadNote() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return "";
        }

        FileInputStream fis = null;

        try {
            fis = context.openFileInput(FILE_NAME);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            return new String(buffer);

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

    public void deleteNote() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(context, "Note deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
