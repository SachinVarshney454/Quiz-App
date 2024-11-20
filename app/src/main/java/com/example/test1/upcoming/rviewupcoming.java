package com.example.test1.upcoming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;

import java.util.ArrayList;

public class rviewupcoming extends RecyclerView.Adapter<rviewupcoming.ViewHolder> {
    public rviewupcoming( ArrayList<modelupcoming> data) {

        this.data = data;
    }

    ArrayList<modelupcoming>data ;
    @NonNull
    @Override
    public rviewupcoming.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_upcominglayout,parent,false);
        ViewHolder view  = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull rviewupcoming.ViewHolder holder, int position) {
        holder.date.setText(data.get(position).data);
        holder.time.setText(data.get(position).time);
        holder.author.setText(data.get(position).author);
        holder.name.setText(data.get(position).name);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,author,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            author=itemView.findViewById(R.id.author);
            name=itemView.findViewById(R.id.name);
        }
    }
}
