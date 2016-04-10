package com.coltan.tapnote.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    //private variables
    private String _id;
    private String _title;
    private String _note;
    private String _tag;
    private String _date;
    private String _starred;

    // Empty constructor
    public Note() {
    }

    public Note(String id, String title, String note, String tag, String date) {
        this._id = id;
        this._title = title;
        this._note = note;
        this._tag = tag;
        this._date = date;
    }

    // getting ID
    public String getID() {
        return this._id;
    }

    // setting ID
    public void setID(String id) {
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

    // getting tag
    public String getTag() {
        return this._tag;
    }

    // setting tag
    public void setTag(String tag) {
        this._tag = tag;
    }

    // getting date
    public String getDate() {
        return this._date;
    }

    // setting date
    public void setDate(String date) {
        this._date = date;
    }

    // getting date
    public String getStarred() {
        return this._starred;
    }

    // setting date
    public void setStarred(String starred) {
        this._starred = starred;
    }


    protected Note(Parcel in) {
        this._id = in.readString();
        this._title = in.readString();
        this._note = in.readString();
        this._tag = in.readString();
        this._date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_title);
        dest.writeString(_note);
        dest.writeString(_tag);
        dest.writeString(_date);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
