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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

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

import ren.qinc.edit.PerformEdit;


public class Editor extends BaseActivity {

    private String note_text, note_titles, title, context;
    private EditText editText, editText1;
    private static final String save_success = "保存成功", edit_sucess = "修改成功";
    private PerformEdit performEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (isTheme()){
            case "red":
                toolbarcolor = R.color.red;
                getWindow().setStatusBarColor(getResources().getColor(R.color.red));
                break;
            case "blue":
                toolbarcolor = R.color.blue;
                getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
                break;
            case "green":
                toolbarcolor = R.color.green;
                getWindow().setStatusBarColor(getResources().getColor(R.color.green));
                break;
            case "orange":
                toolbarcolor = R.color.orange;
                getWindow().setStatusBarColor(getResources().getColor(R.color.orange));
                break;
            case "purple":
                toolbarcolor = R.color.purple;
                getWindow().setStatusBarColor(getResources().getColor(R.color.purple));
                break;
            case "night":
                toolbarcolor = R.color.night;
                getWindow().setStatusBarColor(getResources().getColor(R.color.night));
                break;
        }
        setContentView(R.layout.editor);
        LitePal.getDatabase();
        initView();
        initToolbar();
        initEditor();
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

    }

    private void initView() {
        editText = findViewById(R.id.note_context);
        editText1 = findViewById(R.id.note_title);
        final FloatingActionButton actionButton = findViewById(R.id.editor_reset);
        FloatingActionButton actionButton1 = findViewById(R.id.editor_fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Editor.this);
                builder.setTitle("清空所有内容");
                builder.setMessage("确认删除所有内容吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText("");
                        editText1.setText("");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
                Boolean isexpand = sharedPreferences.getBoolean("fab_expand", true);
                if (!isexpand) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                    editor.putBoolean("fab_expand", true);
                    editor.apply();
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);
                    actionButton.startAnimation(mShowAction);

                    actionButton.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                    editor.putBoolean("fab_expand", false);
                    editor.apply();
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    mShowAction.setDuration(500);
                    actionButton.startAnimation(mShowAction);
                    actionButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar editor_toolbar = findViewById(R.id.editor_toolbar);
        editor_toolbar.inflateMenu(R.menu.editor_toolbar);
        editor_toolbar.setBackgroundColor(getResources().getColor(toolbarcolor));
        editor_toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.back:
                        performEdit.undo();
                        break;
                    case R.id.toward:
                        performEdit.redo();
                        break;
                    case R.id.go:
                        EditText note_context = (EditText) findViewById(R.id.note_context);
                        EditText note_title = (EditText) findViewById(R.id.note_title);
                        Date date = new Date(System.currentTimeMillis());
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                        String date_string = simpleDateFormat.format(date);
                        notes notes = new notes();
                        note_text = editText.getText().toString();
                        note_titles = note_title.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
                        String where = sharedPreferences.getString("where", "");
                        switch (where) {
                            case "MainActivity":
                                notes.setNotes_title(note_titles);
                                notes.setNotes_context(note_text);
                                notes.setDate(date_string);
                                notes.save();
                                Toast.makeText(Editor.this, save_success, Toast.LENGTH_SHORT).show();
                                break;
                            case "read":
                                notes.setNotes_title(note_titles);
                                notes.setNotes_context(note_text);
                                notes.setDate(date_string);
                                notes.updateAll("notes_title = ? and notes_context = ?", title, context);
                                Toast.makeText(Editor.this, edit_sucess, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                notes.setNotes_title(note_titles);
                                notes.setNotes_context(note_text);
                                notes.setDate(date_string);
                                notes.save();
                                break;
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
                        break;
                }

                return true;
            }
        });
        editor_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initEditor() {
        EditText note_context = (EditText) findViewById(R.id.note_context);
        performEdit = new PerformEdit(note_context);

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


