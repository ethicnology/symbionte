package com.ethicnology.symbionte;

public class DataManager {
    private static DataManager sInstance;


    private String category_selected = null;

    // private constructor to limit new instance creation
    private DataManager() {
        // may be empty
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }
    public String getCategory_selected() {
        return category_selected;
    }

    public void setCategory_selected(String category_selected) {
        this.category_selected = category_selected;
    }


}