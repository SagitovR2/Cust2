package com.example.cust;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AllFishAdapter extends RecyclerView.Adapter<FishAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<FishesInAqua> fishesAll;
    Context context;

    AllFishAdapter(Context context, ArrayList<FishesInAqua> fishes) {
        this.fishesAll = fishes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public FishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.all_fishes_list, parent, false);
        return new FishAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView fishName;
        final Button closeButton;
        ViewHolder(View view){
            super(view);
            fishName = (TextView) view.findViewById(R.id.fishName);
            closeButton = view.findViewById(R.id.closeButton);
        }
    }

    @Override
    public void onBindViewHolder(FishAdapter.ViewHolder holder, int position) {
        FishesInAqua fish = fishesAll.get(position);
        holder.fishName.setText(fish.getName());
    }

    @Override
    public int getItemCount() {
        return fishesAll.size();
    }

    public ArrayList<FishesInAqua> getFishesAll() {
        return fishesAll;
    }
}
/*{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<FishesInAqua> fishesInAqua;

    AllFishAdapter(Context context, int resource, ArrayList<FishesInAqua> fishes) {
        super(context, resource, fishes);
        this.fishesInAqua = fishes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FishesInAqua fish = fishesInAqua.get(position);

        viewHolder.fishName.setText(fish.getName());

        return convertView;
    }
    private class ViewHolder {
        final TextView fishName;
        ViewHolder(View view){
            fishName = (TextView) view.findViewById(R.id.fishName);
        }
    }

    public ArrayList<FishesInAqua> getFishesInAquaArray() {
        return fishesInAqua;
    }
}*/
