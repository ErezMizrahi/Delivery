package com.erez8.gymko.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Delivery implements Parcelable {
    private String what_to_deliver , location_of_delivery, time_of_arrival;

    public Delivery(String what_to_deliver, String location_of_delivery, String time_of_arrival) {
        this.what_to_deliver = what_to_deliver;
        this.location_of_delivery = location_of_delivery;
        this.time_of_arrival = time_of_arrival;
    }

    public Delivery() {

    }


    public static Creator<Delivery> getCREATOR() {
        return CREATOR;
    }

    public String getWhat_to_deliver() {
        return what_to_deliver;
    }

    public void setWhat_to_deliver(String what_to_deliver) {
        this.what_to_deliver = what_to_deliver;
    }

    public String getLocation_of_delivery() {
        return location_of_delivery;
    }

    public void setLocation_of_delivery(String location_of_delivery) {
        this.location_of_delivery = location_of_delivery;
    }

    public String getTime_of_arrival() {
        return time_of_arrival;
    }

    public void setTime_of_arrival(String time_of_arrival) {
        this.time_of_arrival = time_of_arrival;
    }

    protected Delivery(Parcel in) {
        what_to_deliver = in.readString();
        location_of_delivery = in.readString();
        time_of_arrival = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(what_to_deliver);
        dest.writeString(location_of_delivery);
        dest.writeString(time_of_arrival);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Delivery> CREATOR = new Creator<Delivery>() {
        @Override
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        @Override
        public Delivery[] newArray(int size) {
            return new Delivery[size];
        }
    };
}
