package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class NoteEditorActivity extends AppCompatActivity {

    int noteIndex;

    TextView noteEditorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        /* Initialize Variables */
        noteEditorTextView = findViewById(R.id.noteEditor);
        noteIndex = getIntent().getIntExtra("note", 0);

        /* Set Text To Note Passed In Intent */
        if (noteIndex == -1) {
            noteEditorTextView.setText("");
        } else {
            noteEditorTextView.setText(MainActivity.notes.get(noteIndex));
        }
        noteEditorTextView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        if (noteIndex == -1) {
            MainActivity.notes.add(noteEditorTextView.getText().toString());
        } else {
            MainActivity.notes.set(noteIndex, noteEditorTextView.getText().toString());
        }
        MainActivity.writeNotes();
        MainActivity.arrayAdapter.notifyDataSetChanged();
        super.onDestroy();
    }
}