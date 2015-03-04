package com.rj.tapnote.db;


public class Note {
    //private variables
    int _id;
    String _title;
    String _note;
    String _tag;
    String _date;

    // Empty constructor
    public Note() {

    }

    // constructor
    public Note(int id, String title, String note, String tag, String date) {
        this._id = id;
        this._title = title;
        this._note = note;
        this._tag = tag;
        this._date = date;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting ID
    public void setID(int id) {
        this._id = id;
    }

    // getting title
    public String getTitle() {
        return this._title;
    }

    // setting title
    public void setTitle(String title) {
        this._title = title;
    }

    // getting note
    public String getNote() {
        return this._note;
    }

    // setting note
    public void setNote(String note) {
        this._note = note;
    }
}
