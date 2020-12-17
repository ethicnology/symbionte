package com.ethicnology.symbionte.Model;

import java.util.ArrayList;

public class Event_model {
    private String date;
    private String name;
    private String time;
    private String id;
    private String created_by;
    private ArrayList<String> members_list = new ArrayList<>();

    public Event_model(){}

    public Event_model(String date, String name, String time, String created_by) {
        this.date = date;
        this.name = name;
        this.time = time;
        this.created_by = created_by;
    }
    public Event_model(String date, String name, String time, String created_by, ArrayList<String> members_list) {
        this.date = date;
        this.name = name;
        this.time = time;
        this.created_by = created_by;
        this.members_list = members_list;
    }
    public Event_model(String date, String name, String time, String created_by, ArrayList<String> members_list, String id) {
        this.date = date;
        this.name = name;
        this.time = time;
        this.created_by = created_by;
        this.members_list = members_list;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String desc) {
        this.name = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String t){
        this.time = t;
    }

    public String getCreated_by() {
        return this.created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public ArrayList<String> getMembers() {
        return members_list;
    }

    public void setMembers(ArrayList<String> members) {
        this.members_list = members;
    }

    public void addMember(String user_name){
        members_list.add(user_name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
