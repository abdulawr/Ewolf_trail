package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Activities.View_Post_Details;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Buy_Pets_Adapter extends RecyclerView.Adapter<Buy_Pets_Adapter.View_Holder> {

    Context context;
    List<Sell_Pets_Container> list;

    public Buy_Pets_Adapter(Context context, List<Sell_Pets_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Buy_Pets_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_pets_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {
     Sell_Pets_Container data=list.get(position);
     Picasso.get().load(BaseURL.PostUrl()+data.getImage()).into(holder.image);
        holder.name.setText(data.getName());
        holder.bread.setText(data.getBread());
        holder.price.setText(data.getPrice() + " PKR");
        holder.gender.setText(data.getGender());
        holder.date.setText(data.getAdded_date());
        holder.age.setText(data.getAge());

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, View_Post_Details.class);
                intent.putExtra("name",data.getName());
                intent.putExtra("id",data.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView image;
        Button details;
        TextView name,bread,price,gender,date,age;

        public View_Holder(@NonNull @NotNull View v) {
            super(v);
            image=v.findViewById(R.id.image);
            details=v.findViewById(R.id.details);
            age=v.findViewById(R.id.age);
            name=v.findViewById(R.id.name);
            bread=v.findViewById(R.id.bread);
            price=v.findViewById(R.id.price);
            gender=v.findViewById(R.id.gender);
            date=v.findViewById(R.id.date);
        }

    }
}
