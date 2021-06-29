package com.example.wig2you.ui.allWigs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.ui.allWigsOnMap.AllWigsOnMapViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

public class AllWigsViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;
    private List<Wig> usersWigsList;
    private static User userSeller = null;

    public static User getUserSeller() {
        return userSeller;
    }
    public static void setUserSeller(User userSeller) {
        AllWigsViewModel.userSeller = userSeller;
    }

    public AllWigsViewModel() {
        wigList = Model.instance.getAllWigs();
        usersWigsList = null;
    }
    public void initWigList(){
        wigList = Model.instance.getAllWigs();
    }
    public void initUsersWigsList(User user){
        usersWigsList = new LinkedList<>();
        for (Wig w:wigList.getValue()) {
            if(w.getOwner()!=null && w.getOwner().equals(user.getId())){
                usersWigsList.add(w);
            }
        }
    }
    public List<Wig> getData() {
        if(usersWigsList!=null){
            return usersWigsList;
        }
        else {
            return wigList.getValue();
        }
    }
    public int getPosition(int pos){
        int currentPosition = pos;
        if(usersWigsList!=null){
            String wigID = usersWigsList.get(pos).getId();
            for (int i=0;i<wigList.getValue().size();i++){
                if(wigList.getValue().get(i).getId().equals(wigID)){
                    currentPosition = i;
                    break;
                }
            }
        }
        return currentPosition;
    }

}