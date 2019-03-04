package com.erez8.gymko.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.erez8.gymko.Models.Business;
import com.erez8.gymko.R;

import java.util.ArrayList;

public class BusinessRecyclerAdapter extends RecyclerView.Adapter<BusinessRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Business> mBusinesses = new ArrayList<>();
    private BusinessesRecyclerClickListener mBusinessesRecyclerClickListener;

    public BusinessRecyclerAdapter(Context mContext, ArrayList<Business> businesses , BusinessesRecyclerClickListener businessesRecyclerClickListener) {
        this.mBusinesses = businesses;
        mBusinessesRecyclerClickListener = businessesRecyclerClickListener;
        this.mContext= mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_business_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, mBusinessesRecyclerClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder)holder).businessTitle.setText(mBusinesses.get(position).getTitle());

     }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView businessTitle;
        BusinessesRecyclerClickListener clickListener;

        public ViewHolder(View itemView, BusinessesRecyclerClickListener clickListener) {
            super(itemView);
            businessTitle = itemView.findViewById(R.id.business_title);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onBusinessSelected(getAdapterPosition());
        }
    }

    public interface BusinessesRecyclerClickListener {
        public void onBusinessSelected(int position);
    }
}
















