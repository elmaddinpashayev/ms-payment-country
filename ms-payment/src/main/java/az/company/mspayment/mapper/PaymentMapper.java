package az.company.mspayment.mapper;

import az.company.mspayment.model.entity.Payment;
import az.company.mspayment.model.request.PaymentRequest;
import az.company.mspayment.model.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class PaymentMapper {

    public static Payment mapRequestToEntity(PaymentRequest request) {
        return Payment.builder()
                .amount(request.getAmount())
                .description(request.getDescrtiption())
                .build();
    }

    public static PaymentResponse mapEntityToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .responseAt(LocalDateTime.now())
                .build();
    }
}
