package com.aueui.note.write;

import org.litepal.crud.LitePalSupport;;

public class notes extends LitePalSupport{
    private int id;
    private String notes_title;
    private String notes_context;

    public String getNotes_context() {
        return notes_context;
    }

    public void setNotes_context(String notes_context) {
        this.notes_context = notes_context;
    }

    public String getNotes_title() {

        return notes_title;
    }

    public void setNotes_title(String notes_title) {
        this.notes_title = notes_title;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
