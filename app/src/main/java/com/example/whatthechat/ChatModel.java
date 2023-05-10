package com.example.whatthechat;

public class ChatModel {
    public String name;
    private String script;
    private String profile_image;
    private String date_time;

    public ChatModel(String name, String script, String profile_image, String date_time) {
        this.name = name;
        this.script = script;
        this.profile_image = profile_image;
        this.date_time = date_time;
    }

    public String getName() {
        return name;
    }

    public String getScript() {
        return script;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getDate_time() {
        return date_time;
    }

    ChatModel() {

    }
}
