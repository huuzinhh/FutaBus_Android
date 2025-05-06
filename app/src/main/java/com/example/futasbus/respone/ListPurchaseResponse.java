package com.example.futasbus.respone;

import java.util.List;

public class ListPurchaseResponse {
    private List<PurchaseResponse> data;
    private boolean success;

    public List<PurchaseResponse> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
