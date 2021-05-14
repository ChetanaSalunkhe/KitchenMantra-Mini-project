package com.example.chetana.kitchenmantra;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetana.kitchenmantra.Adapters.IngredientsAdapter;
import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.Database.DatabaseHandler;
import com.example.chetana.kitchenmantra.Utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewReceipe extends AppCompatActivity {
    private Context parent;
    ArrayList<String> spin_cat_list;
    String[] spincat_array;

    Spinner spin_category;
    EditText edtrecname, edttxtpreparations,edtrecpdurtn;
    TextView txtrec_upldname, txtingredients,txtpreparations;
    ImageView img_edtingred, img_edtpreptn;
    ImageButton imgcamera;
    ListView lstingredients;
    LinearLayout lay_add_ingredients,lay_add_preparation,lay_add_whatsinit;
    Button btnsubmit;
    Bean_RecentAddedRecps bAddRecps;

    DatabaseHandler db;
    SQLiteDatabase sqldb;
    Utility ut;

    String username;
    String RECPHDRID;
    String Recp_preparation, recpCategory, addedDt,recpduration;

    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "KitchenMantra Images";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    String encodedImage, image_encode="NA",Imagefilename, photoName;
    private static int IMG_RESULT = 200;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_receipe);

        getSupportActionBar().setTitle(" Add New Receipe ");

        init();

        username = GetUsername();
        txtrec_upldname.setText(username);

        RECPHDRID = GenerateRecpHdrId();

        setValuToSpinner();

      //  setIngredstoList();

        setListener();

    }

    public void init(){
        parent = AddNewReceipe.this;

        spin_category = (Spinner)findViewById(R.id.spin_category);
        spincat_array = new String[]{"Veg", "Non-veg"};
        spin_cat_list = new ArrayList<String>();

        lay_add_ingredients = (LinearLayout)findViewById(R.id.lay_add_ingredients);
        lay_add_preparation = (LinearLayout)findViewById(R.id.lay_add_preparation);
        lay_add_whatsinit = (LinearLayout)findViewById(R.id.lay_add_whatsinit);
        txtrec_upldname = (TextView) findViewById(R.id.txtrec_upldname);
        edtrecname = (EditText)findViewById(R.id.edtrecname);
        edtrecpdurtn = (EditText)findViewById(R.id.edtrecpdurtn);
        edttxtpreparations = (EditText)findViewById(R.id.edttxtpreparations);
        txtingredients = (TextView)findViewById(R.id.txtingredients);
      //  txtpreparations = (TextView)findViewById(R.id.txtpreparations);
        img_edtingred = (ImageView)findViewById(R.id.img_edtingred);
        img_edtpreptn = (ImageView)findViewById(R.id.img_edtpreptn);
        imgcamera = (ImageButton) findViewById(R.id.imgcamera);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);
        lstingredients = (ListView)findViewById(R.id.lstingredients);

        db = new DatabaseHandler(parent);
        sqldb = db.getWritableDatabase();
        ut = new Utility();
        bAddRecps = new Bean_RecentAddedRecps();
    }

    public void setValuToSpinner(){

        for(int i=0;i<spincat_array.length; i++){
            String category = spincat_array[i];
            spin_cat_list.add(category);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                android.R.layout.simple_spinner_item, spin_cat_list);
        spin_category.setAdapter(adapter);
    }

    public String GetUsername(){

        Cursor c = sqldb.rawQuery(" Select * from "+ut.TABLE_USER,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                username = c.getString(c.getColumnIndex(""+ut.COLUMN_USERNAME));
            }while (c.moveToNext());

            //edtrec_upldname.setText(username);
        }else {
            //no user
            Toast.makeText(parent,"No user present",Toast.LENGTH_SHORT).show();
        }
        return username;

    }

    public void setListener(){

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recpCategory = spin_category.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        img_edtingred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, AddIngredients.class);
                intent.putExtra("ReceipeHeaderId",RECPHDRID);
                startActivity(intent);
            }
        });

        img_edtpreptn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = 111;

                Intent intent = new Intent(parent, AddPreparation.class);
                intent.putExtra("ReceipeHeaderId",RECPHDRID);
               // startActivity(intent);
                startActivityForResult(intent, requestCode);
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save receipe details in Receipeheader table

                addDataInRecpHDRTable();

                Intent intent = new Intent(parent,HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void setIngredstoList(){

        String ingrdDesc, ingrdqty, ingrdunit;
        ArrayList<Bean_RecentAddedRecps> arrListIngrds = new ArrayList<Bean_RecentAddedRecps>();

        String query_ingrd = "select * from recpedetail where Recp_headerid = '"+RECPHDRID +"'";
        Cursor cingrd = sqldb.rawQuery(query_ingrd,null);
        Log.e("crecp", String.valueOf(cingrd.getCount()));

        if(cingrd.getCount() > 0){
            cingrd.moveToFirst();
            txtingredients.setVisibility(View.GONE);
            lstingredients.setVisibility(View.VISIBLE);
            do{
                ingrdDesc = cingrd.getString(cingrd.getColumnIndex("ingrd_itemdesc"));
                ingrdqty = cingrd.getString(cingrd.getColumnIndex("ingrd_itemqty"));
                ingrdunit = cingrd.getString(cingrd.getColumnIndex("ingrd_itemunit"));

                Bean_RecentAddedRecps beanIngrd = new Bean_RecentAddedRecps();
                beanIngrd.setIngrd_desc(ingrdDesc);
                beanIngrd.setIngrd_qty(ingrdqty);
                beanIngrd.setIngrd_unit(ingrdunit);

                arrListIngrds.add(beanIngrd);

            }while (cingrd.moveToNext());

            IngredientsAdapter adapteringrd = new IngredientsAdapter(parent,arrListIngrds);
            lstingredients.setAdapter(adapteringrd);

        }else {
            txtingredients.setVisibility(View.VISIBLE);
            lstingredients.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setIngredstoList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(parent,HomeScreen.class);
        startActivity(intent);
        finish();
       // overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_left_to_right);
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

    private void addDataInRecpHDRTable(){

        sqldb = db.getWritableDatabase();

        String ingrdqty, ingrdunit, ingrddesc,

        addedDt = GetTodaysConvertDate();
        String category = recpCategory;
        recpduration = edtrecpdurtn.getText().toString();

        String query = "Select * from recpedetail where Recp_headerid = '"+ RECPHDRID + "'";

        Cursor cdtldata = sqldb.rawQuery(query,null);
        Log.e("cdtldata",String.valueOf(cdtldata.getCount()));
        if(cdtldata.getCount() > 0){
            cdtldata.moveToFirst();
            do{
                ingrddesc = cdtldata.getString(cdtldata.getColumnIndex("ingrd_itemdesc"));
                ingrdqty = cdtldata.getString(cdtldata.getColumnIndex("ingrd_itemqty"));
                ingrdunit = cdtldata.getString(cdtldata.getColumnIndex("ingrd_itemunit"));

            }while (cdtldata.moveToNext());

        }else {
            Toast.makeText(parent,"No ingredients added for this receipe",Toast.LENGTH_SHORT).show();
        }

        bAddRecps.setRecpHeaderId(RECPHDRID);
        bAddRecps.setRecUploaderName(username);
        bAddRecps.setRecpname(edtrecname.getText().toString().trim());
        bAddRecps.setCategory(recpCategory);
        bAddRecps.setAddedDt(addedDt);
        bAddRecps.setPreparation_desc(/*Recp_preparation*/edttxtpreparations.getText().toString());
        bAddRecps.setRecpTime(recpduration);

        try{
            byte[] data = getBitmapAsByteArray(photo); // this is a function
            String temp=Base64.encodeToString(data, Base64.DEFAULT);
           //   bAddRecps.setRecpImgDATA(data);
            bAddRecps.setRecpImg(temp);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(db.AddReceipeHeader(bAddRecps)){
            Toast.makeText(parent,"Receipe added Successfully.",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(parent,"Receipe not added.",Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
                photo = (Bitmap) data.getExtras().get("data");
                imgcamera.setImageBitmap(photo);
            } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    photo = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                  //  photo.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    imgcamera.setImageBitmap(photo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            try{
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                dialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            } else if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else{
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, 0);
                   // Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
