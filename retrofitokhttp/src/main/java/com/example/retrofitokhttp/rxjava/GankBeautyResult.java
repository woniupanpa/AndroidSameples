package com.example.retrofitokhttp.rxjava;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yanjim
 * @Date 2022/1/10 09:58
 */
public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results")
    List<GankBeauty> beauties;
}
