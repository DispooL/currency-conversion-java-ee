package com.conversioncurrency.responseDtos;

public class GetCurrencyResponseDto {
    private final int id;
    private final String name;
    private final String code;
    private final String sign;
    private final String createdAt;

    public GetCurrencyResponseDto(int id, String name, String code, String sign, String createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() { return sign; }

    public String getCreatedAt() { return createdAt; }
}
