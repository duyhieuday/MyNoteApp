package com.example.mynoteapp.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynoteapp.daos.FolderDao;
import com.example.mynoteapp.daos.NoteDao;
import com.example.mynoteapp.entities.Folder;
import com.example.mynoteapp.entities.Note;

@Database(entities = {Folder.class, Note.class}, version = 1, exportSchema = false)
public abstract class Databases extends RoomDatabase {

    private static Databases databases;

    public static synchronized Databases getDatabases(Context context){
        if (databases == null){
            databases = Room.databaseBuilder(
                    context,
                    Databases.class,
                    "my_db"
            ).allowMainThreadQueries().build();
        }
        return databases;
    }

    public abstract FolderDao folderDao();
    public abstract NoteDao noteDao();
}
