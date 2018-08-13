package com.nvisio.video.videostreamsample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.model.demoModel;

import java.util.List;

public class DemoAdapetr extends RecyclerView.Adapter<DemoAdapetr.ViewHolder> {
private List<demoModel> data;

    public DemoAdapetr(List<demoModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DemoAdapetr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_single, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
