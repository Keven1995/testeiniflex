package com.iniflex.gestao.funcionarios.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Util para formatacao.
 */
public class FormatadorUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale LOCALE_BR = Locale.forLanguageTag("pt-BR");

    private FormatadorUtil() {
    }

    public static String formatarData(LocalDate data) {
        if (data == null) {
            return "";
        }
        return data.format(DATE_FORMATTER);
    }

    public static String formatarMoeda(BigDecimal valor) {
        return formatarNumero(valor);
    }

    public static String formatarNumero(BigDecimal valor) {
        if (valor == null) {
            return "0,00";
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(LOCALE_BR);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(valor);
    }

    public static int calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return 0;
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}
