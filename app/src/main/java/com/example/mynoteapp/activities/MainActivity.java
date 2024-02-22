
package com.example.mynoteapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynoteapp.R;
import com.example.mynoteapp.adapters.FolderAdapter;
import com.example.mynoteapp.callback.FolderClickListener;
import com.example.mynoteapp.callback.OnClickItem;
import com.example.mynoteapp.callback.OnLongClickItem;
import com.example.mynoteapp.daos.FolderDao;
import com.example.mynoteapp.daos.NoteDao;
import com.example.mynoteapp.databases.Databases;
import com.example.mynoteapp.entities.Folder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;
    private List<Folder> folderList;
    private Dialog dialogAddFolder, dialogMultitask, dialogDeleteFolder, dialogEditFolder;
    private EditText textSearchFolder;
    private Folder quantityNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folderRecyclerView = findViewById(R.id.folderRecyclerView);
        folderRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );
        FolderDao folderDao = Databases.getDatabases(this).folderDao();
        NoteDao noteDao = Databases.getDatabases(this).noteDao();

        folderList = folderDao.getAllFolders();
        folderAdapter = new FolderAdapter(folderList);
        folderRecyclerView.setAdapter(folderAdapter);
        Folder folder = new Folder();


        textSearchFolder = findViewById(R.id.textSearchFolder);
        textSearchFolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                folderAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (folderList.size() != 0) {
                    folderAdapter.searchFolder(s.toString());
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarFolder);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showAddFolderDialog();
                return true;
            }
        });

        folderAdapter.setClickListener(new FolderClickListener() {
            @Override
            public void onClick(int position, Folder folder) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("folder", folder);
                startActivity(intent);
            }

            @Override
            public void onClickSetting(int position, Folder folder) {
                showMultitaskDialog(position, folder);
                Log.e("Xyz", "onItemLongClicked: " + folder.getFolderName());
                Log.e("Xyz", "onItemLongClicked: " + position);
            }
        });


        Log.e("Folder List", "onCreate: " + folderList.toString());
    }

    private void showMultitaskDialog(int position, Folder folder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_multitask,
                (ViewGroup) findViewById(R.id.layoutMultitask)
        );
        builder.setView(view);

        dialogMultitask = builder.create();

        ImageView imageClose = view.findViewById(R.id.imageClose);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMultitask.dismiss();
            }
        });

        LinearLayout lnDeleteMultitask = view.findViewById(R.id.layoutDeleteMultitask);
        lnDeleteMultitask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteFolderDialog(position, folder);
                Log.e("Xyz", "Multitask Name: " + folder.getFolderName());
                Log.e("Xyz", "Multitask Id: " + folder.getFolderId());
                Log.e("Xyz", "Multitask Position: " + position);
                dialogMultitask.dismiss();
            }
        });

        LinearLayout lnEditMultitask = view.findViewById(R.id.layoutEditMultitask);
        lnEditMultitask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFolderDialog(position, folder);
                dialogMultitask.dismiss();
            }
        });
        dialogMultitask.show();
    }

    private void showEditFolderDialog(int position, Folder folder) {
        FolderDao folderDao = Databases.getDatabases(this).folderDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_edit_folder,
                (ViewGroup) findViewById(R.id.layoutEditFolderContainer)
        );
        builder.setView(view);

        dialogEditFolder = builder.create();

        EditText inputEditFolder = view.findViewById(R.id.inputEditFolder);
        inputEditFolder.requestFocus();
        inputEditFolder.setText(folder.getFolderName());

        TextView textEdit = view.findViewById(R.id.textEdit);
        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputEditFolder = view.findViewById(R.id.inputEditFolder);
                String folderName = inputEditFolder.getText().toString().trim();

                if (TextUtils.isEmpty(folderName)) {
                    return;
                }

                folder.setFolderName(folderName);
                folderDao.updateFolder(folder);
                folderAdapter.notifyItemChanged(position);

                Log.e("Xyz", "Multitask Name: " + folder.getFolderName());
                Log.e("Xyz", "Multitask Id: " + folder.getFolderId());
                Log.e("Xyz", "Multitask Position: " + position);
                dialogEditFolder.dismiss();
            }
        });

        TextView textCancel = view.findViewById(R.id.textCancel);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditFolder.dismiss();
            }
        });
        dialogEditFolder.show();
    }

    private void showDeleteFolderDialog(int position, Folder folder) {
        FolderDao folderDao = Databases.getDatabases(this).folderDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_delete_folder,
                (ViewGroup) findViewById(R.id.layoutDeleteFolderContainer)
        );
        builder.setView(view);

        dialogDeleteFolder = builder.create();

        TextView textDelete = view.findViewById(R.id.textDelete);
        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Xyz", "setDeleteFolder Name: " + folder.getFolderName());
                Log.e("Xyz", "setDeleteFolder Id: " + folder.getFolderId());
                Log.e("Xyz", "setDeleteFolder Position: " + position);
                folderDao.deleteFolder(folder);
                folderAdapter.removeFolder(folder);
                dialogDeleteFolder.dismiss();
            }
        });

        TextView textCancel = view.findViewById(R.id.textCancel);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteFolder.dismiss();
            }
        });
        dialogDeleteFolder.show();
    }

    private void showAddFolderDialog() {
        FolderDao folderDao = Databases.getDatabases(this).folderDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_add_folder,
                (ViewGroup) findViewById(R.id.layoutAddFolderContainer)
        );
        builder.setView(view);

        dialogAddFolder = builder.create();

        EditText inputAddFolder = view.findViewById(R.id.inputAddFolder);
        inputAddFolder.requestFocus();

        view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputAddFolder.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Name folder can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Folder folder = new Folder();
                    folder.setFolderName(inputAddFolder.getText().toString());
                    long id = folderDao.insertFolder(folder);
                    if (id > 0) {
                        folder.setFolderId(id);
                    }
                    dialogAddFolder.dismiss();
                    folderList.add(0, folder);
                    Toast.makeText(MainActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    folderAdapter.notifyItemInserted(0);
                    folderRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddFolder.dismiss();
            }
        });
        dialogAddFolder.show();
    }

}