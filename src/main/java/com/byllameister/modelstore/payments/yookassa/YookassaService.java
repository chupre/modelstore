package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.*;
import com.byllameister.modelstore.sellers.Seller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class YookassaService implements PaymentGatewayService {
    private final YookassaConfig config;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentGatewayResponse createPayment(BigDecimal amount, String redirectUrl) throws PaymentResponseParseFailed {
        String idempotenceKey = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(config.getShopId(), config.getSecretKey());
        headers.set("Idempotence-Key", idempotenceKey);

        YookassaPaymentRequest request = YookassaPaymentRequest.builder()
                .amount(new YookassaAmount(amount.setScale(2, RoundingMode.HALF_UP).toString()))
                .confirmation(new YookassaPaymentRequest.Confirmation("redirect", redirectUrl))
                .capture(true)
                .build();

        HttpEntity<YookassaPaymentRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                config.getApiUrl() + "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return extractPaymentResponse(response.getBody());
    }

    @Override
    public PayoutGatewayResponse createPayout(BigDecimal amount, PayoutDestination payoutDestination) {
        String idempotenceKey = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(config.getPayoutGatewayId(), config.getPayoutGatewaySecretKey());
        headers.set("Idempotence-Key", idempotenceKey);

        YookassaPayoutRequest request = YookassaPayoutRequest.builder()
                .amount(new YookassaAmount(amount.setScale(2, RoundingMode.HALF_UP).toString()))
                .payout_destination_data(payoutDestination)
                .build();

        HttpEntity<YookassaPayoutRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                config.getApiUrl() + "/payouts",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return extractPayoutResponse(response.getBody());
    }

    @Override
    public PayoutDestination buildPayoutDestination(Seller seller) {
        var method = seller.getPayoutMethod();
        var destination = seller.getPayoutDestination();

        switch (method) {
            case BANK_CARD -> {
                return new YookassaBankCard(new YookassaBankCard.BankCard(destination));
            }
            case YOOMONEY_WALLET -> {
                return new YookassaWallet(destination);
            }
            default -> {
                return null;
            }
        }
    }

    private PayoutGatewayResponse extractPayoutResponse(String body) throws PaymentResponseParseFailed {
        try {
            JsonNode root = objectMapper.readTree(body);
            String paymentId = root.path("id").asText();
            return new PayoutGatewayResponse(paymentId);
        } catch (Exception e) {
            throw new PaymentResponseParseFailed("Failed to parse YooKassa response", e);
        }
    }

    private PaymentGatewayResponse extractPaymentResponse(String body) throws PaymentResponseParseFailed {
        try {
            JsonNode root = objectMapper.readTree(body);
            String paymentId = root.path("id").asText();
            String confirmationUrl = root.path("confirmation").path("confirmation_url").asText();
            return new PaymentGatewayResponse(paymentId, confirmationUrl);
        } catch (Exception e) {
            throw new PaymentResponseParseFailed("Failed to parse YooKassa response", e);
        }
    }
}
