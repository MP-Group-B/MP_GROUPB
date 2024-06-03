package com.example.testtp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusStopDataAdapter extends RecyclerView.Adapter<BusStopDataAdapter.BusStopViewHolder> {
    private List<BusStopData> busStopList;
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public BusStopDataAdapter(List<BusStopData> busStopList, OnItemClickListener listener, OnFavoriteClickListener favoriteListener){
        this.busStopList = new ArrayList<>();
        this.listener = listener;
        this.favoriteListener = favoriteListener;
        this.busStopList.addAll(busStopList);
    }

    @NonNull
    @Override
    public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_busstop_data,parent,false);
        return new BusStopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStopViewHolder holder, int position){
        BusStopData busStopData = busStopList.get(position);
        holder.bind(busStopData, listener, favoriteListener);
    }

    @Override
    public int getItemCount(){
        return busStopList.size();
    }

    public void updateBusStopList(List<BusStopData> newBusStopList){
        this.busStopList.clear();
        busStopList.addAll(newBusStopList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(BusStopData busStopData);
    }

    public interface OnFavoriteClickListener{
        void onFavoriteClick(BusStopData busStopData);
    }

    static class BusStopViewHolder extends RecyclerView.ViewHolder{
        TextView transView;
        TextView busStopNmView;
        TextView busStopNoView;
        Button favoriteBtn;
        BusStopViewHolder(View itemView){
            super(itemView);
            transView =  itemView.findViewById(R.id.trans);
            busStopNmView = itemView.findViewById(R.id.busStopNm);
            busStopNoView = itemView.findViewById(R.id.busStopNo);
            favoriteBtn = itemView.findViewById(R.id.favoriteBusstop);
        }

        void bind(final BusStopData busStopData, final OnItemClickListener listener, final OnFavoriteClickListener favoriteListener){
            if((busStopData.getTransYn()!= null)&&(busStopData.getTransYn().equals("Y")))
                transView.setVisibility(View.VISIBLE);
            if(busStopData.getTransYn() == null)
                favoriteBtn.setVisibility(View.VISIBLE);

            busStopNmView.setText(busStopData.getStationNm());
            busStopNoView.setText(busStopData.getStationNo());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null)
                        listener.onItemClick(busStopData);
                }
            });

            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoriteListener != null){
                        favoriteListener.onFavoriteClick(busStopData);
                    }
                }
            });
        }
    }

}
