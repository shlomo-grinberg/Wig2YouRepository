package com.example.wig2you.ui.wigDetailsOther;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;

import java.util.List;

public class WigDetailsOtherViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;

    public WigDetailsOtherViewModel() {
        wigList = Model.instance.getAllWigs();
    }

    public LiveData<List<Wig>> getData() {
        return wigList;
    }

}