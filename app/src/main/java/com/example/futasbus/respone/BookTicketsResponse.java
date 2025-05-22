package com.example.futasbus.respone;

import com.example.futasbus.model.ViTriGhe;

import java.util.List;

public class BookTicketsResponse {
    private String departureId;
    private String departure;
    private String destinationId;
    private String destination;
    private String departureDate;
    private String returnDate;
    private String start;
    private String end;
    private int idTrip;
    private String startTime;
    private String endTime;
    private String loai;
    private double price;
    private int soGhe;
    private int idXe;
    private int idTripReturn;
    private String startTimeReturn;
    private String endTimeReturn;
    private double priceReturn;
    private int soGheReturn;
    private int idXeReturn;
    private String biensoxeGo;
    private String biensoxeReturn;

    private List<ViTriGhe> viTriGheTangDuoiList;
    private List<ViTriGhe> viTriGheTangTrenList;
    private List<ViTriGhe> viTriGheTangDuoiReturnList;
    private List<ViTriGhe> viTriGheTangTrenReturnList;

    public String getDepartureId() {
        return departureId;
    }

    public void setDepartureId(String departureId) {
        this.departureId = departureId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public int getIdXe() {
        return idXe;
    }

    public void setIdXe(int idXe) {
        this.idXe = idXe;
    }

    public int getIdTripReturn() {
        return idTripReturn;
    }

    public void setIdTripReturn(int idTripReturn) {
        this.idTripReturn = idTripReturn;
    }

    public String getStartTimeReturn() {
        return startTimeReturn;
    }

    public void setStartTimeReturn(String startTimeReturn) {
        this.startTimeReturn = startTimeReturn;
    }

    public String getEndTimeReturn() {
        return endTimeReturn;
    }

    public void setEndTimeReturn(String endTimeReturn) {
        this.endTimeReturn = endTimeReturn;
    }

    public double getPriceReturn() {
        return priceReturn;
    }

    public void setPriceReturn(double priceReturn) {
        this.priceReturn = priceReturn;
    }

    public int getSoGheReturn() {
        return soGheReturn;
    }

    public void setSoGheReturn(int soGheReturn) {
        this.soGheReturn = soGheReturn;
    }

    public int getIdXeReturn() {
        return idXeReturn;
    }

    public void setIdXeReturn(int idXeReturn) {
        this.idXeReturn = idXeReturn;
    }

    public String getBiensoxeGo() {
        return biensoxeGo;
    }

    public void setBiensoxeGo(String biensoxeGo) {
        this.biensoxeGo = biensoxeGo;
    }

    public String getBiensoxeReturn() {
        return biensoxeReturn;
    }

    public void setBiensoxeReturn(String biensoxeReturn) {
        this.biensoxeReturn = biensoxeReturn;
    }

    public List<ViTriGhe> getViTriGheTangDuoiList() {
        return viTriGheTangDuoiList;
    }

    public void setViTriGheTangDuoiList(List<ViTriGhe> viTriGheTangDuoiList) {
        this.viTriGheTangDuoiList = viTriGheTangDuoiList;
    }

    public List<ViTriGhe> getViTriGheTangTrenList() {
        return viTriGheTangTrenList;
    }

    public void setViTriGheTangTrenList(List<ViTriGhe> viTriGheTangTrenList) {
        this.viTriGheTangTrenList = viTriGheTangTrenList;
    }

    public List<ViTriGhe> getViTriGheTangDuoiReturnList() {
        return viTriGheTangDuoiReturnList;
    }

    public void setViTriGheTangDuoiReturnList(List<ViTriGhe> viTriGheTangDuoiReturnList) {
        this.viTriGheTangDuoiReturnList = viTriGheTangDuoiReturnList;
    }

    public List<ViTriGhe> getViTriGheTangTrenReturnList() {
        return viTriGheTangTrenReturnList;
    }

    public void setViTriGheTangTrenReturnList(List<ViTriGhe> viTriGheTangTrenReturnList) {
        this.viTriGheTangTrenReturnList = viTriGheTangTrenReturnList;
    }
}
