package com.backend.jalabank.Payment.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DecodeBarcode {

    public static String fromAsciiCode(String encoded) {
        StringBuilder originalText = new StringBuilder();
        for (int i = 0; i < encoded.length(); i += 3) {
            String charCode = encoded.substring(i, i + 3); // Extrai 3 dígitos por caractere
            char c = (char) Integer.parseInt(charCode); // Converte o código de volta para caractere
            originalText.append(c);
        }
        System.out.println(originalText);

        return originalText.toString();
    }
}
