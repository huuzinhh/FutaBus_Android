package com.example.futasbus.respone;

import java.io.Serializable;
import java.util.Objects;

public class SelectedSeat implements Serializable {
    private int idViTri;
    private String tenViTri;

    public SelectedSeat(int idViTri, String tenViTri) {
        this.idViTri = idViTri;
        this.tenViTri = tenViTri;
    }

    public int getIdViTri() {
        return idViTri;
    }

    public String getTenViTri() {
        return tenViTri;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SelectedSeat other = (SelectedSeat) obj;
        return idViTri == other.idViTri && tenViTri.equals(other.tenViTri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idViTri, tenViTri);
    }

}
