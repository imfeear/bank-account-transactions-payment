package com.backend.jalabank.Payment.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.backend.newbank.Payment.util.DecodeBarcode;

import static org.junit.jupiter.api.Assertions.*;

class DecodeBarcodeTest {

    @Test
    void testFromAsciiCodeWithValidBarcode() {
        String validBarcode = "072101108108111032087111114108100";
        String expectedOutput = "Hello World";

        String result = DecodeBarcode.fromAsciiCode(validBarcode);

        assertEquals(expectedOutput, result, "Decodificação incorreta para código ASCII válido.");
    }

    @Test
    void testFromAsciiCodeWithInvalidLength() {
        String invalidBarcode = "07210110810811103208711111410"; // Não é múltiplo de 3

        Executable executable = () -> DecodeBarcode.fromAsciiCode(invalidBarcode);

        assertThrows(StringIndexOutOfBoundsException.class, executable, "Era esperada uma exceção de comprimento inválido.");
    }

    @Test
    void testFromAsciiCodeWithNonNumericCharacters() {
        String nonNumericBarcode = "072101A081"; // Caracteres não numéricos

        Executable executable = () -> DecodeBarcode.fromAsciiCode(nonNumericBarcode);

        assertThrows(NumberFormatException.class, executable, "Era esperada uma exceção de número inválido para caracteres não numéricos.");
    }

    @Test
    void testFromAsciiCodeWithOutOfRangeValues() {
        String outOfRangeBarcode = "256100100"; // Valor 256 excede o limite do ASCII (0-255)

        Executable executable = () -> DecodeBarcode.fromAsciiCode(outOfRangeBarcode);

        assertThrows(NumberFormatException.class, executable, "Era esperada uma exceção de valor fora do intervalo ASCII.");
    }

    @Test
    void testFromAsciiCodeWithEmptyString() {
        String emptyBarcode = "";
        String expectedOutput = "";

        String result = DecodeBarcode.fromAsciiCode(emptyBarcode);

        assertEquals(expectedOutput, result, "A saída para uma string vazia deveria ser vazia.");
    }

    @Test
    void testFromAsciiCodeWithEdgeCaseCharacters() {
        String edgeCaseBarcode = "032065066067"; // " ABC" (ASCII para espaço e letras maiúsculas)
        String expectedOutput = " ABC";

        String result = DecodeBarcode.fromAsciiCode(edgeCaseBarcode);

        assertEquals(expectedOutput, result, "Decodificação incorreta para caracteres no limite de código ASCII.");
    }

    @Test
    void testFromAsciiCodeWithMaxAsciiValue() {
        String maxAsciiBarcode = "255"; // Caracter com valor ASCII 255
        String expectedOutput = String.valueOf((char) 255);

        String result = DecodeBarcode.fromAsciiCode(maxAsciiBarcode);

        assertEquals(expectedOutput, result, "Decodificação incorreta para o valor ASCII máximo.");
    }
}
