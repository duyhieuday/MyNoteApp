package com.example.mynoteapp.adapters;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteapp.R;
import com.example.mynoteapp.callback.FolderClickListener;
import com.example.mynoteapp.callback.OnClickItem;
import com.example.mynoteapp.callback.OnLongClickItem;
import com.example.mynoteapp.entities.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<Folder> folderList;
    private Timer timer;
    public FolderClickListener clickListener;
    private List<Folder> folderSource;

    public void setClickListener(FolderClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public FolderAdapter(List<Folder> folderList) {
        this.folderList = folderList;
        folderSource = folderList;
    }

    public void removeFolder(Folder folder){
        int index = folderList.indexOf(folder);
        if (index != -1){
            folderList.remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_folder,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.textNameFolder.setText(folder.getFolderName());
        holder.textQuantityFolder.setText(folder.getQuantityNoteInFolder() + "");

        holder.lnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(holder.getAbsoluteAdapterPosition(), folder);
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickSetting(holder.getAbsoluteAdapterPosition(), folder);
            }
        });
    }





    @Override
    public int getItemCount() {
        return folderList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView textNameFolder, textQuantityFolder;
        LinearLayout lnFolder;
        ImageButton imageButton;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameFolder = itemView.findViewById(R.id.textNameFolder);
            textQuantityFolder = itemView.findViewById(R.id.textQuantityFolder);
            lnFolder = itemView.findViewById(R.id.layoutFolder);
            imageButton = itemView.findViewById(R.id.imageButtonSetting);
        }

    }
    public void searchFolder(String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    folderList = folderSource;
                }else {
                    ArrayList<Folder> temp = new ArrayList<>();
                    for (Folder folder : folderSource){
                        if (folder.getFolderName().toLowerCase().contains(searchKeyword.toLowerCase()))
                        {
                            temp.add(folder);
                        }
                    }
                    folderList = temp;
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
