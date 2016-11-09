package com.sunil.firebasedatabasetest.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.author;

/**
 * Created by sunil on 11/7/16.
 */

@IgnoreExtraProperties
public class NotesModel {

    public String uid;
    public String user;
    public String title;
    public String description;

    public NotesModel(){

    }

    public NotesModel(String user, String uid, String title, String description) {
        this.user = user;
        this.uid = uid;
        this.title = title;
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("user", user);
        result.put("title", title);
        result.put("description", description);
        return result;
    }
}
