package az.company.mspayment.service;

import az.company.mspayment.client.CountryClient;
import az.company.mspayment.exception.NotFoundException;
import az.company.mspayment.mapper.PaymentMapper;
import az.company.mspayment.model.constant.ExceptionConstant;
import az.company.mspayment.model.entity.Payment;
import az.company.mspayment.model.request.PaymentRequest;
import az.company.mspayment.model.response.PageablePaymentResponse;
import az.company.mspayment.model.response.PaymentResponse;
import az.company.mspayment.repository.PaymentRepo;
import az.company.mspayment.service.specificarion.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import az.company.mspayment.model.request.PaymentCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.company.mspayment.mapper.PaymentMapper.mapEntityToResponse;
import static az.company.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static az.company.mspayment.model.constant.ExceptionConstant.COUNTRY_NOT_FOUNT_MESSAGE;
import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
                .orElseThrow(() -> new NotFoundException(format(ExceptionConstant.COUNTRY_NOT_FOUND_CODE, request.getAmount(),
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

    public PageablePaymentResponse getAllPayment(int page, int count, PaymentCriteria criteria) {
        log.info("getAllPayment.started");

        Pageable pageable = PageRequest.of(page, count, Sort.by(DESC, "id"));

        Page<Payment> pageablePayments = paymentRepo.findAll(new PaymentSpecification(criteria), pageable);

        List<PaymentResponse> payments = pageablePayments.getContent()
                .stream()
                .map(PaymentMapper::mapEntityToResponse).collect(Collectors.toList());

        log.info("getAllPayment.success");

        return  PageablePaymentResponse.builder()
                .payments(payments)
                .hasNext(pageablePayments.hasNext())
                .totalElements(pageablePayments.getTotalElements())
                .totalPages(pageablePayments.getTotalPages())
                .build();

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
