/*
Copyright 2018 YARSICT

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
package com.aueui.note;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.aueui.note.write.notes;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;


public class editor extends BaseActivity {

    private String note_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editor);
        LitePal.getDatabase();
        Button note_commit = (Button) findViewById(R.id.note_commit);
        EditText editText = (EditText) findViewById(R.id.note_context);
        if (editText.getText().toString().equals("")) {

           /* List<notes> notesList = LitePal.findAll(notes.class);
            for (notes notes : notesList) {
                editText.setText(notes.getNotes_context());
            }*/
        }
        note_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.note_context);
                note_text = editText.getText().toString();
                notes notes = new notes();
                notes.setNotes_context(note_text);
                notes.save();
                finish();
            }
        });

    }
}
