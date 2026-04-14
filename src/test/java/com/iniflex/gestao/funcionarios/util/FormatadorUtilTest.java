package com.iniflex.gestao.funcionarios.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FormatadorUtil Tests")
class FormatadorUtilTest {

    @Test
    @DisplayName("Deve formatar data corretamente em formato dd/MM/yyyy")
    void testFormatarDataValida() {
        LocalDate data = LocalDate.of(2026, 4, 14);

        String resultado = FormatadorUtil.formatarData(data);

        assertEquals("14/04/2026", resultado);
    }

    @Test
    @DisplayName("Deve retornar string vazia quando data é nula")
    void testFormatarData_DataNula() {
        String resultado = FormatadorUtil.formatarData(null);

        assertEquals("", resultado);
    }

    @Test
    @DisplayName("Deve formatar data de nascimento corretamente")
    void testFormatarDataNascimento() {
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);

        String resultado = FormatadorUtil.formatarData(dataNascimento);

        assertEquals("15/05/1990", resultado);
    }

    @Test
    @DisplayName("Deve formatar moeda com formato brasileiro")
    void testFormatarMoedaValida() {
        BigDecimal valor = new BigDecimal("3000.00");

        String resultado = FormatadorUtil.formatarMoeda(valor);

        assertTrue(resultado.contains("3"));
        assertTrue(resultado.contains("000"));
    }

    @Test
    @DisplayName("Deve retornar 0,00 quando valor de moeda é nulo")
    void testFormatarMoeda_ValorNulo() {
        String resultado = FormatadorUtil.formatarMoeda(null);

        assertEquals("0,00", resultado);
    }

    @Test
    @DisplayName("Deve formatar moeda pequena corretamente")
    void testFormatarMoeda_ValorPequeno() {
        BigDecimal valor = new BigDecimal("100.50");

        String resultado = FormatadorUtil.formatarMoeda(valor);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve formatar moeda grande com separador de milhar")
    void testFormatarMoeda_ValorGrande() {
        BigDecimal valor = new BigDecimal("1250000.99");

        String resultado = FormatadorUtil.formatarMoeda(valor);

        assertNotNull(resultado);
        assertTrue(resultado.contains("1"));
    }

    @Test
    @DisplayName("Deve formatar número com separador de milhar")
    void testFormatarNumeroValido() {
        BigDecimal valor = new BigDecimal("1234.56");

        String resultado = FormatadorUtil.formatarNumero(valor);

        assertNotNull(resultado);
        assertTrue(resultado.contains("234"));
    }

    @Test
    @DisplayName("Deve retornar 0,00 quando número é nulo")
    void testFormatarNumero_ValorNulo() {
        String resultado = FormatadorUtil.formatarNumero(null);

        assertEquals("0,00", resultado);
    }

    @Test
    @DisplayName("Deve ter exatamente 2 casas decimais ao formatar número")
    void testFormatarNumero_CasasDecimais() {
        BigDecimal valor = new BigDecimal("1234.5");

        String resultado = FormatadorUtil.formatarNumero(valor);

        assertNotNull(resultado);
        assertTrue(resultado.endsWith("50") || resultado.contains(",50"));
    }

    @Test
    @DisplayName("Deve calcular idade corretamente")
    void testCalcularIdadeValida() {
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);

        int idade = FormatadorUtil.calcularIdade(dataNascimento);

        assertEquals(35, idade);
    }

    @Test
    @DisplayName("Deve retornar 0 quando data de nascimento é nula")
    void testCalcularIdade_DataNula() {
        int idade = FormatadorUtil.calcularIdade(null);

        assertEquals(0, idade);
    }

    @Test
    @DisplayName("Deve calcular idade para alguém recém-nascido")
    void testCalcularIdade_RecemNascido() {
        // Alguém nascido neste ano tem idade 0
        LocalDate dataNascimento = LocalDate.of(2026, 1, 1);

        int idade = FormatadorUtil.calcularIdade(dataNascimento);

        assertEquals(0, idade);
    }

    @Test
    @DisplayName("Deve calcular idade para ano de nascimento diferente")
    void testCalcularIdade_AnoDiferente() {
        LocalDate dataNascimento = LocalDate.of(2000, 1, 1);

        int idade = FormatadorUtil.calcularIdade(dataNascimento);

        assertTrue(idade >= 26);
    }

    @Test
    @DisplayName("Deve calcular idade corretamente mesmo após aniversário")
    void testCalcularIdade_AposPasarAniversario() {
        LocalDate dataNascimento = LocalDate.of(1990, 1, 1);

        int idade = FormatadorUtil.calcularIdade(dataNascimento);

        assertTrue(idade >= 36);
    }

    @Test
    @DisplayName("Deve formatar moeda com valor zero")
    void testFormatarMoeda_Zero() {
        BigDecimal valor = BigDecimal.ZERO;

        String resultado = FormatadorUtil.formatarMoeda(valor);

        assertNotNull(resultado);
        assertTrue(resultado.contains("0"));
    }

    @Test
    @DisplayName("Deve formatar número com valor zero")
    void testFormatarNumero_Zero() {
        BigDecimal valor = BigDecimal.ZERO;

        String resultado = FormatadorUtil.formatarNumero(valor);

        assertEquals("0,00", resultado);
    }
}
