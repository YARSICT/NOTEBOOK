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
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aueui.note.activities.FloatMenuActivity;
import com.aueui.note.utils.MenuReceiver;
import com.aueui.note.write.notes;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ConstraintLayout constraintLayout;
    private List<Notes> Noteslist = new ArrayList();
    private LinearLayout layout;
    private RecyclerView rv;
    private NotesAdapter adapter;
    private Toolbar toolbar;
    private Button btn_menu;
    BroadcastReceiver br;


    public class Notes {
        public String title;
        public String context;
        public String date;

        Notes(String title, String context, String date) {
            this.title = title;
            this.context = context;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public String getContext() {
            return context;
        }

        public String getDate() {
            return date;
        }
    }

    public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
        private List<Notes> mNoteslist;

        class ViewHolder extends RecyclerView.ViewHolder {
            View Notesview;
            TextView NotesContext;
            TextView NotesTitle;
            TextView NotesTime;

            ViewHolder(View view) {
                super(view);
                Notesview = view;
                NotesContext = view.findViewById(R.id.notes_context);
                NotesTitle = view.findViewById(R.id.notes_title);
                NotesTime = view.findViewById(R.id.notes_time);

            }
        }

        NotesAdapter(List<Notes> Noteslist) {
            mNoteslist = Noteslist;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
            String list_ui = sharedPreferences.getString("list_ui", "list");
            final ViewHolder holder;
            View view;
            if (list_ui.equals("list")) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item_style, viewGroup, false);
                if (isTheme().equals("pure_white") || isTheme().equals("")) {
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item_style_white_normal, viewGroup, false);
                }
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item_style2, viewGroup, false);
                if (isTheme().equals("pure_white") || isTheme().equals("")) {
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item_style_white_staggered, viewGroup, false);
                }
            }
            holder = new ViewHolder(view);
            holder.Notesview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Notes Notes = mNoteslist.get(position);
                    Intent intent = new Intent(MainActivity.this, Reader.class);
                    intent.putExtra("title", Notes.getTitle());
                    intent.putExtra("context", Notes.getContext());
                    intent.putExtra("date", Notes.getDate());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade, R.anim.fade_exit);
                }
            });
            holder.Notesview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = holder.getAdapterPosition();
                    final Notes Notes = mNoteslist.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("删除");
                    builder.setMessage("确认删除这条记事吗");
                    final String[] choices = {"复制", "删除", "分享"};
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (choices[which].equals("删除")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("删除");
                                builder.setMessage("确认删除这条记事吗");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notifyItemRemoved(position);
                                        LitePal.deleteAll(notes.class, "notes_context = ? and notes_title = ?", Notes.getContext(), Notes.getTitle());
                                        recreate();
                                    }

                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                            }
                        }
                    });


                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notifyItemRemoved(position);
                            LitePal.deleteAll(notes.class, "notes_context = ? and notes_title = ?", Notes.getContext(), Notes.getTitle());
                            recreate();
                        }

                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return true;
                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Notes Notes = mNoteslist.get(i);
            viewHolder.NotesContext.setText(Notes.getContext());
            viewHolder.NotesTitle.setText(Notes.getTitle());
            viewHolder.NotesTime.setText(Notes.getDate());

        }

        @Override
        public int getItemCount() {
            return mNoteslist.size();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentTheme", CurrentTheme);
    }
    public MainActivity(){

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Log.i(TAG, isTheme());
        toolbarcolor = R.color.pure_white;
        getWindow().setStatusBarColor(getResources().getColor(R.color.pure_white));

        if (isTheme().equals("blue")) {
            toolbarcolor = R.color.blue;
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("red")) {
            toolbarcolor = R.color.red;
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("orange")) {
            toolbarcolor = R.color.orange;
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("green")) {
            toolbarcolor = R.color.green;
            getWindow().setStatusBarColor(getResources().getColor(R.color.green));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("purple")) {
            toolbarcolor = R.color.purple;
            getWindow().setStatusBarColor(getResources().getColor(R.color.purple));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("night")) {
            toolbarcolor = R.color.night;
            getWindow().setStatusBarColor(getResources().getColor(R.color.night));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("pure_white")) {
            toolbarcolor = R.color.pure_white;
            getWindow().setStatusBarColor(getResources().getColor(R.color.pure_white));

        }
        if (isTheme().equals("pure_blue")) {
            toolbarcolor = R.color.blue;
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (isTheme().equals("pure_red")) {
            toolbarcolor = R.color.red;
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        initView();
        initNotes();
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                editor.putString("where", "MainActivity");
                editor.apply();
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade, R.anim.fade_exit);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String list_ui = sharedPreferences.getString("list_ui", "list");
        if (list_ui.equals("list")) {
            LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
            rv.setLayoutManager(LayoutManager);
        } else {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv.setLayoutManager(layoutManager);
        }
        Collections.reverse(Noteslist);
        adapter = new NotesAdapter(Noteslist);
        rv.setAdapter(adapter);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        constraintLayout.setBackgroundResource(R.color.pure_white);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.color_s);
        fab.setBackgroundTintList(csl);
        toolbar.setNavigationIcon(null);
        toolbar.setTitleMarginStart(100);

        if (isTheme().equals("night")) {
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setTitle("正常模式");
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setIcon(R.drawable.day);
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        } else {
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setTitle("夜间模式");


        }
        if (isTheme().equals("blue")) {
            constraintLayout.setBackgroundResource(R.drawable.blue_gradient);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("red")) {
            constraintLayout.setBackgroundResource(R.drawable.red_gradient);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("orange")) {
            constraintLayout.setBackgroundResource(R.drawable.orange_gradient);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("green")) {
            constraintLayout.setBackgroundResource(R.drawable.green_gradient);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("purple")) {
            constraintLayout.setBackgroundResource(R.drawable.purple_gradient);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("pure_white")) {
            constraintLayout.setBackgroundResource(R.color.pure_white);
            toolbar.setTitleTextColor(getResources().getColor(R.color.black));
            resource = getBaseContext().getResources();
            csl = resource.getColorStateList(R.color.color_s);
            fab.setBackgroundTintList(csl);
            toolbar.setNavigationIcon(null);
            toolbar.setTitleMarginStart(100);
        }
        if (isTheme().equals("pure_blue")) {
            constraintLayout.setBackgroundResource(R.color.blue);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        if (isTheme().equals("pure_red")) {
            constraintLayout.setBackgroundResource(R.color.red);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }


    }

    private void initNotes() {
        List<notes> notesList = LitePal.findAll(notes.class);
        for (notes notes : notesList) {
            Notes note = new Notes(notes.getNotes_title(), notes.getNotes_context(), notes.getDate());
            Noteslist.add(note);
        }
        if(notesList.size()!=0){
            Button logo=findViewById(R.id.logo);
            logo.setVisibility(View.GONE);
        }
    }

    private void initView() {
        constraintLayout = findViewById(R.id.main_constrain);
        toolbar = findViewById(R.id.about_toolbar);
        rv = findViewById(R.id.notes_items);
        btn_menu=findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener((view)->startActivity(new Intent(MainActivity.this, FloatMenuActivity.class).putExtra("STAT",1)));
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(toolbarcolor));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        constraintLayout.startAnimation(animation);
        br=new MenuReceiver();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("switchNightMode");
        this.registerReceiver(br,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        Boolean isChecked = sharedPreferences.getBoolean("isNight", false);
        if (id == R.id.nav_day_night_switch) {

            if (!isChecked) {

                SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                editor.putString("theme_items", "night");
                editor.putString("theme_items_now", isTheme());
                editor.putBoolean("isNight", true);
                editor.apply();
                Toast.makeText(MainActivity.this, "夜间模式", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferencess = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                String theme_now_resume = sharedPreferencess.getString("theme_items_now", "");
                SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                editor.putString("theme_items", theme_now_resume);
                editor.putBoolean("isNight", false);
                editor.apply();
                Toast.makeText(MainActivity.this, "正常模式", Toast.LENGTH_SHORT).show();
            }
            finish();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.fade, 0);
        }
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}

