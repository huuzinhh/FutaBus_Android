    package com.example.futasbus.respone;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PurchaseItemResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private PurchaseItem data;

    public boolean isSuccess() { return success; }
    public PurchaseItem getData() { return data; }
}

