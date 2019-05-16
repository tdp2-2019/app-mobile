package com.fiuba.tdpii.correapp.models.local;

import android.os.Parcel;
import android.os.Parcelable;

public class PetLocal implements Parcelable {

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

    public PetLocal() {
    }

    protected PetLocal(Parcel in) {
        this.nombre = in.readString();
        this.tipo = in.readString();
        this.size = in.readString();
        this.obs = in.readString();
    }

    public static final Parcelable.Creator<PetLocal> CREATOR = new Parcelable.Creator<PetLocal>() {
        @Override
        public PetLocal createFromParcel(Parcel source) {
            return new PetLocal(source);
        }

        @Override
        public PetLocal[] newArray(int size) {
            return new PetLocal[size];
        }
    };
}
