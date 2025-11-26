package com.backend.jalabank.Payment.util;

import lombok.experimental.UtilityClass;


@UtilityClass
public class BarcodeGenerator {

    public String generateBarcode (String accountNumber, String agencyNumber, String value, Integer idOrigin){
        String infoCode = accountNumber+"|"+ agencyNumber+"|"+ value+"|"+ idOrigin;
        String barcode = asciiCode(infoCode);

        return barcode;
    }

    private static String asciiCode(String input) {
        StringBuilder asciiCode = new StringBuilder();
        for (char c : input.toCharArray()) {
            asciiCode.append(String.format("%03d", (int) c)); // Converte para ASCII com padding de 3 dígitos
        }
        System.out.println(asciiCode);

        return asciiCode.toString();
    }
}
