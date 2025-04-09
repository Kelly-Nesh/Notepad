package com.example.notepad;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteStorage {
    private final Context context;
    private ExecutorService executorService;
    private NoteDao noteDao;

    public NoteStorage(Context context) {
        this.context = context;
        this.executorService = Executors.newFixedThreadPool(2);
        this.noteDao = MainActivity.getNoteDatabase().noteDao();
    }

    public boolean saveNote(String noteContent, final int noteId) {
        if (noteContent.isEmpty()) {
            Toast.makeText(context, "Cannot save empty note!", Toast.LENGTH_SHORT).show();
            return false;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Note note = new Note(noteContent);
                if (noteId >= 0) {
                    note.id = noteId;
                    noteDao.update(note);
                } else {
                    noteDao.insert(note);
                }
            }
        });
        Toast.makeText(context, "Note saved!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void deleteNote(final int noteId) {
        if (noteId < 0) {
            Toast.makeText(context, "Cannot delete unsaved note!", Toast.LENGTH_SHORT).show();
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Note note = noteDao.getNoteById(noteId);
                noteDao.delete(note);
            }
        });
        Toast.makeText(context, "Note deleted!", Toast.LENGTH_SHORT).show();
    }

    public void getNoteList(final GetNoteListCallback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> notes = noteDao.getAllNotes();
                callback.onNoteListLoaded(notes);
            }
        });
    }

    public interface GetNoteListCallback {
        void onNoteListLoaded(List<Note> notes);
    }
}
