package com.androidfizz.imagepickerlibrary;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    private String imageName, folderName, path;
    private boolean isSelected = false;
    private static int maxImage;

    public ImageModel(String imageName, String folderName, String path) {
        this.imageName = imageName;
        this.folderName = folderName;
        this.path = path;
    }


    protected ImageModel(Parcel in) {
        imageName = in.readString();
        folderName = in.readString();
        path = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getImageName() {
        return imageName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getPath() {
        return path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public static int getMaxImage() {
        return maxImage;
    }

    public static void setMaxImage(int maxImage) {
        ImageModel.maxImage = maxImage;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "imageName='" + imageName + '\'' +
                ", folderName='" + folderName + '\'' +
                ", path='" + path + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageName);
        parcel.writeString(folderName);
        parcel.writeString(path);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImageModel && ((ImageModel) obj).path.equalsIgnoreCase(this.path);
    }

    @Override
    public int hashCode() {
        return this.path != null ? path.hashCode() * 3 : 0;
    }
}
