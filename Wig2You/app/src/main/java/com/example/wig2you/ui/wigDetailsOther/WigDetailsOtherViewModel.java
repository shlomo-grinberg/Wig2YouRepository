package com.example.wig2you.ui.wigDetailsOther;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;

import java.util.List;
import java.util.function.Predicate;

public class WigDetailsOtherViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;
    private LiveData<List<User>> userList;

    public WigDetailsOtherViewModel() {
        wigList = Model.instance.getAllWigs();
        userList = Model.instance.getAllUsers();
    }

    public LiveData<List<Wig>> getWigsData() {
        return wigList;
    }
    public LiveData<List<User>> getUsersData() {
        return userList;
    }

    public String getPhone(String userID){
        User user = null;
        if(userList.getValue()!=null){
            for (User u:userList.getValue()) {
                if(u.getId().equals(userID)){
                    user = u;
                }
            }
            return user.getPhone();
        }
        else {
           return "";
        }

    }

}