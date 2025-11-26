package com.backend.newbank.Payment.config;

import com.aspose.barcode.generation.BarcodeGenerator;
import com.aspose.barcode.EncodeTypes;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class BarCode {
    private final int BAR_CODE_LENGTH = 22;

    public String generateBarcode(Long paymentId, BigDecimal value) {
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId não pode ser nulo");
        }
        // Verificar se 'value' é nulo
        if (value == null) {
            throw new IllegalArgumentException("value não pode ser nulo");
        }

        String barcode = codeGenerator(paymentId, value);

        BarcodeGenerator generator = new BarcodeGenerator(EncodeTypes.CODE_128, barcode);

        generator.getParameters().setResolution(400);
        try {
            generator.save(barcode + ".png");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return barcode;
    }



    private String zeroGenerator(int zeroQnt) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < zeroQnt; i++) {
            result.append("0");
        }
        return result.toString();
    }

    private String codeGenerator(Long paymentId, BigDecimal value) {
        String code, paymentHash, paymentValue, zeros;

        paymentHash = String.format("%010d", paymentId);
        paymentValue = value.toString().replace(".", "");

        int zerosNeeded = BAR_CODE_LENGTH - (paymentHash.length() + paymentValue.length());

        if (zerosNeeded < 0) {
            throw new IllegalArgumentException("Os dados excedem o comprimento máximo de 22 caracteres.");
        }

        zeros = zeroGenerator(zerosNeeded);

        code = paymentHash + zeros + paymentValue;
        return code;
    }

    HashMap<String, String> decodeBar(String barcode) {
        HashMap<String, String> teste = new HashMap<>();
        String paymentHash, paymentValue;

        paymentHash = barcode.substring(0, 10);
        paymentValue = barcode.substring(10);

        teste.put("hash", paymentHash);
        teste.put("value", paymentValue);

        return teste;
    }
}
