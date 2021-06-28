package com.example.wig2you.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

interface JsonUsersModel{
    Map<String,Object> toJson();
}

@Entity
public class User implements JsonUsersModel{
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String address;
    public String phone;
    public String email;
    public String password;
    public String image;
    double latitude;
    double longitude;
    public boolean isAvailable;

    final static String ID = "id";
    final static String NAME = "name";
    final static String ADDRESS = "address";
    final static String PHONE = "phone";
    final static String EMAIL = "email";
    final static String PASSWORD = "password";
    final static String IMAGE = "image";
    final static String LATITUDE = "latitude";
    final static String LONGITUDE = "longitude";
    final static String IS_AVAILABLE = "isAvailable";


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(NAME, name);
        json.put(ADDRESS, address);
        json.put(PHONE, phone);
        json.put(EMAIL, email);
        json.put(PASSWORD, password);
        json.put(IMAGE, image);
        json.put(LATITUDE, latitude);
        json.put(LONGITUDE, longitude);
        json.put(IS_AVAILABLE,isAvailable);
        return json;
    }

    static public User create(Map<String,Object> json) {
        User user = new User();
        user.id = (String)json.get(ID);
        user.name = (String)json.get(NAME);
        user.address = (String)json.get(ADDRESS);
        user.phone = (String)json.get(PHONE);
        user.email = (String)json.get(EMAIL);
        user.password = (String)json.get(PASSWORD);
        user.image = (String)json.get(IMAGE);
        user.latitude = (double)json.get(LATITUDE);
        user.longitude = (double)json.get(LONGITUDE);
        user.isAvailable = (boolean)json.get(IS_AVAILABLE);

        return user;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
