package com.example.testtp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StBusAdapter extends RecyclerView.Adapter<StBusAdapter.StBusViewHolder> {
    private List<BusData> busList;

    public StBusAdapter(List<BusData> busList){

        this.busList = new ArrayList<BusData>();
        this.busList.addAll(busList);
    }

    @NonNull
    @Override
    public StBusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stbus_data, parent, false);
        return new StBusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StBusViewHolder holder, int position){
        BusData busData = busList.get(position);
        holder.bind(busData);
    }
    public int getItemCount(){
        return busList.size();
    }

    public void updateBusList(List<BusData> newBusList){
        this.busList.clear();
        busList.addAll(newBusList);
        notifyDataSetChanged();
    }

    static class StBusViewHolder extends RecyclerView.ViewHolder{
        private TextView busNmView;
        private TextView busIdView;
        private TextView arrv1View;
        private TextView arrv2View;

        StBusViewHolder(View itemView){
            super(itemView);
            busNmView = itemView.findViewById(R.id.busName);
            busIdView = itemView.findViewById(R.id.busID);
            arrv1View = itemView.findViewById(R.id.arrv1);
            arrv2View = itemView.findViewById(R.id.arrv2);
        }

        void bind(final BusData busData){
            busNmView.setText(busData.getBusNm());
            busIdView.setText(busData.getBusId());
            if(busData.getArrvmsg1()!= null && !busData.getArrvmsg1().isEmpty())
                arrv1View.setText(busData.getArrvmsg1());
            if(busData.getArrvmsg2()!= null && !busData.getArrvmsg2().isEmpty())
                arrv2View.setText(busData.getArrvmsg2());
        }
    }
}
