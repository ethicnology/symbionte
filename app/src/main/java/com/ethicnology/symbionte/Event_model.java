package com.ethicnology.symbionte;

public class Event_model {
    private String date;
    private String name;

    public Event_model(){
    }

    public Event_model(String date, String name) {
        this.date = date;
        this.name = name;
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


}
