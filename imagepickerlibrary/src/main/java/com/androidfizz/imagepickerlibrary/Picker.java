package com.androidfizz.imagepickerlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public final class Picker {

    private Picker() {

    }

    private static Builder builder = null;

    public static synchronized Builder with(Context context) {
        if (builder == null)
            builder = new Builder(context);
        return builder;
    }

    private void showFiles(Builder builder) {
        builder.context.startActivity(new Intent(builder.context, MainImageActivity.class)
                .putExtra(ImageSelectionFragment.MAX_IMAGE, builder.imageCount));
    }

    public static void showFiles2(List<ImageModel> result) {
        builder.onFilesSelected.onSelected(result);
    }


    public final static class Builder {
        private Context context;
        private IOnFilesSelected onFilesSelected;
        private int imageCount;

        Builder(Context context) {
            this.context = context;
        }

        public Builder maxImage(int imageCount) {
            this.imageCount = imageCount;
            return this;
        }

        public Builder fileSelected(IOnFilesSelected onFilesSelected) {
            this.onFilesSelected = onFilesSelected;
            return this;
        }


        public void build() {
            new Picker().showFiles(this);
        }
    }


}
