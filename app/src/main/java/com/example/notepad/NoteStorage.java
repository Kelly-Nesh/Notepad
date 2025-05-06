package com.example.notepad;

import android.content.Context;

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

    public boolean saveNote(String noteTitle, String noteContent, final int noteId, SaveNoteCallback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Note note = new Note(noteTitle, noteContent);
                if (noteId >= 0) {
                    note.id = noteId;
                    noteDao.update(note);
                    callback.onNoteSaved(note.id);
                } else {
                    noteDao.insert(note);
                    callback.onNoteSaved(noteDao.getNewNoteId());
                }
            }
        });
        return true;
    }

    public void deleteNote(final int noteId, final DeleteNoteCallback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Note note = noteDao.getNoteById(noteId);
                noteDao.delete(note);
                callback.onNoteDeleted();
            }
        });
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

    public interface SaveNoteCallback {
        void onNoteSaved(int noteId);
    }

    public interface DeleteNoteCallback {
        void onNoteDeleted();
    }
}
