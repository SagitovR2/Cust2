package com.example.cust;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class FishAdapter extends RecyclerView.Adapter<FishAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<FishesInAqua> fishesInAqua;
    Context context;

    FishAdapter(Context context, ArrayList<FishesInAqua> fishes) {
        this.fishesInAqua = fishes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public FishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button deleteButton;
        final TextView fishName;
        final EditText maleCount, femaleCount, fryCount;
        ViewHolder(View view){
            super(view);
            deleteButton = (Button) view.findViewById(R.id.addButtonAllFishes);
            fishName = (TextView) view.findViewById(R.id.fishName);
            maleCount = (EditText) view.findViewById(R.id.maleCount);
            femaleCount = (EditText) view.findViewById(R.id.femaleCount);
            fryCount = (EditText) view.findViewById(R.id.fryCount);
        }
    }

    @Override
    public void onBindViewHolder(FishAdapter.ViewHolder holder, int position) {
        FishesInAqua fish = fishesInAqua.get(position);
        holder.fishName.setText(fish.getName());
        holder.femaleCount.setText(Integer.toString(fish.getCountOfFemales()));
        holder.femaleCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    holder.femaleCount.setText("0");
                }
                else {
                    fishesInAqua.get(position).setCountOfFemales(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.maleCount.setText(Integer.toString(fish.getCountOfMales()));
        holder.maleCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    holder.maleCount.setText("0");
                }
                else {
                    fishesInAqua.get(position).setCountOfMales(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.fryCount.setText(Integer.toString(fish.getCountOfFries()));
        holder.fryCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    holder.fryCount.setText("0");
                }
                else {
                    fishesInAqua.get(position).setCountOfFries(Integer.parseInt(s.toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return fishesInAqua.size();
    }

    public ArrayList<FishesInAqua> getFishesInAqua() {
        return fishesInAqua;
    }
}
