package com.example.salon_fryzjerski_projekt;

public class BookingType {
    private final int id;
    private final String headText;
    private final String descText;
    private final String priceText;
    private final String timeText;

    public BookingType(int id, String headText, String descText, String priceText, String timeText) {
        this.id = id;
        this.headText = headText;
        this.descText = descText;
        this.priceText = priceText;
        this.timeText = timeText;
    }

    public int getId() {
        return id;
    }

    public String getHeadText() {
        return headText;
    }

    public String getDescText() {
        return descText;
    }

    public String getPriceText() {
        return priceText;
    }

    public String getTimeText() {
        return timeText;
    }
}
