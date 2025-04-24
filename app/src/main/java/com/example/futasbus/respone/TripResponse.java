package com.example.futasbus.respone;

import com.example.futasbus.model.ChuyenXeResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripResponse {
    @SerializedName("chuyenXeResultList")
    private List<ChuyenXeResult> chuyenXeResultList; // Chiều đi

    @SerializedName("chuyenXeReturnList")
    private List<ChuyenXeResult> chuyenXeReturnList; // Chiều về

    public List<ChuyenXeResult> getChuyenXeResultList() {
        return chuyenXeResultList;
    }

    public List<ChuyenXeResult> getChuyenXeReturnList() {
        return chuyenXeReturnList;
    }
}
