package com.dsmini.Shirtify.Model;

public class Report {
    String id;
    int alacarte;
    int bento;
    int handroll;
    int beverage;
    double totalPrice;

    public Report(int alacarte, int bento, int handroll, int beverage,double totalPrice) {
        this.alacarte = alacarte;
        this.bento = bento;
        this.handroll = handroll;
        this.beverage = beverage;
        this.totalPrice=totalPrice;
    }

    public Report() {
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAlacarte() {
        return alacarte;
    }

    public void setAlacarte(int alacarte) {
        this.alacarte = alacarte;
    }

    public int getBento() {
        return bento;
    }

    public void setBento(int bento) {
        this.bento = bento;
    }

    public int getHandroll() {
        return handroll;
    }

    public void setHandroll(int handroll) {
        this.handroll = handroll;
    }

    public int getBeverage() {
        return beverage;
    }

    public void setBeverage(int beverage) {
        this.beverage = beverage;
    }
}
