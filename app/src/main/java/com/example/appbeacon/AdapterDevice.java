package com.example.appbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDevice extends RecyclerView.Adapter<AdapterDevice.MyViewHolder> {

    private Context context;
    private ArrayList beacon_id, beacon_name, beacon_uuid, beacon_major, beacon_minor, beacon_txpower;
    Activity activity;

    AdapterDevice(Activity activity, Context context,ArrayList beacon_id, ArrayList beacon_name, ArrayList beacon_uuid, ArrayList beacon_major, ArrayList beacon_minor, ArrayList beacon_txpower){
        this.activity = activity;
        this.context = context;
        this.beacon_id = beacon_id;
        this.beacon_name = beacon_name;
        this.beacon_uuid = beacon_uuid;
        this.beacon_major = beacon_major;
        this.beacon_minor = beacon_minor;
        this.beacon_txpower = beacon_txpower;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout2;
        TextView beacon_nametxt,beacon_uuidtxt,beacon_majortxt,beacon_minortxt,beacon_txpowertxt,beacon_idtxt;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            beacon_idtxt = itemView.findViewById(R.id.beacon_id_txt);
            beacon_nametxt = itemView.findViewById(R.id.name_label);
            beacon_uuidtxt = itemView.findViewById(R.id.uuid_text);
            beacon_majortxt = itemView.findViewById(R.id.major_text);
            beacon_minortxt = itemView.findViewById(R.id.minor_text);
            beacon_txpowertxt = itemView.findViewById(R.id.txpower_text);
            mainLayout2 = itemView.findViewById(R.id.mainLayout2);
        }
    }

    @NonNull
    @Override
    public AdapterDevice.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_device, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDevice.MyViewHolder holder, int position) {
        holder.beacon_idtxt.setText(String.valueOf(beacon_id.get(position)));
        holder.beacon_nametxt.setText(String.valueOf(beacon_name.get(position)));
        holder.beacon_uuidtxt.setText(String.valueOf(beacon_uuid.get(position)));
        holder.beacon_majortxt.setText(String.valueOf(beacon_major.get(position)));
        holder.beacon_minortxt.setText(String.valueOf(beacon_minor.get(position)));
        holder.beacon_txpowertxt.setText(String.valueOf(beacon_txpower.get(position)));
        holder.mainLayout2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(beacon_id.get(position)));
                intent.putExtra("name", String.valueOf(beacon_name.get(position)));
                intent.putExtra("uuid", String.valueOf(beacon_uuid.get(position)));
                intent.putExtra("major", String.valueOf(beacon_major.get(position)));
                intent.putExtra("minor", String.valueOf(beacon_minor.get(position)));
                intent.putExtra("txpower", String.valueOf(beacon_txpower.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beacon_uuid.size();
    }
}
