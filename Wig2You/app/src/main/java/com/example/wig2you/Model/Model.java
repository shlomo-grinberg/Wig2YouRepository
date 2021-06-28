package com.example.wig2you.Model;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class Model {

    final public static  Model instance = new Model();
    User user;

    public enum LoadingState{
        loaded,
        loading,
        error
    }
    public MutableLiveData<LoadingState> loadingState =
            new MutableLiveData<LoadingState>(LoadingState.loaded);

    private Model(){ }

    public interface GetDataListener{
        void onComplete(List<Object> data);
    }

    public interface OnCompleteListener{
        void onComplete();
    }
    public interface OnCompleteLogInListener{
        void onComplete(String validation);
    }

    //users
    MutableLiveData<List<User>> allUsers = new MutableLiveData<List<User>>(new LinkedList<User>());

    public LiveData<List<User>> getAllUsers() {
        loadingState.setValue(LoadingState.loading);
        ModelFirebase.getAllUsers((users)->{
            allUsers.setValue(users);
            loadingState.setValue(LoadingState.loaded);
        });
        return allUsers;
    }

    public void saveUser(User user, OnCompleteListener listener) {
        loadingState.setValue(LoadingState.loading);
        ModelFirebase.saveUser(user,()->{
            listener.onComplete();
        });
    }

    //wigs
    MutableLiveData<List<Wig>> allWigs = new MutableLiveData<List<Wig>>(new LinkedList<Wig>());

    public LiveData<List<Wig>> getAllWigs() {
        loadingState.setValue(LoadingState.loading);
        ModelFirebase.getAllWigs((wigs)->{
            allWigs.setValue(wigs);
            loadingState.setValue(LoadingState.loaded);
        });
        return allWigs;
    }

    public void saveWig(Wig wig, OnCompleteListener listener) {
        loadingState.setValue(LoadingState.loading);
        ModelFirebase.saveWig(wig,()->{
            getAllWigs();
            listener.onComplete();
        });
    }

    //image
    public interface  UpLoadImageListener{
        void onComplete(String url);
    }

    public static void uploadImage(Bitmap imageBmp, String userID,  final UpLoadImageListener listener) {
        ModelFirebase.uploadImage(imageBmp,userID,listener);
    }

    public static void isLoggedIn(OnCompleteLogInListener listener) {
        ModelFirebase.isLoggedIn((val) -> listener.onComplete(val));
    }



    //session
    public void logOut(){
        ModelFirebase.signOut();
    }
    public void login(String email, String password, OnCompleteLogInListener listener) {
        ModelFirebase.login(email, password, (val) -> listener.onComplete(val));
    }





    public User getUser() {
        return user;
    }

    public void setUser(User user, OnCompleteLogInListener listener) {
        this.user = user;
        listener.onComplete("Success");
    }
}
