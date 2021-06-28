package com.example.wig2you.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

interface JsonWigsModel{
    Map<String,Object> toJson();
}

@Entity
public class Wig implements JsonWigsModel{
    @PrimaryKey
    @NonNull
    public String id;
    public String owner;
    public String length;
    public String style;
    public String variety;
    public Double price;
    public String maker;
    public String purchaseDate;
    public String howToUse;
    public String kosher;
    public String image;
//    public String contactPhoneNumber;
    public boolean isAvailable;

    final static String ID = "id";
    final static String OWNER = "owner";
    final static String LENGTH = "length";
    final static String STYLE = "style";
    final static String VARIETY = "variety";
    final static String PRICE = "price";
    final static String MAKER = "maker";
    final static String PURCHASE_DATE = "purchaseDate";
    final static String HOW_TO_USE = "howToUse";
    final static String KOSHER = "kosher";
    final static String IMAGE = "image";
//    final static String CONTACT_PHONE_NUMBER = "contactPhoneNumber";
    final static String IS_AVAILABLE = "isAvailable";

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(OWNER, owner);
        json.put(LENGTH, length);
        json.put(STYLE, style);
        json.put(VARIETY, variety);
        json.put(PRICE, price);
        json.put(MAKER, maker);
        json.put(PURCHASE_DATE, purchaseDate);
        json.put(HOW_TO_USE, howToUse);
        json.put(KOSHER, kosher);
        json.put(IMAGE, image);
//        json.put(CONTACT_PHONE_NUMBER, contactPhoneNumber);
        json.put(IS_AVAILABLE,isAvailable);
        return json;
    }

    static public Wig create(Map<String,Object> json) {
        Wig wig = new Wig();
        wig.id = (String)json.get(ID);
        wig.owner = (String)json.get(OWNER);
        wig.length = (String)json.get(LENGTH);
        wig.style = (String)json.get(STYLE);
        wig.variety = (String)json.get(VARIETY);
        wig.price = (Double) json.get(PRICE);
        wig.maker = (String)json.get(MAKER);
        wig.purchaseDate = (String)json.get(PURCHASE_DATE);
        wig.howToUse = (String)json.get(HOW_TO_USE);
        wig.kosher = (String)json.get(KOSHER);
        wig.image = (String)json.get(IMAGE);
//        wig.contactPhoneNumber = (String)json.get(CONTACT_PHONE_NUMBER);
        wig.isAvailable = (boolean)json.get(IS_AVAILABLE);

        return wig;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    public String getKosher() {
        return kosher;
    }

    public void setKosher(String kosher) {
        this.kosher = kosher;
    }

//    public String getContactPhoneNumber() {
//        return contactPhoneNumber;
//    }
//
//    public void setContactPhoneNumber(String contactPhoneNumber) {
//        this.contactPhoneNumber = contactPhoneNumber;
//    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
