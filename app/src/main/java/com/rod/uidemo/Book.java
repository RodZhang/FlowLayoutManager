package com.rod.uidemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Rod
 * @date 2019/3/8
 */
public class Book implements Parcelable {

    public String name;

    public Book(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Book(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void readFromParcel(Parcel reply) {
        name = reply.readString();
    }
}
