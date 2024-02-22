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
public interface FolderDao {

    @Query("SELECT * FROM folder ORDER BY folderId DESC")
    List<Folder> getAllFolders();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFolder(Folder folder);
    @Delete
    void deleteFolder(Folder folder);
    @Update
    void updateFolder(Folder folder);
    @Query("SELECT * FROM folder WHERE folderName LIKE '%' || :keyword || '%'")
    List<Folder> searchFolder(String keyword);

}
