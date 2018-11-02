/*
 *    Copyright (C)2018 YARSICT IT TEAM
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.aueui.note;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aueui.note.write.notes;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Editor extends BaseActivity {

    private String note_text, note_titles, title, context;
    private EditText editText, editText1;
    private Button note_commit;
    private static final String save_success="保存成功",edit_sucess="修改成功";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        LitePal.getDatabase();
        initView();
        SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
        String where = sharedPreferences.getString("where", "");
        if (where.equals("read")) {
            Intent intent = getIntent();
            title = intent.getStringExtra("title");
            context = intent.getStringExtra("context");
            editText.setText(context);
            editText1.setText(title);
        }
        if (where.equals("MainActivity")) {
            if (!TextUtils.isEmpty(load())) {
                editText.setText(load());
                editText.setSelection(load().length());
            }
            if (!TextUtils.isEmpty(load_title())) {
                editText1.setText(load_title());
                editText1.setSelection(load_title().length());
            }
        }

        note_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText note_context = (EditText) findViewById(R.id.note_context);
                EditText note_title = (EditText) findViewById(R.id.note_title);
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String date_string = simpleDateFormat.format(date);
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
                    Toast.makeText(Editor.this, save_success, Toast.LENGTH_SHORT).show();
                } else if (where.equals("read")) {
                    notes.setNotes_title(note_titles);
                    notes.setNotes_context(note_text);
                    notes.setDate(date_string);
                    notes.updateAll("notes_title = ? and notes_context = ?", title, context);
                    Toast.makeText(Editor.this, edit_sucess, Toast.LENGTH_SHORT).show();
                } else {
                    notes.setNotes_title(note_titles);
                    notes.setNotes_context(note_text);
                    notes.setDate(date_string);
                    notes.save();
                }
                SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                editor.putString("isCommit", "commit");
                editor.apply();
                FileOutputStream out = null;
                BufferedWriter writer = null;
                try {
                    out = openFileOutput("cache_data", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write("");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        {
                            if (writer != null) {
                                writer.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    out = openFileOutput("title_data", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write("");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        {
                            if (writer != null) {
                                writer.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(Editor.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

    }

    private void initView() {
        note_commit = (Button) findViewById(R.id.note_commit);
        editText = (EditText) findViewById(R.id.note_context);
        editText1 = (EditText) findViewById(R.id.note_title);
    }

    public void save(String input) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("cache_data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(input);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                {
                    if (writer != null) {
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void save_title(String titles) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("title_data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(titles);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                {
                    if (writer != null) {
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    protected void onDestroy() {
        super.onDestroy();
        String input = editText.getText().toString();
        String titles = editText1.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
        String isCommit = sharedPreferences.getString("isCommit", "");
        if (isCommit.equals("commit")) {
            SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
            editor.putString("isCommit", "uncommit");
            editor.apply();
        } else {
            save(input);
            save_title(titles);
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("cache_data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return content.toString();
            }
        }
    }

    public String load_title() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("title_data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return content.toString();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Editor.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}


