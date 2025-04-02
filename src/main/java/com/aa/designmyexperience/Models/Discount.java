package com.aa.designmyexperience.Models;

import java.time.LocalDate;

public class Discount {
    int id;
    String code;
    int owner;
    double percentage;
    LocalDate endDate;

    public Discount(){

    }

    public Discount(String code, int owner, double percentage, LocalDate endDate) {
        this.code = code;
        this.owner = owner;
        this.percentage = percentage;
        this.endDate = endDate;
    }

    public Discount(int id, String code, int owner, double percentage, LocalDate endDate) {
        this.id = id;
        this.code = code;
        this.owner = owner;
        this.percentage = percentage;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
