package az.company.mspayment.service;

import az.company.mspayment.client.CountryClient;
import az.company.mspayment.exception.NotFoundException;
import az.company.mspayment.mapper.PaymentMapper;
import az.company.mspayment.model.entity.Payment;
import az.company.mspayment.model.request.PaymentRequest;
import az.company.mspayment.model.response.PaymentResponse;
import az.company.mspayment.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.company.mspayment.mapper.PaymentMapper.mapEntityToResponse;
import static az.company.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static az.company.mspayment.model.constant.ExceptionConstant.COUNTRY_NOT_FOUND_CODE;
import static az.company.mspayment.model.constant.ExceptionConstant.COUNTRY_NOT_FOUNT_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepo paymentRepo;

    private final CountryClient countryClient;

    public void savePayment(PaymentRequest request) {
        log.info("savePayment.started");
        countryClient.getAllAvailableCountries(request.getCurrency())
                .stream()
                .filter(country -> country.getRemaningLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(COUNTRY_NOT_FOUND_CODE, request.getAmount(),
                        request.getCurrency()), COUNTRY_NOT_FOUNT_MESSAGE));
        paymentRepo.save(mapRequestToEntity(request));
        log.info("savePayment.success");
    }


    public PaymentResponse getPaymentById(Long id) {
        log.info("getPaymentById.start id:{}", id);
        Payment payment = fetchPaymentIfExists(id);
        log.info("getPaymentById.success id:{}", id);
        return mapEntityToResponse(payment);
    }

    public List<PaymentResponse> getAllPayment() {
        return paymentRepo.findAll()
                .stream()
                .map(PaymentMapper::mapEntityToResponse).collect(Collectors.toList());
    }

    public void deletePaymentById(Long id) {
        log.info("deletePayment.start id:{}", id);
        Payment payment = fetchPaymentIfExists(id);
        paymentRepo.deleteById(id);
        log.info("deletePayment.success id:{}", id);
    }

    public void updatePayment(Long id, PaymentRequest request) {
        log.info("updatePayment.success id:{}", id);
        Payment payment = fetchPaymentIfExists(id);
        payment.setDescription(request.getDescrtiption());
        payment.setAmount(request.getAmount());
        paymentRepo.save(payment);
        log.info("updatePayment.success id:{}", id);

    }

    private Payment fetchPaymentIfExists(Long id) {
        return paymentRepo.findById(id).orElseThrow(RuntimeException::new);
    }
}
