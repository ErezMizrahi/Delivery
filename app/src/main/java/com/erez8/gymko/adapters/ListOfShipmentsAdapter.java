package com.erez8.gymko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erez8.gymko.Models.Delivery;
import com.erez8.gymko.R;

import java.util.ArrayList;
import java.util.List;

public class ListOfShipmentsAdapter extends BaseAdapter {

    private List<Delivery> listOfShipments = new ArrayList<>();
    private Context mContext;

    public ListOfShipmentsAdapter(List<Delivery> listOfShipments, Context mContext) {
        this.listOfShipments = listOfShipments;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listOfShipments.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfShipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_of_shipments_item,parent,false);
        TextView details = view.findViewById(R.id.what_to);
        TextView location = view.findViewById(R.id.location);
        TextView time = view.findViewById(R.id.timeOFDelivery);
        details.setText(listOfShipments.get(position).getWhat_to_deliver());
        location.setText(listOfShipments.get(position).getLocation_of_delivery());
        time.setText(listOfShipments.get(position).getTime_of_arrival());

        return view;
    }

}
