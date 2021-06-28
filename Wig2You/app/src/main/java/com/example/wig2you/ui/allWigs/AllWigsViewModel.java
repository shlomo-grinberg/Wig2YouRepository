package com.example.wig2you.ui.allWigs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;

import java.util.List;

public class AllWigsViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;

    public AllWigsViewModel() {
        wigList = Model.instance.getAllWigs();
    }

    public LiveData<List<Wig>> getData() {
        return wigList;
    }



}