package com.example.wig2you.Model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {

    final public static  Model instance = new Model();
    User user;
    ExecutorService executorService = Executors.newCachedThreadPool();

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
    LiveData<List<User>> allUsers =   AppLocalDB.db.userDao().getAll();

    public LiveData<List<User>> getAllUsers() {
        loadingState.setValue(LoadingState.loading);
        Long localLastUpdate = User.getLocalLastUpdateTime();
        ModelFirebase.getAllUsers(localLastUpdate,(users)->{
            executorService.execute(()->
            {
                Long lastUpdate = new Long(0);
                for (User user: users)
                {
                    if(!(user.isAvailable()))
                    {
                        AppLocalDB.db.userDao().delete(user);
                    }
                    else{
                        AppLocalDB.db.userDao().insertAll(user);
                    }
                    if(lastUpdate < user.lastUpdated)
                    {
                        lastUpdate = user.lastUpdated;
                    }
                }
                User.setLocalLastUpdateTime(lastUpdate);
                loadingState.postValue(LoadingState.loaded);
            });
        });

        return allUsers;
    }

    public void saveUser(User user, OnCompleteListener listener) {
        loadingState.setValue(LoadingState.loading);
        ModelFirebase.saveUser(user,()->{
            getAllUsers();
            listener.onComplete();
        });
    }

    //wigs
    LiveData<List<Wig>> allWigs =   AppLocalDB.db.wigDao().getAll();

    public LiveData<List<Wig>> getAllWigs() {
        Log.d("TAG","getAllWigs");
        loadingState.setValue(LoadingState.loading);
        Long localLastUpdate = Wig.getLocalLastUpdateTime();
        ModelFirebase.getAllWigs(localLastUpdate,(wigs)->{
            executorService.execute(()->
            {
                Long lastUpdate = new Long(0);
                for (Wig wig: wigs)
                {
                    Log.d("TAG",wig.isAvailable() +"");
                    Log.d("TAG",allWigs.getValue().size() +"");

                    if(!(wig.isAvailable()))
                    {
                        AppLocalDB.db.wigDao().delete(wig);
                    }
                    else{
                        AppLocalDB.db.wigDao().insertAll(wig);
                    }
                    if(lastUpdate < wig.lastUpdated)
                    {
                        lastUpdate = wig.lastUpdated;
                    }
                }
                Wig.setLocalLastUpdateTime(lastUpdate);
                loadingState.postValue(LoadingState.loaded);
            });
        });

        return allWigs;
    }

//    MutableLiveData<List<Wig>> allWigs = new MutableLiveData<List<Wig>>(new LinkedList<Wig>());
//
//    public LiveData<List<Wig>> getAllWigs() {
//        loadingState.setValue(LoadingState.loading);
//        ModelFirebase.getAllWigs((wigs)->{
//            allWigs.setValue(wigs);
//            loadingState.setValue(LoadingState.loaded);
//        });
//        return allWigs;
//    }

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
    public void setUser(User newUser){
        user = user;
    }
}
