package com.ethicnology.symbionte.Model;

import java.util.ArrayList;

public class Refund {
    private String name;
    private String created_by;
    private String date;
    private String amount;


    public Refund(String name, String created_by, String date, String amount) {
        this.name = name;
        this.created_by = created_by;
        this.date = date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
