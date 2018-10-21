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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aueui.note.write.notes;
import com.aueui.note.write.read;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ConstraintLayout constraintLayout;
    private List<Notes> Noteslist = new ArrayList();

    public class Notes {
        public String title;
        public String context;

        public Notes(String title, String context) {
            this.title = title;
            this.context = context;


        }

        public String getTitle() {
            return title;
        }

        public String getContext() {
            return context;
        }


    }

    public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
        private List<Notes> mNoteslist;

        class ViewHolder extends RecyclerView.ViewHolder {
            View Notesview;
            TextView NotesContext;
            TextView NotesTitle;

            public ViewHolder(View view) {
                super(view);
                Notesview = view;
                NotesContext = (TextView) view.findViewById(R.id.notes_context);
                NotesTitle = (TextView) view.findViewById(R.id.notes_title);

            }
        }

        public NotesAdapter(List<Notes> Noteslist) {
            mNoteslist = Noteslist;
        }


        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item_style, viewGroup, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.Notesview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Notes Notes = mNoteslist.get(position);
                    Intent intent=new Intent(MainActivity.this,read.class);
                    intent.putExtra("title",Notes.getTitle());
                    intent.putExtra("context",Notes.getContext());
                    startActivity(intent);
                }
            });
        /*    holder.NotesContext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Notes Notes = mNoteslist.get(position);
                    Toast.makeText(view.getContext(), Notes.getContext(), Toast.LENGTH_LONG).show();
                }
            });*/

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Notes Notes = mNoteslist.get(i);
            viewHolder.NotesContext.setText(Notes.getContext());
            viewHolder.NotesTitle.setText(Notes.getTitle());

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, isTheme());
        if (isTheme().equals("blue")) {
            toolbarcolor = R.color.blue;
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
        }
        if (isTheme().equals("red")) {
            toolbarcolor = R.color.red;
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }
        if (isTheme().equals("orange")) {
            toolbarcolor = R.color.orange;
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange));
        }
        if (isTheme().equals("green")) {
            toolbarcolor = R.color.green;
            getWindow().setStatusBarColor(getResources().getColor(R.color.green));
        }
        if (isTheme().equals("purple")) {
            toolbarcolor = R.color.purple;
            getWindow().setStatusBarColor(getResources().getColor(R.color.purple));
        }
        if (isTheme().equals("night")) {
            toolbarcolor = R.color.night;
            getWindow().setStatusBarColor(getResources().getColor(R.color.night));

        }
        setContentView(R.layout.activity_main);
        Log.i(TAG, Theme_all + "");
        constraintLayout = (ConstraintLayout) findViewById(R.id.main_constrain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(toolbarcolor));
        initNotes();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, editor.class);
                startActivity(intent);
                finish();
            }
        });
        RecyclerView rv = (RecyclerView) findViewById(R.id.notes_items);
        // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        //  rv.setLayoutManager(layoutManager);
        // LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(LayoutManager);
        NotesAdapter adapter = new NotesAdapter(Noteslist);
        rv.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (isTheme().equals("night")) {
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setTitle("正常模式");
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setIcon(R.drawable.day);
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
        } else {
            navigationView.getMenu().findItem(R.id.nav_day_night_switch).setTitle("夜间模式");

        }

    }

    private void initNotes() {
        List<notes> notesList = LitePal.findAll(notes.class);
        for (notes notes : notesList) {
            Notes note = new Notes(notes.getNotes_title(), notes.getNotes_context());
            Noteslist.add(note);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        SharedPreferences sharedPreferences = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
        Boolean isChecked = sharedPreferences.getBoolean("isNight", false);
        if (id == R.id.nav_day_night_switch) {

            if (isChecked == false) {

                SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                editor.putString("theme_items", "night");
                editor.putString("theme_items_now", isTheme());
                editor.putBoolean("isNight", true);
                editor.apply();
                Toast.makeText(MainActivity.this, "夜间模式", Toast.LENGTH_SHORT).show();
                recreate();
            } else {
                SharedPreferences sharedPreferencess = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE);
                String theme_now_resume = sharedPreferencess.getString("theme_items_now", "");
                SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                editor.putString("theme_items", theme_now_resume);
                editor.putBoolean("isNight", false);
                editor.apply();
                Toast.makeText(MainActivity.this, "正常模式", Toast.LENGTH_SHORT).show();
                recreate();
            }
        }

        if (id == R.id.nav_license) {
            Intent intent = new Intent(MainActivity.this, license.class);
            startActivity(intent);
        } else if (id == R.id.nav_source) {
            Intent intent = new Intent(MainActivity.this, source.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, settings.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
