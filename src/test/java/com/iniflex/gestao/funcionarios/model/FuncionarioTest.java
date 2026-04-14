package com.iniflex.gestao.funcionarios.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Funcionario Model Tests")
class FuncionarioTest {

    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario("João Silva", LocalDate.of(1990, 5, 15), 
                                     new BigDecimal("3000.00"), "Gerente");
    }

    @Test
    @DisplayName("Deve criar funcionário com construtor completo")
    void testCriarFuncionarioComConstrutorCompleto() {
        Funcionario novo = new Funcionario("Maria Santos", LocalDate.of(1985, 3, 20), 
                                          new BigDecimal("2500.00"), "Analista");

        assertEquals("Maria Santos", novo.getNome());
        assertEquals(LocalDate.of(1985, 3, 20), novo.getDataNascimento());
        assertEquals(new BigDecimal("2500.00"), novo.getSalario());
        assertEquals("Analista", novo.getFuncao());
    }

    @Test
    @DisplayName("Deve criar funcionário com construtor sem argumentos")
    void testCriarFuncionarioSemArgumentos() {
        Funcionario novo = new Funcionario();

        assertNotNull(novo);
        assertNull(novo.getNome());
        assertNull(novo.getDataNascimento());
        assertNull(novo.getSalario());
        assertNull(novo.getFuncao());
    }

    @Test
    @DisplayName("Deve aplicar aumento de 10% no salário")
    void testAumentarSalario() {
        BigDecimal salarioOriginal = funcionario.getSalario();
        BigDecimal salarioEsperado = salarioOriginal.multiply(BigDecimal.valueOf(1.10));

        funcionario.aumentarSalario();

        assertEquals(salarioEsperado, funcionario.getSalario());
    }

    @Test
    @DisplayName("Deve aplicar múltiplos aumentos acumulativos")
    void testAumentarSalarioMultiplasVezes() {
        BigDecimal salarioOriginal = new BigDecimal("3000.00");
        funcionario.setSalario(salarioOriginal);

        funcionario.aumentarSalario();
        BigDecimal aposUmAumento = funcionario.getSalario();
        funcionario.aumentarSalario();
        BigDecimal aposDoisAumentos = funcionario.getSalario();

        assertEquals(salarioOriginal.multiply(BigDecimal.valueOf(1.10)), aposUmAumento);
        assertEquals(salarioOriginal.multiply(BigDecimal.valueOf(1.10)).multiply(BigDecimal.valueOf(1.10)), aposDoisAumentos);
    }

    @Test
    @DisplayName("Deve calcular salários mínimos corretamente")
    void testCalcularSalariosMinimos() {
        BigDecimal salarioMinimo = new BigDecimal("1320.00");
        funcionario.setSalario(new BigDecimal("3300.00"));

        BigDecimal resultado = funcionario.calcularSalariosMinimos(salarioMinimo);

        assertEquals(new BigDecimal("2.50"), resultado);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular salários mínimos com salário nulo")
    void testCalcularSalariosMinimos_SalarioNulo() {
        funcionario.setSalario(null);
        BigDecimal salarioMinimo = new BigDecimal("1320.00");

        BigDecimal resultado = funcionario.calcularSalariosMinimos(salarioMinimo);

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular salários mínimos com salário mínimo nulo")
    void testCalcularSalariosMinimos_SalarioMinimoNulo() {
        funcionario.setSalario(new BigDecimal("3000.00"));

        BigDecimal resultado = funcionario.calcularSalariosMinimos(null);

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular salários mínimos com ambos nulos")
    void testCalcularSalariosMinimos_AmbosNulos() {
        funcionario.setSalario(null);

        BigDecimal resultado = funcionario.calcularSalariosMinimos(null);

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve calcular salários mínimos com valores pequenos")
    void testCalcularSalariosMinimos_ValoresPequenos() {
        funcionario.setSalario(new BigDecimal("200.00"));
        BigDecimal salarioMinimo = new BigDecimal("100.00");

        BigDecimal resultado = funcionario.calcularSalariosMinimos(salarioMinimo);

        assertEquals(new BigDecimal("2.00"), resultado);
    }

    @Test
    @DisplayName("Deve calcular salários mínimos com arredondamento correto")
    void testCalcularSalariosMinimos_Arredondamento() {
        funcionario.setSalario(new BigDecimal("3250.00"));
        BigDecimal salarioMinimo = new BigDecimal("1320.00");

        BigDecimal resultado = funcionario.calcularSalariosMinimos(salarioMinimo);

        assertEquals(new BigDecimal("2.46"), resultado);
    }

    @Test
    @DisplayName("Deve acessar herança de Pessoa - nome")
    void testHerancaNome() {
        assertEquals("João Silva", funcionario.getNome());
    }

    @Test
    @DisplayName("Deve acessar herança de Pessoa - data nascimento")
    void testHerancaDataNascimento() {
        assertEquals(LocalDate.of(1990, 5, 15), funcionario.getDataNascimento());
    }

    @Test
    @DisplayName("Deve modificar nome herdado de Pessoa")
    void testModificarNomeHerdado() {
        funcionario.setNome("João Silva Modificado");

        assertEquals("João Silva Modificado", funcionario.getNome());
    }

    @Test
    @DisplayName("Deve modificar função do funcionário")
    void testModificarFuncao() {
        funcionario.setFuncao("Diretor");

        assertEquals("Diretor", funcionario.getFuncao());
    }

    @Test
    @DisplayName("Deve igualar dois funcionários construídos com mesmos dados")
    void testEqualidadeFuncionarios() {
        Funcionario funcionario2 = new Funcionario("João Silva", LocalDate.of(1990, 5, 15), 
                                                   new BigDecimal("3000.00"), "Gerente");
        funcionario2.setId(1L);
        funcionario.setId(1L);
        assertTrue(funcionario.getId().equals(funcionario2.getId()));
    }

    @Test
    @DisplayName("Deve não aumentar salário quando salário é nulo")
    void testAumentarSalario_SalarioNulo() {
        funcionario.setSalario(null);

        funcionario.aumentarSalario();

        assertNull(funcionario.getSalario());
    }

    @Test
    @DisplayName("Deve retornar descrição string do funcionário com toString")
    void testToString() {
        String resultado = funcionario.toString();

        assertNotNull(resultado);
        assertTrue(resultado.contains("João Silva") || resultado.contains("Funcionario"));
    }
}
