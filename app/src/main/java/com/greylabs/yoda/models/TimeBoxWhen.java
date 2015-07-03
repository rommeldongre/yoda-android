package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 7/3/2015.
 */
public class TimeBoxWhen {

    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long timeBoxId;
    private List<com.greylabs.yoda.enums.TimeBoxWhen> whenValues;
    private Database database;
    private Context context;

    /**********************************************************************************************/
    // Geters and Setters
    /**********************************************************************************************/
    public long getTimeBoxId() {
        return timeBoxId;
    }

    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
    }



    public List<com.greylabs.yoda.enums.TimeBoxWhen> getWhenValues() {
        return whenValues;
    }

    public void setWhenValues(List<com.greylabs.yoda.enums.TimeBoxWhen> whenValues) {
        this.whenValues = whenValues;
    }



    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBoxWhen(Context context,long timeBoxId){
        database=Database.getInstance(context);
        this.context=context;
        this.timeBoxId=timeBoxId;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public  List<com.greylabs.yoda.enums.TimeBoxWhen> get(){
        List<com.greylabs.yoda.enums.TimeBoxWhen> whenValues=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxWhen.timeBoxWhen+" " +
                " "+"where "+ TableTimeBoxWhen.id+" = "+this.timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            whenValues=new ArrayList();
            do{
                int value=c.getInt(c.getColumnIndex(TableTimeBoxWhen.when));
                whenValues.add(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(value));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return whenValues;
    }

    public long save(){
        delete();
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(com.greylabs.yoda.enums.TimeBoxWhen when:whenValues) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxWhen.when, com.greylabs.yoda.enums.TimeBoxWhen.getEnumToIntegerType(when));
            values.put(TableTimeBoxWhen.id, timeBoxId);
            rowId += db.insert(TableTimeBoxWhen.timeBoxWhen, null, values);
        }
        db.close();
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBoxWhen.timeBoxWhen,TableTimeBoxWhen.id + "=" + timeBoxId, null);
        db.close();
        return numOfRowAffected;
    }



    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
}
