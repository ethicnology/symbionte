package com.ethicnology.symbionte.Model;

public class Todo {

    private String title;
    private String description;
    private String id;
    private String category;


    public Todo() {

    }

    public Todo(String id, String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.category = category;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
