package com.fiuba.tdpii.correapp.models.local;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {

    public String nombre;

    public String tipo;

    public String size;

    public String obs;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.tipo);
        dest.writeString(this.size);
        dest.writeString(this.obs);
    }

    public Pet() {
    }

    protected Pet(Parcel in) {
        this.nombre = in.readString();
        this.tipo = in.readString();
        this.size = in.readString();
        this.obs = in.readString();
    }

    public static final Parcelable.Creator<Pet> CREATOR = new Parcelable.Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel source) {
            return new Pet(source);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };
}
