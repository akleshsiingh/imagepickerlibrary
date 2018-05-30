package com.androidfizz.imagepickerlibrary;

import java.util.List;

public interface IOnFilesSelected {

    void onSelected(List<ImageModel> result);
    void onFail(String error);
}
