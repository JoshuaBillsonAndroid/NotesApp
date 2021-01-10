package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();

    ListView notesListView;

    static ArrayAdapter<String> arrayAdapter;

    static SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote) {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            intent.putExtra("note", -1);
            startActivity(intent);
            return true;
        } else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesListView = findViewById(R.id.notesListView);
        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        /* Set Up List View Adapter */
        readNotes();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        notesListView.setAdapter(arrayAdapter);

        /* Handle Click Events On List Items (Edit Note) */
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            intent.putExtra("note", position);
            startActivity(intent);
        });

        /* Handle Long Click Events On List Items (Delete Note) */
        notesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Are You Sure?")
                    .setMessage("Do you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notes.remove(position);
                        arrayAdapter.notifyDataSetChanged();})
                    .setNegativeButton("No", null)
                    .show();
            return false;
        });
    }

    /* Write Notes To Shared Preferences */
    static void writeNotes() {
        try {
            String serializedNotes = ObjectSerializer.serialize(notes);
            sharedPreferences.edit().putString("notes", serializedNotes).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Read Notes From Shared Preferences */
    static void readNotes() {
        try {
            String serializedNotes = sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<String>()));
            notes = (ArrayList<String>) ObjectSerializer.deserialize(serializedNotes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}