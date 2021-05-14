package com.example.chetana.kitchenmantra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chetana.kitchenmantra.Adapters.HomeListAdapter;
import com.example.chetana.kitchenmantra.Adapters.IngredientsAdapter;
import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.Database.DatabaseHandler;
import com.example.chetana.kitchenmantra.Utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeScreen extends AppCompatActivity {
    private Context parent;
    LinearLayout layoutGoto, lay_imgvw;
    ListView list_recps;
    ImageView img_gotokitchen, img_slider;
    HomeListAdapter homeListAdapter;
    ArrayList<Bean_RecentAddedRecps> recentrec_plist;
    Bean_RecentAddedRecps receipesbeanclass;
    String RECPHDRID;

    public static final int[] imgthumbIds = {R.drawable.recp1, R.drawable.recpstart,R.drawable.recp33,R.drawable.recp41,R.drawable.recp44,
            R.drawable.recp9, R.drawable.recp8,R.drawable.recp7,R.drawable.recp6, R.drawable.spices4, R.drawable.recp71,R.drawable.recp5,
            R.drawable.recp1,R.drawable.recppreparation, R.drawable.recp51};
    int imgcnt = 0;

    DatabaseHandler db;
    SQLiteDatabase sqldb;
    Utility ut;

    String recpname, recphdrid, recpcategory, recpaddedDt, recpfavcnt, recpfeedback, recpuploadby, recpduration;
    int recpimg;
    String IMG;
    byte[] imgrecp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportActionBar().setTitle("Home");

        init();

        imgcnt = 0;
        imgslider.start();

       // getDummyList();

       //AddDefaultReceipes();
        if(checkRecpHeader() == 0){
            AddDefaultReceipes();
        }else {

        }

        getDataFromDatabase();

        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homescreen,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*switch (id){
            case R.id.addreceipe:
                Toast.makeText(parent,"Add receipe selected",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search:
                Toast.makeText(parent,"Search selected",Toast.LENGTH_SHORT).show();
                return true;
        }*/

        if(id == R.id.addreceipe){
           // Toast.makeText(parent,"Add receipe selected",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(parent, AddNewReceipe.class);
            startActivity(intent);
            finish();

        }else if(id == R.id.search) {
            Toast.makeText(parent,"Search selected",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        parent = HomeScreen.this;

        Intent intent = getIntent();

        layoutGoto = (LinearLayout)findViewById(R.id.layoutGoto);
        layoutGoto.setVisibility(View.VISIBLE);
        list_recps = (ListView) findViewById(R.id.list_recps);
        lay_imgvw = (LinearLayout)findViewById(R.id.lay_imgvw);
        img_gotokitchen = (ImageView)findViewById(R.id.img_gotokitchen);
        img_slider = (ImageView)findViewById(R.id.img_slider);
        recentrec_plist = new ArrayList<Bean_RecentAddedRecps>();

        db = new DatabaseHandler(parent);
        sqldb = db.getWritableDatabase();
        ut = new Utility();
        receipesbeanclass = new Bean_RecentAddedRecps();

    }

    public  void setListener(){
        img_gotokitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,GoToKitchen_AllReceipesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
            }
        });

        layoutGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,GoToKitchen_AllReceipesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
            }
        });

        list_recps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // int imgpath = recentrec_plist.get(position).getRecpImg();
                String RECPHDRID = recentrec_plist.get(position).getRecpHeaderId();
                String RECPCATEGORY = recentrec_plist.get(position).getCategory();
                String ADDEDDATE = recentrec_plist.get(position).getAddedDt();
                String RECPNAME = recentrec_plist.get(position).getRecpname();
                String RECPUPLOADER = recentrec_plist.get(position).getRecUploaderName();

                Intent intent = new Intent(getApplicationContext(), RecentlyAddedReceipes.class);
                intent.putExtra("ReceipeHeaderId",RECPHDRID);
                intent.putExtra("ReceipeCategory",RECPCATEGORY);
                intent.putExtra("ReceipeName",RECPNAME);
                intent.putExtra("ReceipeAddedDate",ADDEDDATE);
                intent.putExtra("ReceipeUploadeBy",RECPUPLOADER);

                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
            }
        });
    }

    //Imageviewer thread
    Thread imgslider = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            img_slider.setImageResource(imgthumbIds[imgcnt]);
                            //overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                            Display display = getWindowManager().getDefaultDisplay();
                            float width = display.getWidth();/*width + 75*/
                            TranslateAnimation animation = new TranslateAnimation(-100, 0, 0, 0); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                            animation.setDuration(700); // animation duration
                            animation.setRepeatCount(5); // animation repeat count
                            animation.setRepeatMode(2); // repeat animation (left to right, right to
                            // left )
                            // animation.setFillAfter(true);
                            img_slider.startAnimation(animation);
                            imgcnt++;
                            if (imgcnt >= imgthumbIds.length) {
                                imgcnt = 0;
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void getDummyList(){

        for (int i=0;i<imgthumbIds.length;i++){
            receipesbeanclass = new Bean_RecentAddedRecps();
            receipesbeanclass.setRecpname("Veg - Pulaw");
            receipesbeanclass.setFavcnt("3");
            receipesbeanclass.setRecpTime("1hr 30min");
            receipesbeanclass.setRecUploaderName("Chetana Salunkhe");
         //   receipesbeanclass.setRecpImg(imgthumbIds[i]);
            recentrec_plist.add(receipesbeanclass);
        }

        homeListAdapter = new HomeListAdapter(parent,recentrec_plist);
        list_recps.setAdapter(homeListAdapter);

    }

    private void getDataFromDatabase(){
        recentrec_plist.clear();
        sqldb = db.getReadableDatabase();

        String query = "Select * from recpeheader";
        Cursor chdr = sqldb.rawQuery(query,null);
        Log.e("",String.valueOf(chdr));
        if(chdr.getCount() > 0){
            chdr.moveToFirst();
            do{
                try{
                    recphdrid  = chdr.getString(chdr.getColumnIndex("Recp_headerid"));
                    recpname  = chdr.getString(chdr.getColumnIndex("Recp_title"));
                    recpaddedDt  = chdr.getString(chdr.getColumnIndex("Recp_addeddt"));
                    recpcategory  = chdr.getString(chdr.getColumnIndex("Recp_category"));
                    recpduration  = chdr.getString(chdr.getColumnIndex("Recp_prep_durtn"));
                    recpfavcnt  = chdr.getString(chdr.getColumnIndex("Recp_fvrtcnt"));
                    recpfeedback  = chdr.getString(chdr.getColumnIndex("Recp_feedback"));
                    IMG = chdr.getString(chdr.getColumnIndex("Recp_imgpath"));
                    //imgrecp = chdr.getString(chdr.getColumnIndex("Recp_imgpath"));
                    recpuploadby  = chdr.getString(chdr.getColumnIndex("Recp_uploadedby"));

                    receipesbeanclass = new Bean_RecentAddedRecps();
                    receipesbeanclass.setRecpHeaderId(recphdrid);
                    receipesbeanclass.setRecpname(recpname);
                    receipesbeanclass.setAddedDt(recpaddedDt);
                    receipesbeanclass.setCategory(recpcategory);
                    receipesbeanclass.setRecpTime(recpduration);
                    receipesbeanclass.setFavcnt(recpfavcnt);
                    receipesbeanclass.setFeedback(recpfeedback);

                    if(IMG == null){
                        IMG = "";
                    }else {

                    }
                    receipesbeanclass.setRecpImg(IMG);
                  //  receipesbeanclass.setRecpImgDATA(imgrecp);
                    receipesbeanclass.setRecUploaderName(recpuploadby);

                  /*  Bitmap bmp = getBitmapImage();
                    byte[] data = getBitmapAsByteArray(bmp);
                    receipesbeanclass.setRecpImgDATA(imgrecp);*/

                    recentrec_plist.add(receipesbeanclass);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }while (chdr.moveToNext());

            homeListAdapter = new HomeListAdapter(parent,recentrec_plist);
            list_recps.setAdapter(homeListAdapter);

        }else {
            Toast.makeText(parent,"No receipes found",Toast.LENGTH_SHORT).show();
        }

    }

    public Bitmap getBitmapImage(){
        return BitmapFactory.decodeByteArray(imgrecp, 0, imgrecp.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void AddDefaultReceipes(){
        Receipe1_vegPulao();
        Receipe1_eggmaggie();
        Receipe1_gajarhalwa();
    }

    public void Receipe1_vegPulao(){
        String temp = "";
        RECPHDRID = "1"+GenerateRecpHdrId();
        String RECPDTLID = GenerateRecpDetailId();

        String recpName = "Veg Pulao";
        String catgry = "Veg";
        String AddedDate = GetTodaysConvertDate();
        String prepTime = "55 Min";
        ArrayList<Bean_RecentAddedRecps> ingred = new ArrayList<Bean_RecentAddedRecps>();

        String ingDesc = "Long grained or basmati rice, Chopped potatoes, Cauliflower/broccoli, French beans, Green peas(fresh/frozen)," +
                "Chopped Carrots,Large onion thinly sliced, Medium size chopped tomato, Ginger, Medium garlic cloves, Green chilli," +
                "Lime juice, Water, Ghee/Oil, Salt, Cumin seeds, Black pepper,Indian bay leaf, Cloves, Green cardamoms, Single strands of mace, " +
                "Small star anise, Cinnamon, Stone flower";
        String[] ingrddflts_desc_1 = new String[ingDesc.length()];
        ingrddflts_desc_1 = ingDesc.split(",");

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            String itemdesc = ingrddflts_desc_1[i];
        }

        String qty = "1.5, 1/4, 1/4, 1/4, 1/4, 1/4,1,1,1/2,3,1,7,3,2, ,1,5,1,3,3,1,2,1,1,1";
        String[] ingrddflts_qty_1 = new String[qty.length()];
        ingrddflts_qty_1 = qty.split(",");

        for(int i=0;i<ingrddflts_qty_1.length; i++){
            String itemqty = ingrddflts_qty_1[i];
        }

        String unit = "Cup, Cup, Cup, Cup, Cup, Cup,No,No,Inch,No,No,Drops,Cup,TblSpn, Required Pinch,TblSpn,No,No,No,No,No,No," +
                "No,Inch,Small piece";
        String[] ingrddflts_unit_1 = new String[unit.length()];
        ingrddflts_unit_1 = unit.split(",");

        for(int i=0;i<ingrddflts_unit_1.length; i++){
            String itemunit = ingrddflts_unit_1[i];
        }

        String preparatoion = "\t Rinse rice till water runs clear of starch and becomes transparent while rinsing, soak the rice in enought water for 30 minutes." +
                " Drain all water and keep the soaked rice aside. In a deep thick bottomed pot or pan, heat ghee or oil and fry all whole spices " +
                "mentioned above, till the oil becomes fragrant. Add onions and saute them till golden. Saute the onions on a low flame and stir " +
                "often for uniform browning. Add the ginger garlic green chilli paste and saute for 10-12 seconds. Add tomatoes and saute for 2-3 minutes on a low " +
                " to medium low flame. \n\t Add rice and saute gently for 2 minutes on low or medium low flame. so the rice gets well coated with the oil. " +
                "Pour water and stir. Sprinkle 6 to 7 drops of lime juice season with salt and stir again. " +
                "\n\t Cover tightly and let the rice cook on low flame, till water is absorbed and rice is well cooked. Check in between few times to check if water " +
                "is enough. Depending on the quality of rice you may need to add less or more water with a fork too, you can gently stir the rice without breaking the " +
                "rice grains. Once the rice grains are cooked, fluff and let the rice stand for 5 minutes.\n\t Your Veg Pulao is ready!";

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            Bean_RecentAddedRecps bdefrec1 = new Bean_RecentAddedRecps();
            bdefrec1.setIngrd_desc(ingrddflts_desc_1[i]);
            bdefrec1.setIngrd_unit(ingrddflts_unit_1[i]);
            bdefrec1.setIngrd_qty(ingrddflts_qty_1[i]);
            bdefrec1.setRecpHeaderId(RECPHDRID);
            bdefrec1.setRecpDetailId(RECPDTLID);

            ingred.add(bdefrec1);
            IngredientsAdapter ingredientsAdapter;
            ingredientsAdapter = new IngredientsAdapter(parent,ingred);
        }

        if(db.AddReceipeDetail(ingred)){
            Toast.makeText(parent,"Ingredients saved successfully",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(parent,"Ingredients not saved",Toast.LENGTH_SHORT).show();
        }

        Bean_RecentAddedRecps bdefrechdr = new Bean_RecentAddedRecps();
        bdefrechdr.setRecpHeaderId(RECPHDRID);
        bdefrechdr.setRecUploaderName("Chetana Salunkhe");
        bdefrechdr.setRecpname(recpName);
        bdefrechdr.setCategory(catgry);
        bdefrechdr.setAddedDt(AddedDate);
        bdefrechdr.setPreparation_desc(preparatoion);
        bdefrechdr.setRecpTime(prepTime);
        //  bdefrechdr.setRecpImgDATA(imgrecp);
       /* Drawable myDrawable = getResources().getDrawable(R.drawable.veg_pulao);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        byte[] data = getBitmapAsByteArray(myLogo); // this is a function
        temp = Base64.encodeToString(data, Base64.DEFAULT);*/
        temp = "vegpulao";
        bdefrechdr.setRecpImg(temp);

        if(db.AddReceipeHeader(bdefrechdr)){
            Toast.makeText(parent,"Receipe added Successfully.",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(parent,"Receipe not added.",Toast.LENGTH_SHORT).show();
        }

    }

    public void Receipe1_eggmaggie(){
        String temp = "";
        RECPHDRID = "2"+GenerateRecpHdrId();
        String RECPDTLID = GenerateRecpDetailId();

        String recpName = "Egg Maggie";
        String catgry = "Non-veg";
        String AddedDate = GetTodaysConvertDate();
        String prepTime = "20 Min";
        ArrayList<Bean_RecentAddedRecps> ingred = new ArrayList<Bean_RecentAddedRecps>();

        String ingDesc = "Eggs,Maggie pack,Water,Sliced onion,Sliced capsicum,Red and green chilli chopped,Roasted cumin powder,Garam masala," +
                "Red chilli powder and salt,Vegetable oil,Hot and sweet maggie sauce";
        String[] ingrddflts_desc_1 = new String[ingDesc.length()];
        ingrddflts_desc_1 = ingDesc.split(",");

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            String itemdesc = ingrddflts_desc_1[i];
        }

        String qty = "3,2,1.5,1/2,1/2,1,1/2,1/2,1/2,2,3";
        String[] ingrddflts_qty_1 = new String[qty.length()];
        ingrddflts_qty_1 = qty.split(",");

        for(int i=0;i<ingrddflts_qty_1.length; i++){
            String itemqty = ingrddflts_qty_1[i];
        }

        String unit = "No,No,Cup,No,No,TblSpn,TblSpn,TblSpn,TblSpn,TblSpn,TblSpn";
        String[] ingrddflts_unit_1 = new String[unit.length()];
        ingrddflts_unit_1 = unit.split(",");

        for(int i=0;i<ingrddflts_unit_1.length; i++){
            String itemunit = ingrddflts_unit_1[i];
        }

        String preparatoion = "\t To cook the maggi, in a microwave safe bowl, break 2 packs of maggi, add the masala, leave a little bit to garnish in the end.\n\t\t" +
                "Add a little less than 1 ½ cups of water. Cook in the microwave for 4 minutes on high. Once done keep it aside. Do not worry if you see some water left, the maggi will soak it up. Don't be tempted to cook more, you don't want to make the maggi mushy.\n\t\t" +
                "You can even cook the maggi on the stove, follow the same instructions(but bring the water to a boil and then add the maggi).\n\t\t" +
                "In a pan, add 1-2 tbsp of olive oil, once hot add the sliced onion and fry on high flame for 4 minutes until the onions become crisp.\n\t\t" +
                "Add the capsicum and the chillies next and fry to 3-4 minutes(add the shredded cabbage at this time if you have some). Then add the spices, a pinch of turmeric powder, ½ tsp garam masala, ½ tsp red chilli powder, ½ tsp roasted cumin powder and salt to taste.\n\t\t" +
                "Cook for a minute and then add 2-3 tbsp of hot and sweet maggi sauce and saute for another 30 seconds.\n\t\t" +
                "Then push the veggies aside, and break 2-3 eggs, scramble and combine the eggs with the veggies. Cook until the mixture is completely dry.\n\t\t" +
                "Toss in the cooked maggi and mix well for about 2 minutes and your egg maggi is done.\n\t\t" +
                "Serve hot and sprinkle some leftover maggi masala and crushed black pepper. Enjoy!";

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            Bean_RecentAddedRecps bdefrec1 = new Bean_RecentAddedRecps();
            bdefrec1.setIngrd_desc(ingrddflts_desc_1[i]);
            bdefrec1.setIngrd_unit(ingrddflts_unit_1[i]);
            bdefrec1.setIngrd_qty(ingrddflts_qty_1[i]);
            bdefrec1.setRecpHeaderId(RECPHDRID);
            bdefrec1.setRecpDetailId(RECPDTLID);

            ingred.add(bdefrec1);
            IngredientsAdapter ingredientsAdapter;
            ingredientsAdapter = new IngredientsAdapter(parent,ingred);
        }

        if(db.AddReceipeDetail(ingred)){
            Toast.makeText(parent,"Ingredients saved successfully",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(parent,"Ingredients not saved",Toast.LENGTH_SHORT).show();
        }

        Bean_RecentAddedRecps bdefrechdr = new Bean_RecentAddedRecps();
        bdefrechdr.setRecpHeaderId(RECPHDRID);
        bdefrechdr.setRecUploaderName("Sunita Sehgal");
        bdefrechdr.setRecpname(recpName);
        bdefrechdr.setCategory(catgry);
        bdefrechdr.setAddedDt(AddedDate);
        bdefrechdr.setPreparation_desc(preparatoion);
        bdefrechdr.setRecpTime(prepTime);
        //  bdefrechdr.setRecpImgDATA(imgrecp);
       /* Drawable myDrawable = getResources().getDrawable(R.drawable.veg_pulao);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        byte[] data = getBitmapAsByteArray(myLogo); // this is a function
        temp = Base64.encodeToString(data, Base64.DEFAULT);*/
        temp = "eggmaggie";
        bdefrechdr.setRecpImg(temp);

        if(db.AddReceipeHeader(bdefrechdr)){
            Toast.makeText(parent,"Receipe added Successfully.",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(parent,"Receipe not added.",Toast.LENGTH_SHORT).show();
        }

    }

    public void Receipe1_gajarhalwa(){
        String temp = "";
        RECPHDRID = "3"+GenerateRecpHdrId();
        String RECPDTLID = GenerateRecpDetailId();

        String recpName = "Gajar Halwa Khoya";
        String catgry = "Veg";
        String AddedDate = GetTodaysConvertDate();
        String prepTime = "45 Min";
        ArrayList<Bean_RecentAddedRecps> ingred = new ArrayList<Bean_RecentAddedRecps>();

        String ingDesc = "Ghee (Clarified butter),Carrots grated,Milk,Sugar,Khoya,Green cardamon seeds powder,Almond chopped,Cashewnuts chopped," +
                "Raisins ";
        String[] ingrddflts_desc_1 = new String[ingDesc.length()];
        ingrddflts_desc_1 = ingDesc.split(",");

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            String itemdesc = ingrddflts_desc_1[i];
        }

        String qty = "1,2,2,1/3,1/3,1/4,1,1,1/2";
        String[] ingrddflts_qty_1 = new String[qty.length()];
        ingrddflts_qty_1 = qty.split(",");

        for(int i=0;i<ingrddflts_qty_1.length; i++){
            String itemqty = ingrddflts_qty_1[i];
        }

        String unit = "TblSpn,Cup,Cup,Cup,Cup,TblSpn,TblSpn,TblSpn,TblSpn";
        String[] ingrddflts_unit_1 = new String[unit.length()];
        ingrddflts_unit_1 = unit.split(",");

        for(int i=0;i<ingrddflts_unit_1.length; i++){
            String itemunit = ingrddflts_unit_1[i];
        }

        String preparatoion = "\t\t Heat the ghee in a pan in a medium heat. Once it get heated add grated carrot and saute for 2-3 minutes or till the carrot " +
                "starts soften. Now add milk and let it come to boil. Simmer on a low medium heat till all the milk evaporates. Do stir in between so milk does" +
                " not stick to the bottom and sides of the pan. \n\t\t Now add khoya mix it and let it melt completely. Then add sugar, cardamon powder and continue " +
                "cooking till all the liquid dries out and becomes thick halwa consistency again. This time you need to stir it more frequently. " +
                "\n\t\t Lastly mix in chopped almond, cashews and raisins. Halwa is ready to serve!";

        for(int i=0;i<ingrddflts_desc_1.length; i++){
            Bean_RecentAddedRecps bdefrec1 = new Bean_RecentAddedRecps();
            bdefrec1.setIngrd_desc(ingrddflts_desc_1[i]);
            bdefrec1.setIngrd_unit(ingrddflts_unit_1[i]);
            bdefrec1.setIngrd_qty(ingrddflts_qty_1[i]);
            bdefrec1.setRecpHeaderId(RECPHDRID);
            bdefrec1.setRecpDetailId(RECPDTLID);

            ingred.add(bdefrec1);
            IngredientsAdapter ingredientsAdapter;
            ingredientsAdapter = new IngredientsAdapter(parent,ingred);
        }

        if(db.AddReceipeDetail(ingred)){
            Toast.makeText(parent,"Ingredients saved successfully",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(parent,"Ingredients not saved",Toast.LENGTH_SHORT).show();
        }

        Bean_RecentAddedRecps bdefrechdr = new Bean_RecentAddedRecps();
        bdefrechdr.setRecpHeaderId(RECPHDRID);
        bdefrechdr.setRecUploaderName("Richa Sharma");
        bdefrechdr.setRecpname(recpName);
        bdefrechdr.setCategory(catgry);
        bdefrechdr.setAddedDt(AddedDate);
        bdefrechdr.setPreparation_desc(preparatoion);
        bdefrechdr.setRecpTime(prepTime);
        //  bdefrechdr.setRecpImgDATA(imgrecp);
       /* Drawable myDrawable = getResources().getDrawable(R.drawable.veg_pulao);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        byte[] data = getBitmapAsByteArray(myLogo); // this is a function
        temp = Base64.encodeToString(data, Base64.DEFAULT);*/
        temp = "gajarhalwa";
        bdefrechdr.setRecpImg(temp);

        if(db.AddReceipeHeader(bdefrechdr)){
            Toast.makeText(parent,"Receipe added Successfully.",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(parent,"Receipe not added.",Toast.LENGTH_SHORT).show();
        }

    }

    private String GenerateRecpHdrId(){
        int val;
        Date C_TIME = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        String str_recpHdrID = df.format(C_TIME);
        /*try {
            val =((Number) NumberFormat.getInstance().parse(str_annschhistid)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        String recphdrid = "1000"+ str_recpHdrID ;
        return recphdrid;
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

    public int checkRecpHeader(){

        sqldb = db.getReadableDatabase();
        int res = 0;
        Cursor ccheck = sqldb.rawQuery("Select * from recpeheader", null);
        if(ccheck.getCount() > 0){
            ccheck.moveToFirst();
            res = ccheck.getCount();

        }else {
            res = 0;
        }
        return res;
    }

    private String GetTodaysConvertDate(){
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
}
