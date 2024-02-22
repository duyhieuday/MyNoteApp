package com.example.mynoteapp.callback;

import com.example.mynoteapp.entities.Folder;

public interface FolderClickListener {
    void onClick(int position, Folder folder);
    public void onClickSetting(int position, Folder folder);
}
