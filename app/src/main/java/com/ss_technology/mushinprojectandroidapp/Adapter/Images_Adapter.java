package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.View_Holder> {

    List<String> list;
    OnImgClick onImgClick;

    public Images_Adapter(List<String> list, OnImgClick onImgClick) {
        this.list = list;
        this.onImgClick=onImgClick;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Images_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {
        Picasso.get().load(BaseURL.PostUrl()+list.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        public View_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onImgClick.getUrl(list.get(getLayoutPosition()));
        }
    }

    public interface OnImgClick{
        void getUrl(String url);
    }
}
