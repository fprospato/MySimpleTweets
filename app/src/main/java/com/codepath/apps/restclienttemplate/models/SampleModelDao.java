package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SampleModelDao {

    // @Query annotation requires knowing SQL syntax
    // See http://www.sqltutorial.org/

    //identifier of the objects
    @Query("SELECT * FROM SampleModel WHERE id = :id")
    SampleModel byId(long id);

    //get last 300 entries
    @Query("SELECT * FROM SampleModel ORDER BY ID DESC LIMIT 300")
    List<SampleModel> recentItems();

    //inserting entries in the table. the onConflict just makes sure to replace reoccuring data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(SampleModel... sampleModels);
}
