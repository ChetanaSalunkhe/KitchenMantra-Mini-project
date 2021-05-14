package com.example.chetana.kitchenmantra.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetana.kitchenmantra.Adapters.HomeListAdapter;
import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.Database.DatabaseHandler;
import com.example.chetana.kitchenmantra.R;
import com.example.chetana.kitchenmantra.RecentlyAddedReceipes;
import com.example.chetana.kitchenmantra.Utilities.Utility;

import java.util.ArrayList;

public class NonVegFragment extends Fragment {
    ImageView imgrecp, imgvegnonveg, imgheart;
    TextView txtrecname, txtfavcnt, txtrectime,txtrecpuploadby;

    ListView listreceipes;
    Bean_RecentAddedRecps receipesbeanclass;
    ArrayList<Bean_RecentAddedRecps> recentrec_plist;
    HomeListAdapter homeListAdapter;

    public static final int[] imgthumbIds = {R.drawable.recp1, R.drawable.recpstart,R.drawable.recp33,R.drawable.recp41,R.drawable.recp44,R.drawable.recp9, R.drawable.recp8,R.drawable.recp7,R.drawable.recp6,
            R.drawable.spices4, R.drawable.recp71,R.drawable.recp5,R.drawable.recp1,R.drawable.recppreparation,
            R.drawable.recp51};
    int imgcnt = 0;

    DatabaseHandler db;
    SQLiteDatabase sqldb;
    Utility ut;

    String recpname, recphdrid, recpcategory, recpaddedDt, recpfavcnt, recpfeedback, recpuploadby, recpduration;
    int recpimg;
    String IMG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_ordered_items__fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmentlayout, container, false);

        listreceipes = (ListView)view.findViewById(R.id.listreceipes);

        recentrec_plist = new ArrayList<Bean_RecentAddedRecps>();
        db = new DatabaseHandler(getContext());
        sqldb = db.getWritableDatabase();

        /*imgrecp = (ImageView)view.findViewById(R.id.imgrecp);
        imgvegnonveg = (ImageView)view.findViewById(R.id.imgvegnonveg);
        imgheart = (ImageView)view.findViewById(R.id.imgheart);
        txtrecname = (TextView)view.findViewById(R.id.txtrecname);
        txtfavcnt = (TextView)view.findViewById(R.id.txtfavcnt);
        txtrectime = (TextView)view.findViewById(R.id.txtrectime);
        txtrecpuploadby = (TextView)view.findViewById(R.id.txtrecpuploadby);*/

       /* for (int i=0;i<imgthumbIds.length;i++){

            receipesbeanclass = new Bean_RecentAddedRecps();
            receipesbeanclass.setRecpname("Veg - Pulaw");
            receipesbeanclass.setFavcnt("3");
            receipesbeanclass.setRecpTime("1hr 30min");
            receipesbeanclass.setRecUploaderName("Chetana Salunkhe");
           // receipesbeanclass.setRecpImg(imgthumbIds[i]);
            recentrec_plist.add(receipesbeanclass);

        }*/

       getDataFromDatabase();

        listreceipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // int imgpath = recentrec_plist.get(position).getRecpImg();
                String RECPHDRID = recentrec_plist.get(position).getRecpHeaderId();
                String RECPCATEGORY = recentrec_plist.get(position).getCategory();
                String ADDEDDATE = recentrec_plist.get(position).getAddedDt();
                String RECPNAME = recentrec_plist.get(position).getRecpname();
                String RECPUPLOADER = recentrec_plist.get(position).getRecUploaderName();

                Intent intent = new Intent(getContext(), RecentlyAddedReceipes.class);
                intent.putExtra("ReceipeHeaderId",RECPHDRID);
                intent.putExtra("ReceipeCategory",RECPCATEGORY);
                intent.putExtra("ReceipeName",RECPNAME);
                intent.putExtra("ReceipeAddedDate",ADDEDDATE);
                intent.putExtra("ReceipeUploadeBy",RECPUPLOADER);

                startActivity(intent);
                //overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
            }
        });

        return view;
    }

    private void getDataFromDatabase(){
        recentrec_plist.clear();
        sqldb = db.getReadableDatabase();

        String query = "Select * from recpeheader WHERE Recp_category = 'Non-veg'";
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

            homeListAdapter = new HomeListAdapter(getContext(),recentrec_plist);
            listreceipes.setAdapter(homeListAdapter);

        }else {
            Toast.makeText(getContext(),"No receipes found",Toast.LENGTH_SHORT).show();
        }
    }

}
