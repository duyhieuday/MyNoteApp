package com.example.mynoteapp.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteapp.R;
import com.example.mynoteapp.callback.NoteOnClickItem;
import com.example.mynoteapp.entities.Folder;
import com.example.mynoteapp.entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Note> noteList;
    private NoteOnClickItem noteOnClickItem;
    private Timer timer;
    private List<Note> noteSource;
    private String folderName;

    public NoteAdapter(List<Note> noteList, NoteOnClickItem noteOnClickItem) {
        this.noteList = noteList;
        this.noteOnClickItem = noteOnClickItem;
        noteSource = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textNameNote.setText(note.getTitle());
        holder.textTextNote.setText(note.getNoteText());
        holder.textDateTime.setText(note.getDateTime());
        holder.textNameFolderNote.setText(note.getNameFolder());


        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutItemNote.getBackground();
        if (note.getColor() != null){
            gradientDrawable.setColor(Color.parseColor(note.getColor()));
        }else {
            gradientDrawable.setColor(Color.parseColor("#333333"));
        }

        if (note.getImagePath() != null){
            holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            holder.imageNote.setVisibility(View.VISIBLE);
        }else {
            holder.imageNote.setVisibility(View.GONE);
        }

        holder.layoutItemNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteOnClickItem.onNoteClicked(holder.getAbsoluteAdapterPosition(), note);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textNameNote, textDateTime, textTextNote, textNameFolderNote;
        LinearLayout layoutItemNote;
        ImageView imageNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameNote = itemView.findViewById(R.id.textNameNote);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            textTextNote = itemView.findViewById(R.id.textTextNote);
            textNameFolderNote = itemView.findViewById(R.id.textNameFolderNote);
            layoutItemNote = itemView.findViewById(R.id.layoutItemNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }
    }

    public void searchNote(String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    noteList = noteSource;
                }else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource){
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()))
                        {
                            temp.add(note);
                        }
                    }
                    noteList = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }
    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }

}
