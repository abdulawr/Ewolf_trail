package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Activities.Chat_Width;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Container.Chat_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Chat_List_Adapter extends RecyclerView.Adapter<Chat_List_Adapter.View_Holder> {

    List<Chat_Container> list;
    Context context;

    public Chat_List_Adapter(List<Chat_Container> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Chat_List_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {
      Chat_Container data=list.get(position);
      holder.name.setText(data.getName());
      holder.mobile.setText("0"+data.getMobile());
        Picasso.get().load(BaseURL.userImage()+data.getImage()).into(holder.image);
        if(data.getStatus().trim().equals("1")){
            holder.notif.setVisibility(View.VISIBLE);
        }
        else{
            holder.notif.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name,mobile,notif;
        public View_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            mobile=itemView.findViewById(R.id.mobile);
            notif=itemView.findViewById(R.id.notif);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context, Chat_Width.class);
            intent.putExtra("obj",list.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }
}
