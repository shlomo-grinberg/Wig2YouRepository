package com.example.wig2you.ui.allWigsOnMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.ui.allWigs.AllWigsViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AllWigsOnMapViewModel extends ViewModel {
    private Map<String, User> usersDic;
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
                    User u = null;
                    for (User user:usersList.getValue()) {
                        if(user.getId().equals(w.owner)) u = user;
                    }
                    if(u!=null) usersDic.put(u.getId(),u);
                }
            }
        }
    }

    public Collection<User> GetDicUsers(){
        return usersDic.values();
    }
    public void getUserByLocation(LatLng latLng){
        User user = new User();
        for (User u:usersDic.values()) {
            if(u.getLatitude()== latLng.latitude && u.getLongitude()==latLng.longitude){
                user = u;
                break;
            }
        }
        AllWigsViewModel.setUserSeller(user);
    }


}