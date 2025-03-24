package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> notes = new ArrayList<>();

    public NotesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.noteTextView.setText(currentNote.content.substring(0, Math.min(currentNote.content.length(), 200)));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyItemRangeChanged(0, notes.size());
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
        }
    }
}
