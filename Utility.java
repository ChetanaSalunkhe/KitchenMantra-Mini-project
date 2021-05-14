package com.example.chetana.kitchenmantra.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    /*---------------------------------------------------tables-------------------------------------------*/
    public static String COLUMN_ID = "id";
    public static String COLUMN_USERNAME = "username";
    public static String COLUMN_USERMOB = "usermobile";
    public static String COLUMN_PSW = "password";
    public static String TABLE_USER = "usertable";
    public static String TABLE_RECEIPES = "receipes";
    public static String TABLE_RECP_HEADER = "recpeheader";
    public static String TABLE_RECP_DETAIL = "recpedetail";

    /*-------------------------------------------Create table queries-------------------------------------------*/
    public static String CREATE_TABLE_USER =
            " CREATE TABLE usertable (id integer primary key, username TEXT, usermobile TEXT, password TEXT)";

    public static String CREATE_TABLE_RECEIPE =
            " CREATE TABLE receipes (Recp_headerid TEXT, Recp_uploadedby TEXT, Recp_addeddt TEXT, Recp_title TEXT," +
             "Recp_category TEXT, Recp_fvrt_cnt TEXT, Recp_prep_durtn TEXT, Recp_prep_desc TEXT, Recp_feedback TEXT," +
             "Recp_imgpath TEXT, Recp_ingrd_desc TEXT, Recp_ingrd_qty TEXT, Recp_ingrd_unit TEXT )";

    public static String CREATE_TABLE_RECP_HEADER =
            " CREATE TABLE recpeheader (Recp_headerid TEXT,Recp_title TEXT,Recp_category TEXT,Recp_fvrtcnt TEXT,Recp_prep_desc TEXT," +
                    "Recp_uploadedby TEXT,Recp_addeddt TEXT,Recp_prep_durtn TEXT,Recp_feedback TEXT,Recp_imgpath TEXT )";

    public static String CREATE_TABLE_RECP_DETAIL =
            " CREATE TABLE recpedetail (Recp_headerid TEXT, Recp_DetailID TEXT, ingrd_itemmasterid TEXT, ingrd_itemdesc TEXT, ingrd_itemqty TEXT, ingrd_itemunit TEXT )";

    /*----------------------------------------------------------------------------------------------------------*/
    public String GetTodaysConvertDate(){
        String convrtdDate = "";
        Date d = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        try {
            //Date date2 = dateFormat2.parse(d);
            convrtdDate = dateFormat1.format(d);
        }catch( Exception e){
            e.printStackTrace();
        }

        return convrtdDate;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
