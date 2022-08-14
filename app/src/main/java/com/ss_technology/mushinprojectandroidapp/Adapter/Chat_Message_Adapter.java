package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.mushinprojectandroidapp.Container.Chat_Message_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Chat_Message_Adapter extends RecyclerView.Adapter<Chat_Message_Adapter.View_Holder> {

    List<Chat_Message_Container> list;

    public Chat_Message_Adapter(List<Chat_Message_Container> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Chat_Message_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messages_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {
     Chat_Message_Container data=list.get(position);
     if(data.getSender_id().trim().equals("1")){
         holder.sender.setVisibility(View.VISIBLE);
         holder.receiver.setVisibility(View.GONE);
         holder.sender_message.setText(data.getMessage());
     }
     else{
         holder.sender.setVisibility(View.GONE);
         holder.receiver.setVisibility(View.VISIBLE);
         holder.receiver_message.setText(data.getMessage());
     }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        CardView sender,receiver;
        TextView sender_message,receiver_message;

        public View_Holder(@NonNull @NotNull View itemView) {
            super(itemView);

            sender=itemView.findViewById(R.id.sender);
            receiver=itemView.findViewById(R.id.receiver);
            sender_message=itemView.findViewById(R.id.sender_message);
            receiver_message=itemView.findViewById(R.id.receiver_message);
        }
    }
}
