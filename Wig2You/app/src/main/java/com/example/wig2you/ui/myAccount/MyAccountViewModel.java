package com.example.wig2you.ui.myAccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;

import java.util.LinkedList;
import java.util.List;

public class MyAccountViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;

    public MyAccountViewModel() {
        wigList = Model.instance.getAllWigs();
    }

    public LiveData<List<Wig>> getData() {
        return wigList;
    }

    public List<Wig> getFilterData() {
        List<Wig> newWigList = new LinkedList<>();
        if(wigList.getValue()!=null){
            for (Wig w : wigList.getValue()) {
                if(w.getOwner()!=null && w.getOwner().equals(Model.instance.getUser().id)){
                    newWigList.add(w);
                }
            }
        }
        return newWigList;
    }

    public int getPosition(int pos){
        int currentPosition = pos;
            String wigID = getFilterData().get(pos).getId();
            for (int i=0;i<wigList.getValue().size();i++){
                if(wigList.getValue().get(i).getId().equals(wigID)){
                    currentPosition = i;
                    break;
                }
            }

        return currentPosition;
    }

    public void refresh() {
        Model.instance.getAllWigs();
    }
}