package com.example.notepad;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final Context context;
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
        holder.noteTextView.setText(getPreviewText(currentNote.content));
        holder.itemView.setOnClickListener(v -> viewNote(currentNote.content));
    }

    private void viewNote(String noteContent) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("noteContent", noteContent);
        startActivity(context, intent, null);
    }

    private String getPreviewText(String text) {
        int lineCount = 0;
        int indexCount = 0;
        for (String x : text.split("")) {
            if (Objects.equals(x, "\n")) {
                lineCount++;
            }
            indexCount++;
            if (lineCount == 5) {
                break;
            }
        }
        String previewText = text.substring(0,
                Math.min(text.length(), indexCount));
        previewText = previewText.length() > 500 ? previewText.substring(0, 500) : previewText;
        return previewText;
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
        private final TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
        }
    }
}
