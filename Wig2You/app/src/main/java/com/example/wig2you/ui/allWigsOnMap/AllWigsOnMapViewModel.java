package com.example.wig2you.ui.allWigsOnMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AllWigsOnMapViewModel extends ViewModel {

    Map<String, User> usersDic;
    private LiveData<List<User>> usersList;
    private LiveData<List<Wig>> wigsList;
    public AllWigsOnMapViewModel() {
        usersDic = new Hashtable<>();
        wigsList = Model.instance.getAllWigs();
        usersList = Model.instance.getAllUsers();
        for (Wig w:wigsList.getValue()) {
            if(w.getOwner()!=null){
                boolean isExsits = usersDic.containsKey(w.getOwner());
                if(!isExsits){
                    User u = new User();
                    for (User user:usersList.getValue()) {
                        if(user.getId().equals(w.owner)) u = user;
                    }
                    usersDic.put(u.getId(),u);
                }
            }
        }
    }

    public Collection<User> GetDicUsers(){
        return usersDic.values();
    }
    public User getUserByLocation(Double latitude, Double longitude){
        User user = new User();
        for (User u:usersDic.values()) {
            if(u.getLatitude()== latitude && u.getLongitude()==longitude){
                user = u;
                break;
            }
        }
        return user;
    }


}