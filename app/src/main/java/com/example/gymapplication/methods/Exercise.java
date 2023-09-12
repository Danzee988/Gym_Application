package com.example.gymapplication.methods;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name,id;
    private boolean isSelected, showRepsSetsWeight;
    private int imageResource;
    private Integer sets, reps, weight;

    public Exercise(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
        this.sets = null;  // Initialize as null
        this.reps = null;  // Initialize as null
        this.weight = null; // Initialize as null
    }

    public Exercise(String name) {
        this.name = name;
        this.imageResource = imageResource;
        this.sets = null;  // Initialize as null
        this.reps = null;  // Initialize as null
        this.weight = null; // Initialize as null
    }

    public Exercise(String name, int sets, int reps, int weight, String id, int imageResource) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.id = id;
        this.imageResource = imageResource;
    }
    public Exercise() {

    }

    protected Exercise(Parcel in) {
        name = in.readString();
        isSelected = in.readByte() != 0;
        imageResource = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    //-----------------------------Getters-------------------------------
    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public Integer getSets() {
        return sets;
    }

    public Integer getReps() {
        return reps;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getId() {
        return id;
    }

    //-----------------------------Setters-------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setId(String id) {
        this.id = id;
    }

    //-----------------------------Other-------------------------------
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(imageResource);
    }

    public boolean isShowRepsSetsWeight() {
        return showRepsSetsWeight;
    }

    public void setShowRepsSetsWeight(boolean showRepsSetsWeight) {
        this.showRepsSetsWeight = showRepsSetsWeight;
    }

    public String toString() {
        return name;
    }
}
