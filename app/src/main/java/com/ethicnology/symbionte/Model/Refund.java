package com.ethicnology.symbionte.Model;

import java.util.ArrayList;

public class Refund {
    private String created_by;
    private String date;
    private String amount;
    private String id;

    public Refund(String created_by, String date, String amount) {
        this.created_by = created_by;
        this.date = date;
        this.amount = amount;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
