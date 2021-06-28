package com.example.wig2you.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WigDao {
    @Query("select * from Wig")
    LiveData<List<Wig>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Wig... wigs);

    @Delete
    void delete(Wig wigs);
}
