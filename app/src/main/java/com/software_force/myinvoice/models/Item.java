package com.software_force.myinvoice.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Matthew So on 10/4/2015.
 */
public class Item implements Parcelable {
    private String code;
    private String description;
    private BigDecimal price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.description);
        dest.writeString(this.price.toPlainString());
    }

    public Item(){

    }

    public Item(Parcel in){
        this.code =  in.readString();
        this.description =  in.readString();
        this.price =  new BigDecimal( in.readString());
    }

    // Just cut and paste this for now
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}