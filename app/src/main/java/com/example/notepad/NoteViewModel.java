package com.example.notepad;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteViewModel extends AndroidViewModel {
    public LiveData<List<Note>> allNotes;
    private NoteDao noteDao;
    private ExecutorService executorService;
    ;

    public NoteViewModel(Application application) {
        super(application);
        NoteDatabase db = MainActivity.getNoteDatabase();
        noteDao = db.noteDao();
        allNotes = noteDao.getAllLiveDataNotes();
        executorService = Executors.newFixedThreadPool(4);
    }

    public void insertNote(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void updateNote(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void deleteNote(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public Note getNoteById(int noteId) {
        return noteDao.getNoteById(noteId);
    }
}
