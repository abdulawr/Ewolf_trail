package com.ss_technology.mushinprojectandroidapp.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Container.Train_Gif_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class Train_Gif_Adapter extends RecyclerView.Adapter<Train_Gif_Adapter.View_Holder> {

    Context context;
    List<Train_Gif_Container> list;
    ClickGIFlistner clickGIFlistner;

    public Train_Gif_Adapter(Context context, List<Train_Gif_Container> list,ClickGIFlistner clickGIFlistner) {
        this.context = context;
        this.list = list;
        this.clickGIFlistner=clickGIFlistner;
    }

    @NonNull
    @NotNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.train_gif_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull View_Holder holder, int position) {
     Train_Gif_Container container=list.get(position);
     holder.name.setText(container.getName());

     if(container.getType().trim().equals("mp4")){
         holder.imageView.setVisibility(View.GONE);
         holder.vidview.setVisibility(View.VISIBLE);

         try{
             Uri video = Uri.parse(BaseURL.GifUrl()+container.getGif_path());
             MediaController mediaController = new MediaController(context);
             holder.vidview.setMediaController(mediaController);
             holder.vidview.setVideoURI(video);
             holder.vidview.requestFocus();
             holder.vidview.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
             {

                 public void onPrepared(MediaPlayer mp)
                 {

                     holder.vidview.start();
                 }
             });
         }
         catch (Exception e){
             Log.e("Basit","Video exception");
         }



     }
     else{
         holder.imageView.setVisibility(View.VISIBLE);
         holder.vidview.setVisibility(View.GONE);
         Glide.with(context).load(BaseURL.GifUrl()+container.getGif_path()) .into(holder.imageView);

     }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView imageView;
        VideoView vidview;
        public View_Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
            vidview=itemView.findViewById(R.id.vidview);
        }

        @Override
        public void onClick(View view) {
            if(!list.get(getLayoutPosition()).getType().trim().equals("mp4"))
            clickGIFlistner.GIF_url(list.get(getLayoutPosition()).getGif_path());
        }
    }

    public interface ClickGIFlistner{
        void GIF_url(String url);
    }
}
