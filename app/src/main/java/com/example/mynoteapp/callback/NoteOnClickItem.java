package com.example.mynoteapp.callback;

import com.example.mynoteapp.entities.Note;

public interface NoteOnClickItem {
    void onNoteClicked( int position, Note note);
}
