package com.example.chetana.kitchenmantra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chetana.kitchenmantra.Adapters.IngredientsAdapter;
import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.Database.DatabaseHandler;
import com.example.chetana.kitchenmantra.Utilities.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TooManyListenersException;

public class AddIngredients extends AppCompatActivity {
    private Context parent;
    AutoCompleteTextView edtingrddesc, spiningrdqty, spiningrdunit;
    Button btnsavingrds;
    ImageButton btnadd;
    ListView listingredients;

    ArrayList<String> spin_units;
    String[] units;
    String[] ingredients_list;

    ArrayList<String> spin_qty;
    String[] qty;

    Bean_RecentAddedRecps bean_addingred;
    IngredientsAdapter ingredientsAdapter;

    DatabaseHandler db;
    SQLiteDatabase sqldb;
    Utility ut;

    String ingrdqty, ingrdunit, ingrddesc, ingrdmasterID;
    ArrayList<Bean_RecentAddedRecps> ingrdList;
    String RECPHDRID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        getSupportActionBar().setTitle(" Add Ingredients ");

        init();

        setValuToSpinner();

        GetIngredientsList();

        setListener();
    }

    @SuppressLint("WrongViewCast")
    public void init(){
        parent = AddIngredients.this;

        edtingrddesc = (AutoCompleteTextView) findViewById(R.id.edtingrddesc);
        spiningrdqty = (AutoCompleteTextView)findViewById(R.id.spiningrdqty);
        spiningrdunit = (AutoCompleteTextView)findViewById(R.id.spiningrdunit);
        btnadd = (ImageButton)findViewById(R.id.btnadd);
        btnsavingrds = (Button)findViewById(R.id.btnsavingrds);
        listingredients = (ListView)findViewById(R.id.listingredients);

        Intent intent = getIntent();
        RECPHDRID = intent.getStringExtra("ReceipeHeaderId");

        spin_units = new ArrayList<String>();
        units = new String[]{"Kg","Gm","Ltr","No","TblSpn"};

        spin_qty = new ArrayList<String>();
        qty = new String[]{"0.5","1","1.5","2","2.5","3","3.5","4","4.5","5","5.5","6","6.5","7","7.5","8","8.5","9","9.5","10"};

        ingredients_list = new String[]{"Spices Mixture (Garam Masala)","Cumin Powder (Jeera Masala)","Pepper Powder (Kali Mirch Masala)","Turmeric Powder",
        "Sambar Powder","Red Chili Powder","Dried Mango Powder (Amchur)","Chaat Masala","Jaljeera Powder","Coriander Powder","Cumin Seeds","Peppercorns (Kali Mirch)",
        "Coriander Seeds","Fenugreek Seeds (Methi Dhana)","Fennel Seeds (Saunf)","Black Cumin Seeds (Shahi Jeera)","Poppy Seeds","Sesame Seeds",
        "Black Mustard Seeds","Yellow Mustard Seeds","Onion Seeds","Carom Seeds","Cloves","Cinnamon","Green Cardamom","Black Cardamom","Star Anise",
        "Bay leaf","Nutmeg (Jaiphal)","Mace","Saffron","Stone Flower","Dried Ginger","Pomegranate Seeds","Turmeric","Dried Pumpkin Seeds","Yellow split peas",
        "Yellow split peas (Turdal)","Split Bengal Gram (Chana Dal)","Green Gram","Split green gram","Black Gram","Red Kidney Beans (Rajma)","White Chickpeas (Kabuli Chana)",
        "Brown chickpeas (Kale Chana)","Green Chickpeas (Hara Chana)","Split Red Lentils (Masur Dal)","Dry Green Peas","All Purpose Flour","Wheat Flour","Sago",
        "Rice Flour","Barley","Pearl Millet","Ragi Flour","Maize Flour","Gram Flour","Corn Flour","Semolina","Puttu Flour","Idiyapam Flour",
        "Raw Rice","Boiled Rice","Basmati Rice","Samba Rice","Beaten Rice","Ground Rice","Puttu Rice","Puffed Rice","Brown Basmati Rice",
        "Brown Rice","Black Rice","Almonds","Cashewnuts","Raisin","Pistachio","Dry Dates","Black Dates","Groundnut","Walnut","Dried Fig",
        "Apricot","Dry Plums","Pine nut","Tutti Frutti","Pecans","Salt","Sugar","Rock Salt","Lemon Salt","Clarified Butter","Cooking Oil",
        "Butter","Cream","Cottage Cheese","Asofoetida","Tamarind","Jaggery","Palm Jaggery","Dry Red Chilies","Dried Fenugreek Leaves",
        "Soya Sauce","Tomato Chili Sauce","Vinegar","Fruit Salt","Noodles","Vermicelli","Essence","Baking Powder","Honey","Dates Honey","Curd",
        "Yogurt"};

        db = new DatabaseHandler(parent);
        sqldb = db.getWritableDatabase();
        ut = new Utility();
        bean_addingred = new Bean_RecentAddedRecps();
        ingrdList = new ArrayList<Bean_RecentAddedRecps>();

    }

    public void setValuToSpinner(){

        //value to autocompletetextview
        ArrayAdapter<String> adptr_ingrds = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, ingredients_list);
        edtingrddesc.setThreshold(1);
        edtingrddesc.setAdapter(adptr_ingrds);

        //value for qty
        for(int i=0;i<qty.length; i++){
            String category = qty[i];
            spin_qty.add(category);
        }

        ArrayAdapter<String> adapter_qty = new ArrayAdapter<String>(parent,
                android.R.layout.simple_spinner_item, spin_qty);
        spiningrdqty.setAdapter(adapter_qty);

        //value for units
        for(int i=0;i<units.length; i++){
            String category = units[i];
            spin_units.add(category);
        }

        ArrayAdapter<String> adapter_unit = new ArrayAdapter<String>(parent,
                android.R.layout.simple_spinner_dropdown_item, spin_units);
        spiningrdunit.setAdapter(adapter_unit);
    }

    public void setListener(){

        edtingrddesc.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                edtingrddesc.showDropDown();
                return false;
            }
        });

        spiningrdqty.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                spiningrdqty.showDropDown();
                return false;
            }
        });

        spiningrdunit.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                spiningrdunit.showDropDown();
                return false;
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RECPDTLID = GenerateRecpDetailId();
                ingrdqty = spiningrdqty.getText().toString();
                ingrdunit = spiningrdunit.getText().toString();

                //store in bean class
                bean_addingred = new Bean_RecentAddedRecps();
                bean_addingred.setRecpHeaderId(RECPHDRID);
                bean_addingred.setRecpDetailId(RECPDTLID);
                bean_addingred.setIngrd_desc(edtingrddesc.getText().toString());
                bean_addingred.setIngrd_qty(ingrdqty);
                bean_addingred.setIngrd_unit(ingrdunit);

                ingrdList.add(bean_addingred);

                ingredientsAdapter = new IngredientsAdapter(parent,ingrdList);
                listingredients.setAdapter(ingredientsAdapter);

                edtingrddesc.setText("");
                spiningrdqty.setText("");
                spiningrdunit.setText("");

            }
        });

        btnsavingrds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add all ingredients in detail table

                 if(db.AddReceipeDetail(ingrdList)){
                    Toast.makeText(parent,"Ingredients saved successfully",Toast.LENGTH_SHORT).show();
                    edtingrddesc.setText("");
                    spiningrdqty.setText("");
                    spiningrdunit.setText("");
                   // GetIngredientsList();

                    finish();

                }else {
                    Toast.makeText(parent,"Ingredients not saved",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String GenerateRecpDetailId(){
        int val;
        Date C_TIME = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        String str_recpdtlID = df.format(C_TIME);
        /*try {
            val =((Number) NumberFormat.getInstance().parse(str_annschhistid)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        String recpdtlid = "0011"+str_recpdtlID ;
        return recpdtlid;
    }

    public void GetIngredientsList(){
        ingrdList.clear();

        Cursor cingrd = sqldb.rawQuery("Select * from "+ ut.TABLE_RECP_DETAIL + " Where Recp_headerid = '"+ RECPHDRID + "'", null);
        if(cingrd.getCount() > 0){
            cingrd.moveToFirst();
            do{
              ingrdqty = cingrd.getString(cingrd.getColumnIndex("ingrd_itemqty"));
              ingrddesc = cingrd.getString(cingrd.getColumnIndex("ingrd_itemdesc"));
              ingrdunit = cingrd.getString(cingrd.getColumnIndex("ingrd_itemunit"));
             // ingrdmasterID = cingrd.getString(cingrd.getColumnIndex("ingrd_itemmasterid"));

                bean_addingred = new Bean_RecentAddedRecps();
               // bean_addingred.setIngr_itemasterid(ingrdmasterID);
                bean_addingred.setIngrd_desc(ingrddesc);
                bean_addingred.setIngrd_qty(ingrdqty);
                bean_addingred.setIngrd_unit(ingrdunit);

                ingrdList.add(bean_addingred);

            }while (cingrd.moveToNext());

            ingredientsAdapter = new IngredientsAdapter(this,ingrdList);
            listingredients.setAdapter(ingredientsAdapter);

        }else {

        }
    }
}
