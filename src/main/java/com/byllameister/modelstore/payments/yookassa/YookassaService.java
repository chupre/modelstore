package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.Currency;
import com.byllameister.modelstore.payments.PaymentGatewayService;
import com.byllameister.modelstore.payments.PaymentResponse;
import com.byllameister.modelstore.payments.PaymentResponseParseFailed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class YookassaService implements PaymentGatewayService {
    private final YookassaConfig config;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentResponse createPayment(BigDecimal amount, Currency currency, String redirectUrl) throws PaymentResponseParseFailed {
        String idempotenceKey = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(config.getShopId(), config.getSecretKey());
        headers.set("Idempotence-Key", idempotenceKey);

        YookassaPaymentRequest request = YookassaPaymentRequest.builder()
                .amount(new YookassaPaymentRequest.Amount(amount.toString(), currency))
                .confirmation(new YookassaPaymentRequest.Confirmation("redirect", redirectUrl))
                .capture(true)
                .build();

        HttpEntity<YookassaPaymentRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                config.getApiUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return extractPaymentResponse(response.getBody());
    }

    private PaymentResponse extractPaymentResponse(String body) throws PaymentResponseParseFailed {
        try {
            JsonNode root = objectMapper.readTree(body);
            String paymentId = root.path("id").asText();
            String confirmationUrl = root.path("confirmation").path("confirmation_url").asText();
            return new PaymentResponse(paymentId, confirmationUrl);
        } catch (Exception e) {
            throw new PaymentResponseParseFailed("Failed to parse YooKassa response", e);
        }
    }
}
