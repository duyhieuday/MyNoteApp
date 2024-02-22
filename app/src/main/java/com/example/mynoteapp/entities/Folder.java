package com.example.mynoteapp.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Folder implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long folderId;
    public String folderName;

    public int quantityNoteInFolder = 0;

    public int getQuantityNoteInFolder() {
        return quantityNoteInFolder;
    }

    public void setQuantityNoteInFolder(int quantityNoteInFolder) {
        this.quantityNoteInFolder = quantityNoteInFolder;
    }

    public Folder() {
    }

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public long getFolderId() {
        return folderId;
    }
    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folderId=" + folderId +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
