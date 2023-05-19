package com.example.appbeacon;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<BeaconData> beaconList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView uuid;
        private TextView major;
        private TextView minor;
        private TextView rssi;
        private TextView distance;
        private TextView txpower;
        ConstraintLayout mainLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            uuid = itemView.findViewById(R.id.uuid);
            major = itemView.findViewById(R.id.major);
            minor = itemView.findViewById(R.id.minor);
            rssi = itemView.findViewById(R.id.rssi);
            distance = itemView.findViewById(R.id.distance);
            txpower = itemView.findViewById(R.id.txpower_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

    }

    private List<BeaconData> beacon;
    Context mContext;

    public MyAdapter(Context mContext, List<BeaconData> beacon){
        this.mContext = mContext;
        this.beacon = beacon;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        /*View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_row,viewGroup,false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;*/

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View nameView = inflater.inflate(R.layout.my_row, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(nameView);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position){
        /*myViewHolder.uuid.setText(beacon.get(i).uuid);
        myViewHolder.major.setText(beacon.get(i).major);
        myViewHolder.minor.setText(beacon.get(i).minor);
        myViewHolder.rssi.setText((int) beacon.get(i).rssi);*/

        BeaconData beaconData = beacon.get(position);
        TextView textView = myViewHolder.name;
        textView.setText(String.valueOf(beaconData.getName()));
        TextView textView2 = myViewHolder.uuid;
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(beaconData.getUuid());
        TextView textView3 = myViewHolder.major;
        textView3.setText(""+ beaconData.getMajor());
        TextView textView4 = myViewHolder.minor;
        textView4.setText(""+ beaconData.getMinor());

        TextView textView5 = myViewHolder.rssi;
        String rssi_double = Double.toString(beaconData.getRssi());
        textView5.setText(rssi_double + " dBm");

        TextView textView6 = myViewHolder.distance;
        String distance_double = Double.toString(((double)((int)(beaconData.getDistance()*100.0)))/100.0);
        textView6.setText(distance_double +" m");

        TextView textView7 = myViewHolder.txpower;

        myViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SecondActivity.class);
                intent.putExtra("name", beaconData.getName());
                intent.putExtra("uuid", beaconData.getUuid());
                intent.putExtra("Major", beaconData.getMajor());
                intent.putExtra("Minor", beaconData.getMinor());
                intent.putExtra("Rssi", beaconData.getRssi());
                intent.putExtra("Distance", beaconData.getDistance());
                intent.putExtra("TxPower", beaconData.getTxpower());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return beacon.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void clear(){
        this.beaconList = null;
        notifyDataSetChanged();
    }

    public void addAll(List<BeaconData> beaconList){
        this.beaconList = beaconList;
        //notifyDataSetChanged();
    }
    
}
