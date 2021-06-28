package com.example.wig2you.ui.wigDetailsOwn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;

import java.util.List;

public class WigDetailsOwnViewModel extends ViewModel {

    private LiveData<List<Wig>> wigList;

    public WigDetailsOwnViewModel() {
        wigList = Model.instance.getAllWigs();
    }

    public LiveData<List<Wig>> getData() {
        return wigList;
    }

}