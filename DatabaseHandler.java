package com.example.chetana.kitchenmantra.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chetana.kitchenmantra.Classes.BeanUser_LoginCredentials;
import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.Fragments.AllReceipesFragment;
import com.example.chetana.kitchenmantra.Utilities.Utility;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "KitchenMantra.db";
    private static int DATABASE_VERSION = 1;

    Utility ut;
    SQLiteDatabase sqlDB;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ut = new Utility();
        db.execSQL(ut.CREATE_TABLE_USER);
        db.execSQL(ut.CREATE_TABLE_RECEIPE);
        db.execSQL(ut.CREATE_TABLE_RECP_HEADER);
        db.execSQL(ut.CREATE_TABLE_RECP_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ ut.TABLE_USER);
        db.execSQL(" DROP TABLE IF EXISTS "+ ut.TABLE_RECEIPES);
        db.execSQL(" DROP TABLE IF EXISTS "+ ut.TABLE_RECP_HEADER);
        db.execSQL(" DROP TABLE IF EXISTS "+ ut.TABLE_RECP_DETAIL);

        onCreate(db);
    }

    public boolean AddUser(BeanUser_LoginCredentials blogin){
        sqlDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ut.COLUMN_USERNAME, blogin.getUsername());
        contentValues.put(ut.COLUMN_USERMOB, blogin.getUsermobile());
        contentValues.put(ut.COLUMN_PSW, blogin.getPsw());

        sqlDB.insert(ut.TABLE_USER,null,contentValues);

        return true;
    }

    public boolean IsUserPresent(){
        sqlDB = this.getReadableDatabase();

        Cursor c = sqlDB.rawQuery(" Select * from "+ut.TABLE_USER,null);

        if(c.getCount() > 0){

            c.moveToFirst();
            do{
                String user = c.getString(c.getColumnIndex(""+ut.COLUMN_USERNAME));
            }while (c.moveToNext());

            return true;
        }else {
            //no user
            return  false;
        }
    }

    public boolean AddReceipe(Bean_RecentAddedRecps bAddRecps){
        sqlDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Recp_masterid",bAddRecps.getRecpmasterId());
        contentValues.put("Recp_uploadedby",bAddRecps.getRecUploaderName());
        contentValues.put("Recp_title", bAddRecps.getRecpname());
        contentValues.put("Recp_addeddt",bAddRecps.getAddedDt());
        contentValues.put("Recp_category", bAddRecps.getCategory());
        contentValues.put("Recp_fvrt_cnt",bAddRecps.getFavcnt());
        contentValues.put("Recp_feedback", bAddRecps.getFeedback());
        contentValues.put("Recp_ingrd_desc",bAddRecps.getIngrd_desc());
        contentValues.put("Recp_ingrd_qty", bAddRecps.getIngrd_qty());
        contentValues.put("Recp_ingrd_unit",bAddRecps.getIngrd_unit());
        contentValues.put("Recp_prep_desc", bAddRecps.getPreparation_desc());
        contentValues.put("Recp_imgpath",bAddRecps.getRecpImg());
        contentValues.put("Recp_prep_durtn", bAddRecps.getRecpTime());

        sqlDB.insert(ut.TABLE_RECEIPES,null,contentValues);

        return true;
    }

    public boolean AddReceipeHeader(Bean_RecentAddedRecps bAddRecps){
        sqlDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Recp_headerid",bAddRecps.getRecpHeaderId());
        contentValues.put("Recp_title", bAddRecps.getRecpname());
        contentValues.put("Recp_category", bAddRecps.getCategory());
        contentValues.put("Recp_fvrtcnt", /*bAddRecps.getFavcnt()*/"4");
        contentValues.put("Recp_prep_desc", bAddRecps.getPreparation_desc());
        contentValues.put("Recp_uploadedby",bAddRecps.getRecUploaderName());
        contentValues.put("Recp_addeddt",bAddRecps.getAddedDt());
        contentValues.put("Recp_prep_durtn", bAddRecps.getRecpTime());
        contentValues.put("Recp_feedback", "It's an amazing receipe, Thanks for sharing it with us.");
        contentValues.put("Recp_imgpath",/*bAddRecps.getRecpImgDATA()*/bAddRecps.getRecpImg());

        long a = sqlDB.insert(ut.TABLE_RECP_HEADER,null,contentValues);
        Log.e("RecpHdrcnt",String.valueOf(a));

        return true;
    }

    public boolean AddReceipeDetail(ArrayList<Bean_RecentAddedRecps> bAddRecps){
        sqlDB = this.getWritableDatabase();
        ContentValues contentValues = null;

        for(int pos=0; pos < bAddRecps.size(); pos++){
            contentValues = new ContentValues();
            contentValues.put("Recp_headerid",bAddRecps.get(pos).getRecpHeaderId());
            contentValues.put("ingrd_itemdesc",bAddRecps.get(pos).getIngrd_desc());
            contentValues.put("ingrd_itemqty", bAddRecps.get(pos).getIngrd_qty());
            contentValues.put("ingrd_itemunit",bAddRecps.get(pos).getIngrd_unit());

            Long a =  sqlDB.insert(ut.TABLE_RECP_DETAIL,null,contentValues);
            Log.e("IngrdHDR",String.valueOf(a));
        }

        return true;
    }
}
