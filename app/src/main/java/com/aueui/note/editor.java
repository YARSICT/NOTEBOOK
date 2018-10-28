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
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aueui.note.write.notes;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class editor extends BaseActivity {

    private String note_text;
    private String note_titles;
    private EditText editText;
    private EditText editText1;
    private String title;
    private String context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editor);
        LitePal.getDatabase();
        Button note_commit = (Button) findViewById(R.id.note_commit);
        editText = (EditText) findViewById(R.id.note_context);
        editText1 = (EditText) findViewById(R.id.note_title);
        if (editText.getText().toString().equals("")) {

        }
        //  if (isread==true) {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        context = intent.getStringExtra("context");
        //  where=intent.getStringExtra("where");
        editText.setText(context);
        editText1.setText(title);

        // }
        note_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText note_context = (EditText) findViewById(R.id.note_context);
                EditText note_title = (EditText) findViewById(R.id.note_title);
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss

                String date_string = simpleDateFormat.format(date);
                //Toast.makeText(editor.this,date_string,Toast.LENGTH_LONG).show();
                notes notes = new notes();

                note_text = editText.getText().toString();
                note_titles = note_title.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
                String where = sharedPreferences.getString("where", "");
                if (where.equals("MainActivity")) {
                    notes.setNotes_title(note_titles);
                    notes.setNotes_context(note_text);
                    notes.setDate(date_string);
                    notes.save();
                    Toast.makeText(editor.this, "保存成功", Toast.LENGTH_SHORT).show();
                } else if (where.equals("read")) {
                    notes.setNotes_title(note_titles);
                    notes.setNotes_context(note_text);
                    notes.setDate(date_string);
                    notes.updateAll("notes_title = ? and notes_context = ?", title, context);
                    Toast.makeText(editor.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    notes.setNotes_title(note_titles);
                    notes.setNotes_context(note_text);
                    notes.setDate(date_string);
                    notes.save();
                }
                Intent intent = new Intent(editor.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(editor.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}


