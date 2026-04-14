package com.iniflex.gestao.funcionarios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.iniflex.gestao.funcionarios.model.Funcionario;
import com.iniflex.gestao.funcionarios.service.FuncionarioService;


@Component
public class Principal implements CommandLineRunner {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    private final FuncionarioService funcionarioService;

    public Principal(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("SISTEMA DE GESTAO DE FUNCIONARIOS - INIFLEX");
        System.out.println("=".repeat(120) + "\n");

        inserirFuncionarios();

        System.out.println("\n3.2 - Removendo funcionario...");
        remover();
        System.out.println("Funcionario removido com sucesso!\n");

        System.out.println("\n3.3 - Funcionarios cadastrados:");
        funcionarioService.imprimirTodosFuncionarios();

        System.out.println("\n3.4 - Aplicando aumento de 10% no salario...");
        funcionarioService.aplicarAumentoSalarial();
        System.out.println("Aumento aplicado com sucesso!\n");
        funcionarioService.imprimirTodosFuncionarios();

        System.out.println("\n3.5 e 3.6 - Funcionarios agrupados por funcao:");
        funcionarioService.imprimirFuncionariosPorFuncao();

        System.out.println("\n3.8 - Funcionarios que fazem aniversario em outubro e dezembro:");
        funcionarioService.imprimirAniversariantes();

        System.out.println("\n3.9 - Funcionario com maior idade:");
        funcionarioService.imprimirFuncionarioMaisVelho();

        System.out.println("\n3.10 - Funcionarios em ordem alfabetica:");
        funcionarioService.imprimirFuncionariosOrdenados();

        System.out.println("\n3.11 - Total de salarios:");
        funcionarioService.imprimirTotalSalarios();

        System.out.println("\n3.12 - Quantos salarios minimos cada funcionario ganha:");
        funcionarioService.imprimirSalariosMinimos(SALARIO_MINIMO);

        System.out.println("\n" + "=".repeat(120));
        System.out.println("FIM DO PROCESSAMENTO");
        System.out.println("=".repeat(120) + "\n");
    }

    private void inserirFuncionarios() {
        System.out.println("3.1 - Inserindo funcionarios...\n");

        Funcionario[] funcionarios = {
            new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
            new Funcionario("Joao", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
            new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
            new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
            new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
            new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
            new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
            new Funcionario("Heloisa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
            new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        };

        for (Funcionario funcionario : funcionarios) {
            funcionarioService.criarFuncionario(funcionario);
        }

        System.out.println("Total de " + funcionarios.length + " funcionarios inseridos com sucesso!\n");
    }

    private void remover() {
        List<Funcionario> todos = funcionarioService.buscarTodos();
        todos.stream()
                .filter(funcionario -> "Joao".equalsIgnoreCase(funcionario.getNome()))
                .findFirst()
                .ifPresent(funcionario -> funcionarioService.removerFuncionario(funcionario.getId()));
    }
}
