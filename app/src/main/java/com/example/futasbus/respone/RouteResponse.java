package com.example.futasbus.respone;

import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.model.TuyenXe;

import java.util.List;

public class RouteResponse {
    private List<TuyenXe> tuyenXeList;

    public List<TuyenXe> getTuyenXeList() {
        return tuyenXeList;
    }

    public void setTuyenXeList(List<TuyenXe> tuyenXeList) {
        this.tuyenXeList = tuyenXeList;
    }
}
