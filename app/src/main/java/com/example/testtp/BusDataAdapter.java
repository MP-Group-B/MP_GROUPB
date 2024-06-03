package com.example.testtp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusDataAdapter extends RecyclerView.Adapter<BusDataAdapter.BusDataViewHolder>{
    private List<BusData> busList;
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public BusDataAdapter(List<BusData> busList, OnItemClickListener listener, OnFavoriteClickListener favoriteListener){
        this.busList = new ArrayList<BusData>();
        this.listener = listener;
        this.favoriteListener = favoriteListener;
        this.busList.addAll(busList);
    }

    @NonNull
    @Override
    public BusDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_data,parent, false);
        return new BusDataViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BusDataViewHolder holder, int position){
        BusData busData = busList.get(position);
        holder.bind(busData,listener, favoriteListener);
    }

    public int getItemCount(){
        return busList.size();
    }

    public void updateBusList(List<BusData> newBusList){
        this.busList.clear();
        busList.addAll(newBusList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(BusData busData);
    }

    public interface OnFavoriteClickListener{
        void onFavoriteClick(BusData busData);
    }

    static class BusDataViewHolder extends RecyclerView.ViewHolder{
        TextView busNmTextView;
        TextView busIdTextView;
        TextView busRegionTextView;
        Button favoriteBtn;

        BusDataViewHolder(View itemView){
            super(itemView);
            busNmTextView = itemView.findViewById(R.id.busName);
            busIdTextView = itemView.findViewById(R.id.busId);
            busRegionTextView = itemView.findViewById(R.id.busRegion);
            favoriteBtn = itemView.findViewById(R.id.favoriteBus);
        }
        void bind(final BusData busData, final OnItemClickListener listener, final OnFavoriteClickListener favoriteListener){
            busNmTextView.setText(busData.getBusNm());
            busIdTextView.setText(busData.getBusId());
            busRegionTextView.setText(busData.getBusRegion());

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null)
                        listener.onItemClick(busData);
                }
            });

            favoriteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(favoriteListener != null)
                        favoriteListener.onFavoriteClick(busData);
                }
            });
        }
    }
}
