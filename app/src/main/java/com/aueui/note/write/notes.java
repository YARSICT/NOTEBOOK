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
package com.aueui.note.write;

import org.litepal.crud.LitePalSupport;;import java.util.Date;

public class notes extends LitePalSupport {
    private String date, notes_title, notes_context;


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

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;

    }
}
