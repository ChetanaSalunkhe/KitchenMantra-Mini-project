package com.example.chetana.kitchenmantra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chetana.kitchenmantra.Classes.Bean_RecentAddedRecps;
import com.example.chetana.kitchenmantra.R;

import java.util.ArrayList;

public class IngredientsAdapter extends BaseAdapter {
    ArrayList<Bean_RecentAddedRecps> ingrd_list;
    LayoutInflater inflater;
    Context context;

    public IngredientsAdapter(Context context, ArrayList<Bean_RecentAddedRecps> ingrd_list) {
        this.ingrd_list = ingrd_list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return ingrd_list.size(); }

    @Override
    public Object getItem(int position) { return ingrd_list.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_addingredients,null);
            holder = new ViewHolder();

            holder.txtingrd_desc = (TextView)convertView.findViewById(R.id.txtingrd_desc);
            holder.txtingrd_qty = (TextView)convertView.findViewById(R.id.txtingrd_qty);
            holder.txtingrd_unit = (TextView)convertView.findViewById(R.id.txtingrd_unit);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtingrd_desc.setText(ingrd_list.get(position).getIngrd_desc());
        holder.txtingrd_unit.setText(ingrd_list.get(position).getIngrd_unit());
        holder.txtingrd_qty.setText(ingrd_list.get(position).getIngrd_qty());

        return convertView;
    }

    public class ViewHolder{

        TextView txtingrd_desc,txtingrd_qty, txtingrd_unit;

    }
}
