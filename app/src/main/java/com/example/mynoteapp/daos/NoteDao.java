package com.example.mynoteapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mynoteapp.entities.Folder;
import com.example.mynoteapp.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Note Where folderId =:folderId")
    List<Note> getNoteForFolder(long folderId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);
    @Update
    void updateNote(Note note);
    @Delete
    void deleteNote(Note note);
    @Query("SELECT folderId, folderName, quantityNoteInFolder FROM Folder WHERE folderId = :folderId ")
    Folder findByNameFolderId(long folderId);
}
