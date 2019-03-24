package com.lazy.bike.pojo;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:28
 */
public class Bike {
    private Long id;

    private int status;

    private String qrCode;

    private Double latitude;

    private Double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "Bike{" +
                "id=" + id +
                ", status=" + status +
                ", qrCode='" + qrCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
