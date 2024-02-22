package com.example.mynoteapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynoteapp.R;
import com.example.mynoteapp.adapters.NoteAdapter;
import com.example.mynoteapp.callback.NoteOnClickItem;
import com.example.mynoteapp.daos.FolderDao;
import com.example.mynoteapp.daos.NoteDao;
import com.example.mynoteapp.databases.Databases;
import com.example.mynoteapp.entities.Folder;
import com.example.mynoteapp.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements NoteOnClickItem {

    private ImageView imageNoteBack, imageAddNote;
    private List<Note> noteList;
    private RecyclerView noteRecyclerView;
    private NoteAdapter noteAdapter;
    private EditText textSearchFolder;
    private Folder folder;
    public static final int REQUEST_CODE_UPDATE_NOTE = 1;
    public static final int REQUEST_CODE_SHOW_NOTE = 2;
    public static final int REQUEST_CODE_ADD_NOTE = 3;
    private int noteClickedPosition = -1;

    @SuppressLint({"NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        folder = (Folder) getIntent().getSerializableExtra("folder");

        imageNoteBack = findViewById(R.id.imageNoteBack);
        imageNoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView textNote = findViewById(R.id.textNote);
        textNote.setText(folder.getFolderName());

        imageAddNote = findViewById(R.id.imageAddNote);
        imageAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, CreateNoteActivity.class);
                Folder folder = (Folder) getIntent().getSerializableExtra("folder");
                intent.putExtra("folder", folder);
                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            }
        });

        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        noteRecyclerView.setAdapter(noteAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTE, false);

        textSearchFolder = findViewById(R.id.textSearchFolder);
        textSearchFolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0) {
                    noteAdapter.searchNote(s.toString());
                }
            }
        });

        NoteDao noteDao = Databases.getDatabases(this).noteDao();

        TextView totalNoteInFolder = findViewById(R.id.totalNoteInFolder);
        totalNoteInFolder.setText(noteDao.getNoteForFolder(folder.getFolderId()).size() + " notes ");

    }

    private void getNotes(int requestCode, boolean isNoteDeleted) {

        FolderDao folderDao = Databases.getDatabases(this).folderDao();
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return Databases
                        .getDatabases(getApplicationContext())
                        .noteDao().getNoteForFolder(folder.getFolderId());
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                folderDao.updateFolder(folder);
                if (requestCode == REQUEST_CODE_SHOW_NOTE) {
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    folder.setQuantityNoteInFolder(folder.getQuantityNoteInFolder() + 1);
                    folderDao.updateFolder(folder);
                    noteList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                    noteRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    folder.setQuantityNoteInFolder(folder.getQuantityNoteInFolder());
                    folderDao.updateFolder(folder);
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        folder.setQuantityNoteInFolder(folder.getQuantityNoteInFolder() - 1);
                        folderDao.updateFolder(folder);
                        noteAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        noteAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    public void onNoteClicked(int position, Note note) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("folder", folder);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }
}