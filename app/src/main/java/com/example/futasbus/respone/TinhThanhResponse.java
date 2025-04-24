package com.example.futasbus.respone;

import com.example.futasbus.model.TinhThanh;

import java.util.List;

public class TinhThanhResponse {
    private List<TinhThanh> tinhThanhList;

    public List<TinhThanh> getTinhThanhList() {
        return tinhThanhList;
    }

    public void setTinhThanhList(List<TinhThanh> tinhThanhList) {
        this.tinhThanhList = tinhThanhList;
    }
}
