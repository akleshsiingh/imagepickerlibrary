package com.androidfizz.imagepickerlibrary;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.MyViewHolder> {
    private final RequestOptions requestOptions;
    private List<ImageModel> imageList;
    private IImagSelector listner;
    private int totalImageCount = 0;

    AdapterImage(List<ImageModel> imageList, IImagSelector listner) {
        this.imageList = imageList;
        this.listner = listner;
        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_image_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ImageModel single = imageList.get(position);

        setImage(holder.ivImage, requestOptions, single);
        if (single.isSelected()) {
            holder.ivImage.setAlpha(60);
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
            holder.ivImage.setAlpha(255);
        }
        if (listner != null)
            holder.itemView.setOnClickListener(v -> {
                listner.onClick(single, position);

                if (single.isSelected()) {
                    --totalImageCount;
                    single.setSelected(false);
                    holder.itemView.setSelected(false);
                    holder.ivImage.setAlpha(255);
                } else if (totalImageCount < ImageModel.getMaxImage()) {
                    ++totalImageCount;
                    holder.ivImage.setAlpha(60);
                    single.setSelected(true);
                    holder.itemView.setSelected(true);
                }

            });
    }

    private void setImage(ImageView ivImage, RequestOptions requestOptions, ImageModel single) {
        Glide
                .with(ivImage.getContext())
                .load(new File(single.getPath()))
                .apply(requestOptions)
                .into(ivImage);
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            itemView.getLayoutParams().height = (int) ((float) getScreenWidth() / 2.9);
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
