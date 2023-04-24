package az.company.mspayment.controller;

import az.company.mspayment.model.request.PaymentRequest;
import az.company.mspayment.model.response.PaymentResponse;
import az.company.mspayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/save")
    public void savePayment(@RequestBody PaymentRequest request){
        paymentService.savePayment(request);
    }


    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayment() {
        return ResponseEntity.ok(paymentService.getAllPayment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));

    }

    @PutMapping("/edit/{id}")
    public void updatePaymentById(@PathVariable("id") Long id, @RequestBody PaymentRequest request) {
        paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePaymentById(@PathVariable("id") Long id) {
        paymentService.deletePaymentById(id);
    }


}
