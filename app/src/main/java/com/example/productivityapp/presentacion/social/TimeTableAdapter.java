package com.example.productivityapp.presentacion.social;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.productivityapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.model.TimeData;
import com.example.productivityapp.model.TimeDataFormatted;

import java.util.List;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {

    private List<TimeDataFormatted> timeDataList;

    public TimeTableAdapter(List<TimeDataFormatted> timeDataList) {
        this.timeDataList = timeDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeDataFormatted timeData = timeDataList.get(position);

        holder.emailTextView.setText(timeData.getEmail());
        holder.secondsTextView.setText(String.valueOf(timeData.getSeconds()));
    }

    @Override
    public int getItemCount() {
        return timeDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        TextView secondsTextView;

        ViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            secondsTextView = itemView.findViewById(R.id.secondsTextView);
        }
    }
}

