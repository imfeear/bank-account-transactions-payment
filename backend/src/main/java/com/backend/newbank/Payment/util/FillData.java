package com.backend.newbank.Payment.util;


import lombok.experimental.UtilityClass;


import java.math.BigDecimal;

import com.backend.newbank.Payment.DTO.PaymentDTO;

@UtilityClass
public class FillData {


    public static PaymentDTO fillData (String barcodeText) {
        PaymentDTO paymentData = new PaymentDTO();

        String barcodeData = DecodeBarcode.fromAsciiCode(barcodeText);

        // Dados devem estar formatados -> "accountNumber|agencyNumber|value|origin"
        String[] dataParts = barcodeData.split("\\|");

        if (dataParts.length != 4) {
            throw new RuntimeException("Invalid barcode format");
        }
        paymentData.setAccountNumber(Integer.parseInt(dataParts[0]));
        paymentData.setAgencyNumber(Integer.parseInt(dataParts[1]));
        paymentData.setValue(new BigDecimal(dataParts[2]));
        paymentData.setOriginId(Integer.parseInt(dataParts[3]));
        paymentData.setCode(barcodeText); // Fix barcode text estava passando o DTO para uma variável !


        return paymentData;
    }
}
