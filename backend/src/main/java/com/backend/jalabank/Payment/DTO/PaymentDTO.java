package com.backend.jalabank.Payment.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Integer accountNumber;              // Conta do destinatário
    private Integer agencyNumber;               // Número da agência
    private BigDecimal value;                   // Valor do pagamento
    private Integer originId;                      // Nome do Serviço
    private String code;                        // Código de barras para validação
}
