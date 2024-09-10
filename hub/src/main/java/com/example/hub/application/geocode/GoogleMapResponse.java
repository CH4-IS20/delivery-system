package com.example.hub.application.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

//
public class GoogleMapResponse {
    @JsonProperty("results")        // 구글 MAP에서 반환하는 Json형식의 key값을 일치키기기 위해 Json명을 지정해줌
    private Result[] result;

    public Result[] getResult() {
        return result;
    }

    public void setResult(Result[] result) {
        this.result = result;
    }

}