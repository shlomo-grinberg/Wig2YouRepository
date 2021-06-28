package com.example.wig2you.ui.editUserAccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;

import java.util.List;

public class EditUserAccountViewModel extends ViewModel {

    private LiveData<List<User>> userList;

    public EditUserAccountViewModel() {
        userList = Model.instance.getAllUsers();
    }

    public LiveData<List<User>> getData() {
        return userList;
    }

}