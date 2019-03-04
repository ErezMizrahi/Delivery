package com.erez8.gymko.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Business implements Parcelable {

    private String title;
    private String mBusiness_id;


    public Business(String title, String mBusiness_id) {
        this.title = title;
        this.mBusiness_id = mBusiness_id;
    }

    public Business() {

    }

    protected Business(Parcel in) {
        title = in.readString();
        mBusiness_id = in.readString();
    }

    public static final Creator<Business> CREATOR = new Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel in) {
            return new Business(in);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getmBusiness_id() {
        return mBusiness_id;
    }

    public void setmBusiness_id(String mBusiness_id) {
        this.mBusiness_id = mBusiness_id;
    }

    @Override
    public String toString() {
        return "Business{" +
                "title='" + title + '\'' +
                ", mBusiness_id='" + mBusiness_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(mBusiness_id);
    }
}
