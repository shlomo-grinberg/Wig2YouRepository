package com.example.wig2you.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wig2you.MyApplication;


@Database(entities = {User.class,Wig.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WigDao wigDao();
}

public class AppLocalDB{
    final static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbWig2You.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

