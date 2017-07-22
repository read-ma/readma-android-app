package com.plumya.readma.data.model;

/**
 * Created by miltomasz on 15/07/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miltomasz on 12/06/16.
 */

public class Article implements Parcelable {
    public long id;
    public String title;
    @SerializedName("source_url")
    public String sourceUrl;
    @SerializedName("content_type")
    public String contentType;
    public String status;
    @SerializedName("created_at")
    public String createdAt;
    public String difficulty;
    public String content;
    @SerializedName("admin_user")
    public String adminUserId;
    public String image;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.sourceUrl);
        dest.writeString(this.contentType);
        dest.writeString(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.difficulty);
        dest.writeString(this.content);
        dest.writeString(this.adminUserId);
        dest.writeString(this.image);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.sourceUrl = in.readString();
        this.contentType = in.readString();
        this.status = in.readString();
        this.createdAt = in.readString();
        this.difficulty = in.readString();
        this.content = in.readString();
        this.adminUserId = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

