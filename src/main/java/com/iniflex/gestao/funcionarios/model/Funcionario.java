package com.iniflex.gestao.funcionarios.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Classe Funcionário.
 */
@Entity
@Table(name = "funcionario")
public class Funcionario extends Pessoa {

    @Column(precision = 19, scale = 2)
    private BigDecimal salario;

    @Column(length = 100)
    private String funcao;

    public Funcionario() {
    }

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    /**
     * Aplica aumento de 10% no salário.
     */
    public void aumentarSalario() {
        if (this.salario != null) {
            this.salario = this.salario.multiply(BigDecimal.valueOf(1.10));
        }
    }

    /**
     * Calcula quantos salários mínimos o funcionário ganha.
     * @param salarioMinimo valor do salário mínimo
     * @return quantidade de salários mínimos
     */
    public BigDecimal calcularSalariosMinimos(BigDecimal salarioMinimo) {
        if (this.salario == null || salarioMinimo == null) {
            return BigDecimal.ZERO;
        }
        return this.salario.divide(salarioMinimo, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}
