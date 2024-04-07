package com.conversioncurrency.requestDtos;

public class CreateCurrencyRequestDto {
    private final String code;
    private final String name;
    private final String sign;

    public CreateCurrencyRequestDto(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }
}
