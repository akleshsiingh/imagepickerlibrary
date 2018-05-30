package com.androidfizz.imagepickerlibrary;


import android.content.res.Resources;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainImageActivity extends AppCompatActivity implements ImageSelectionFragment.OnSelectionDone {

    private ImageSelectionAdapter mAdapter;
    private int MAX_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_image);

        findViewById(R.id.ivBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.tvDone);

        MAX_IMAGE = getIntent().getIntExtra(ImageSelectionFragment.MAX_IMAGE, 1);

        RecyclerView mList = findViewById(R.id.mList);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageSelectionAdapter();
        mList.setAdapter(mAdapter);


        new Thread(() -> {
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            getAllImages(root);
            runOnUiThread(() -> mAdapter.setItems(mHashList));
        }).start();
    }



    private Map<String, List<ImageModel>> mHashList = new HashMap<>();

    private void getAllImages(File rootn) {

        ArrayList<ImageModel> list = new ArrayList<>();

        File[] listFiles = rootn.listFiles();
        if (listFiles == null)
            return;
        for (File single : listFiles) {
            if (single.isDirectory()) {
                getAllImages(single);
            } else {
                if (single.getName().endsWith(".jpg") || single.getName().endsWith(".jpeg")) {
                    list.add(new ImageModel(single.getName(), rootn.getName(), single.getAbsolutePath()));
                }
            }

        }
        if (list.size() > 0) {
            mHashList.put(rootn.getName(), list);
        }
    }

    @Override
    public void onDone(List<ImageModel> mImageList) {
        Picker.showFiles2(mImageList);
    }

    private class ImageSelectionAdapter extends RecyclerView.Adapter<ImageSelectionAdapter.MyViewHolder> {

        private List<String> folderList;
        private Map<String, List<ImageModel>> mHashList;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_row_folder, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String foldername = folderList.get(position);
            final ArrayList<ImageModel> single = (ArrayList<ImageModel>) mHashList.get(foldername);
            holder.tvName.setText(foldername);
            holder.tvTotal.setText("" + single.size());
            setImage(holder.ivImage, single.get(0).getPath());
            holder.itemView.setOnClickListener(view -> {
                Fragment imageSelectFragment = ImageSelectionFragment.getInstance(MAX_IMAGE, single);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, imageSelectFragment, ImageSelectionFragment.TAG)
                        .addToBackStack(ImageSelectionFragment.TAG).commit();
            });
        }

        private void setImage(ImageView ivImage, String path) {


            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide
                    .with(ivImage.getContext())
                    .load(new File(path))
                    .apply(requestOptions)
                    .into(ivImage);
        }

        @Override
        public int getItemCount() {
            return folderList != null ? folderList.size() : 0;
        }

        private void setItems(Map<String, List<ImageModel>> mHashList) {

            folderList = new ArrayList<>(mHashList.keySet());
            this.mHashList = mHashList;
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvName, tvTotal;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
                itemView.getLayoutParams().height = (int) ((float) getScreenWidth() / 2.8);
            }
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
