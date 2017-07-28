package com.plumya.readma.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miltomasz on 26/07/17.
 */

public class ArticleDetail {
    public long id;
    public String[][] title;
    @SerializedName("text")
    public String content;
}
