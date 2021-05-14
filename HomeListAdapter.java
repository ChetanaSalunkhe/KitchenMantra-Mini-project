package com.example.chetana.kitchenmantra.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.R;

import java.util.ArrayList;

public class HomeListAdapter extends BaseAdapter {
    ArrayList<Bean_RecentAddedRecps> recpList;
    LayoutInflater inflater;
    Context context;

    public HomeListAdapter(Context parent, ArrayList<Bean_RecentAddedRecps> recp_List) {
        this.recpList = recp_List;
        context = parent;
        inflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return recpList.size();
    }

    @Override
    public Object getItem(int position) {
        return recpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.homelistadapter,null);
            holder = new ViewHolder();

            holder.txtrecname = (TextView)convertView.findViewById(R.id.txtrecname);
            holder.txtfavcnt = (TextView)convertView.findViewById(R.id.txtfavcnt);
            holder.txtrectime = (TextView)convertView.findViewById(R.id.txtrectime);
            holder.txtrecpuploadby = (TextView)convertView.findViewById(R.id.txtrecpuploadby);
            holder.imgrecp = (ImageView)convertView.findViewById(R.id.imgrecp);
            holder.imgvegnonveg = (ImageView)convertView.findViewById(R.id.imgvegnonveg);
            holder.imgheart = (ImageView)convertView.findViewById(R.id.imgheart);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        byte[] imgrecp;
        String IMG;
        /*holder.txtrecname.setText("Veg - Pulaw");
        holder.txtfavcnt.setText("3");
        holder.txtrectime.setText("1hr 30min");
        holder.txtrecpuploadby.setText("Chetana Salunkhe");*/
        holder.txtrecname.setText(recpList.get(position).getRecpname());
        holder.txtfavcnt.setText(recpList.get(position).getFavcnt());
        holder.txtrectime.setText(recpList.get(position).getRecpTime());
        holder.txtrecpuploadby.setText(recpList.get(position).getRecUploaderName());
        IMG = recpList.get(position).getRecpImg();
       // holder.imgrecp.setImageResource(R.drawable.veg_pulao);

        /*if(IMG == null || IMG == ""){
            holder.imgrecp.setImageResource(R.drawable.veg_pulao);
        }else*/ if(IMG.equalsIgnoreCase("vegpulao")) {
            holder.imgrecp.setImageResource(R.drawable.veg_pulao);
        }else if(IMG.equalsIgnoreCase("gajarhalwa")) {
            holder.imgrecp.setImageResource(R.drawable.gajrhalwa);
        }else if(IMG.equalsIgnoreCase("eggmaggie")) {
            holder.imgrecp.setImageResource(R.drawable.eggmagg);
        }else {
                byte [] encodeByte=Base64.decode(IMG,Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                holder.imgrecp.setImageBitmap(bitmap);
            }

        /*try{
            imgrecp = recpList.get(position).getRecpImgDATA();
            Bitmap bmp = BitmapFactory.decodeByteArray(imgrecp, 0, imgrecp.length);
            holder.imgrecp.setImageBitmap(bmp);

        }catch (Exception e){
            e.printStackTrace();
          //  holder.imgrecp.setImageResource(recpList.get(position).getRecpImg());
        }*/

        String category = recpList.get(position).getCategory();

        if(category.equalsIgnoreCase("Veg")){
            holder.imgvegnonveg.setImageResource(R.drawable.veg);
        }else if(category.equalsIgnoreCase("Non-veg")) {
            holder.imgvegnonveg.setImageResource(R.drawable.nonveg);
        }
       // holder.imgvegnonveg.setImageResource(R.drawable.veg);
       //holder.imgheart.setImageResource(R.drawable.favourites_sele);

        return convertView;
    }


    public class ViewHolder{
        TextView txtrecpuploadby, txtrectime,txtfavcnt, txtrecname;
        ImageView imgrecp, imgvegnonveg, imgheart;
    }

}
