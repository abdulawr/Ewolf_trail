package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Your_Sell_Pets_Post_Adapter extends RecyclerView.Adapter<Your_Sell_Pets_Post_Adapter.View_Holder> {

    Context context;
    List<Sell_Pets_Container> list;
    Delete_Click delete_click;
    boolean ch;

    public Your_Sell_Pets_Post_Adapter(Context context,boolean ch,List<Sell_Pets_Container> list,Delete_Click delete_click) {
        this.context = context;
        this.list = list;
        this.delete_click=delete_click;
        this.ch=ch;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Your_Sell_Pets_Post_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.your_sell_pets_post_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {

      Sell_Pets_Container data=list.get(position);
      try
      {
          if(!TextUtils.isEmpty(data.getMessage())){
              holder.status_text.setVisibility(View.GONE);
              holder.delete_btn.setVisibility(View.GONE);
              holder.message.setVisibility(View.VISIBLE);
              holder.message.setText(data.getMessage());
          }
          else{
              if (ch){
                  holder.message.setVisibility(View.GONE);
                  Picasso.get().load(BaseURL.PostUrl()+data.getImage()).into(holder.image);
                  holder.price.setVisibility(View.VISIBLE);
                  if (data.getStatus().trim().equals("1")){
                      holder.status_text.setText("Active");
                  }
                  else {
                      holder.status_text.setText("Pending");
                  }
              }
              else{
                  holder.message.setVisibility(View.GONE);
                  holder.price.setVisibility(View.GONE);
                  Picasso.get().load(BaseURL.PetUrl()+data.getImage()).into(holder.image);
              }
          }
      }
      catch (Exception e){
         // the message is not set
          if (ch){
              holder.message.setVisibility(View.GONE);
              Picasso.get().load(BaseURL.PostUrl()+data.getImage()).into(holder.image);
              holder.price.setVisibility(View.VISIBLE);
              if (data.getStatus().trim().equals("1")){
                  holder.status_text.setText("Active");
              }
              else {
                  holder.status_text.setText("Pending");
              }
          }
          else{
              holder.message.setVisibility(View.GONE);
              holder.price.setVisibility(View.GONE);
              Picasso.get().load(BaseURL.PetUrl()+data.getImage()).into(holder.image);
          }
      }


        holder.name.setText(data.getName());
        holder.bread.setText(data.getBread());
        holder.price.setText(data.getPrice() + " PKR");
        holder.gender.setText(data.getGender());
        holder.date.setText(data.getAdded_date());

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               delete_click.onclick(data.getId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView image;
        Button delete_btn;
        TextView status_text,name,bread,price,gender,date,message;

        public View_Holder(@NonNull @NotNull View v) {
            super(v);
            image=v.findViewById(R.id.image);
            delete_btn=v.findViewById(R.id.delete_btn);
            status_text=v.findViewById(R.id.status_text);
            name=v.findViewById(R.id.name);
            bread=v.findViewById(R.id.bread);
            price=v.findViewById(R.id.price);
            gender=v.findViewById(R.id.gender);
            date=v.findViewById(R.id.date);
            message=v.findViewById(R.id.message);
        }
    }

    public interface Delete_Click{
        void onclick(String post_id,int position);
    }
}
