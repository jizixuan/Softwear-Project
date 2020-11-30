package com.example.myapplication.note.entity;

public class NoteItem {
    private String note;
    private String date;
    private String title;

    public NoteItem() {
    }

    public NoteItem(String note, String date,String title) {
        this.note = note;
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
