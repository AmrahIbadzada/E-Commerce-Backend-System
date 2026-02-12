package com.payment.mypayment_ms.mapper;

import com.payment.mypayment_ms.dto.req.PaymentRequestDto;
import com.payment.mypayment_ms.entity.PaymentHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPaymentHistoryMapper {

    PaymentHistoryEntity mapDtoToEntity(PaymentRequestDto paymentRequestDto);
}
