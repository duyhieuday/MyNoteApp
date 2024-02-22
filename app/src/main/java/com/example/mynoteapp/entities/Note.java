package com.example.mynoteapp.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = Folder.class,
        parentColumns = "folderId",
        childColumns = "folderId",  // Đây là tên cột trong bảng Note, không phải tên cột trong bảng Folder
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long noteId;
    public long folderId;
    public String title;
    public String dateTime;
    public String noteText;
    public String imagePath;

    public String color;
    public String webLink;
    public String nameFolder;

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public Note() {
    }

    public Note(long folderId, String title, String dateTime, String noteText, String imagePath, String color, String webLink) {
        this.folderId = folderId;
        this.title = title;
        this.dateTime = dateTime;
        this.noteText = noteText;
        this.imagePath = imagePath;
        this.color = color;
        this.webLink = webLink;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", folderId=" + folderId +
                ", title='" + title + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", noteText='" + noteText + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", color='" + color + '\'' +
                ", webLink='" + webLink + '\'' +
                '}';
    }
}
