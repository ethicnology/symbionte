package com.ethicnology.symbionte.Model;

import java.util.ArrayList;

public class Bill {
    private String name;
    private String created_by;
    private String date;
    private int amount;
    private ArrayList<String> members;
    private ArrayList<Refund> refunds;
    private String id;

    public Bill(String name, String created_by, String date, int amount, ArrayList<String> members) {
        this.name = name;
        this.created_by = created_by;
        this.date = date;
        this.amount = amount;
        this.members = members;
    }


    public Bill(String name, String created_by, String date, int amount, ArrayList<String> members, ArrayList<Refund> refunds) {
        this.name = name;
        this.created_by = created_by;
        this.date = date;
        this.amount = amount;
        this.members = members;
        this.refunds = refunds;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    public void addMember(String member) {
        this.members.add(member);
    }
    public ArrayList<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(ArrayList<Refund> refunds) {
        this.refunds = refunds;
    }
    public void addRefund(Refund refund) {
        this.refunds.add(refund);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
