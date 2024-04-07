package com.conversioncurrency.mappers;

import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.responseDtos.CalculatedExchangeRateDto;
import com.conversioncurrency.responseDtos.GetCalculatedExchangeRateResponseDto;
import com.conversioncurrency.responseDtos.GetExchangeRateResponseDto;
import com.conversioncurrency.utils.CurrencyUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {
    ExchangeRateMapper MAPPER = Mappers.getMapper(ExchangeRateMapper.class);
    @Mapping(target = "rate", source = "rate", qualifiedByName = "centsToDecimal")
    GetExchangeRateResponseDto exchangeRateToResponse(ExchangeRate exchangeRate);
    GetCalculatedExchangeRateResponseDto calculatedRateToResponse(CalculatedExchangeRateDto calculatedExchangeRateDto);

    @Named("centsToDecimal")
    default BigDecimal centsToDecimal(int cents) {
        return CurrencyUtils.centsToDecimal(cents);
    }
}
