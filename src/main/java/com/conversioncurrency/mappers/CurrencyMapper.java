package com.conversioncurrency.mappers;

import com.conversioncurrency.models.Currency;
import com.conversioncurrency.responseDtos.GetCurrencyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper MAPPER = Mappers.getMapper(CurrencyMapper.class);

    GetCurrencyResponseDto currencyToGetCurrencyResponse(Currency currency);
}
