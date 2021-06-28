package com.example.wig2you.ui.addWig;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddWigViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddWigViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add wig fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}