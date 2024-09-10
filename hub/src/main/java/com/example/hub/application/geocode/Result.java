package com.example.hub.application.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

// Result Dto
public class Result {
    @JsonProperty("formatted_address")      // Google Map api Json 반환 키값과 일치 시켜주기 위해(따로 사용안하고 싶으면 변수명을 맞춰주면 됨)
    private String address;
    //    @JsonProperty("geometry")
    private Geometry geometry;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}