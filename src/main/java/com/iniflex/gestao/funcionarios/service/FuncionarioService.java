package com.iniflex.gestao.funcionarios.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iniflex.gestao.funcionarios.model.Funcionario;
import com.iniflex.gestao.funcionarios.repository.FuncionarioRepository;
import com.iniflex.gestao.funcionarios.util.FormatadorUtil;

/**
 * Service.
 */
@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = Objects.requireNonNull(
                funcionarioRepository,
                "FuncionarioRepository nao pode ser nulo"
        );
    }

    public Funcionario criarFuncionario(Funcionario funcionario) {
        Objects.requireNonNull(funcionario, "Funcionario nao pode ser nulo");
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> buscarTodos() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        Objects.requireNonNull(id, "ID nao pode ser nulo");
        return funcionarioRepository.findById(id);
    }

    public void removerFuncionario(Long id) {
        Objects.requireNonNull(id, "ID nao pode ser nulo");
        funcionarioRepository.deleteById(id);
    }

    public Funcionario atualizarFuncionario(Funcionario funcionario) {
        Objects.requireNonNull(funcionario, "Funcionario nao pode ser nulo");
        return funcionarioRepository.save(funcionario);
    }

    public void aplicarAumentoSalarial() {
        List<Funcionario> funcionarios = buscarTodos();
        funcionarios.forEach(Funcionario::aumentarSalario);
        funcionarioRepository.saveAll(funcionarios);
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return buscarTodos().stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    public List<Funcionario> buscarAniversariantes(Integer... meses) {
        List<Funcionario> resultado = new ArrayList<>();
        for (Integer mes : meses) {
            resultado.addAll(funcionarioRepository.findByAniversarioMes(mes));
        }
        return resultado.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Funcionario> buscarFuncionarioMaisVelho() {
        return funcionarioRepository.findFuncionarioMaisVelho();
    }

    public List<Funcionario> buscarTodosOrdenados() {
        return funcionarioRepository.findAllByOrderByNomeAsc();
    }

    public BigDecimal calcularTotalSalarios() {
        return buscarTodos().stream()
                .map(Funcionario::getSalario)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void imprimirTodosFuncionarios() {
        List<Funcionario> funcionarios = buscarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }

        System.out.println("=".repeat(120));
        System.out.printf("%-20s %-25s %-15s %-30s%n", "Nome", "Data Nascimento", "Salario", "Funcao");
        System.out.println("=".repeat(120));

        funcionarios.forEach(funcionario -> System.out.printf(
                "%-20s %-25s %-15s %-30s%n",
                funcionario.getNome(),
                FormatadorUtil.formatarData(funcionario.getDataNascimento()),
                FormatadorUtil.formatarMoeda(funcionario.getSalario()),
                funcionario.getFuncao()
        ));

        System.out.println("=".repeat(120));
    }

    public void imprimirFuncionariosPorFuncao() {
        Map<String, List<Funcionario>> agrupados = agruparPorFuncao();
        if (agrupados.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.println("FUNCIONARIOS AGRUPADOS POR FUNCAO");
        System.out.println("=".repeat(120));

        agrupados.forEach((funcao, funcionarios) -> {
            System.out.println("\n--- " + funcao + " ---");
            funcionarios.forEach(funcionario -> System.out.printf(
                    "  - %s - %s - %s%n",
                    funcionario.getNome(),
                    FormatadorUtil.formatarData(funcionario.getDataNascimento()),
                    FormatadorUtil.formatarMoeda(funcionario.getSalario())
            ));
        });

        System.out.println("\n" + "=".repeat(120));
    }

    public void imprimirAniversariantes() {
        List<Funcionario> aniversariantes = buscarAniversariantes(10, 12);
        if (aniversariantes.isEmpty()) {
            System.out.println("\nNenhum funcionario faz aniversario em outubro ou dezembro.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("FUNCIONARIOS QUE FAZEM ANIVERSARIO EM OUTUBRO E DEZEMBRO");
        System.out.println("=".repeat(80));

        aniversariantes.forEach(funcionario -> System.out.printf(
                "  - %s - %s (Data: %s)%n",
                funcionario.getNome(),
                funcionario.getFuncao(),
                FormatadorUtil.formatarData(funcionario.getDataNascimento())
        ));

        System.out.println("=".repeat(80));
    }

    public void imprimirFuncionarioMaisVelho() {
        Optional<Funcionario> maisVelho = buscarFuncionarioMaisVelho();
        if (maisVelho.isEmpty()) {
            System.out.println("\nNenhum funcionario cadastrado.");
            return;
        }

        Funcionario funcionario = maisVelho.get();
        int idade = FormatadorUtil.calcularIdade(funcionario.getDataNascimento());

        System.out.println("\n" + "=".repeat(80));
        System.out.println("FUNCIONARIO COM MAIOR IDADE");
        System.out.println("=".repeat(80));
        System.out.println("  Nome: " + funcionario.getNome());
        System.out.println("  Idade: " + idade + " anos");
        System.out.println("=".repeat(80));
    }

    public void imprimirFuncionariosOrdenados() {
        List<Funcionario> funcionarios = buscarTodosOrdenados();
        if (funcionarios.isEmpty()) {
            System.out.println("\nNenhum funcionario cadastrado.");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println("FUNCIONARIOS ORDENADOS ALFABETICAMENTE");
        System.out.println("=".repeat(100));

        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario funcionario = funcionarios.get(i);
            System.out.printf(
                    "  %d. %s - %s - %s%n",
                    i + 1,
                    funcionario.getNome(),
                    funcionario.getFuncao(),
                    FormatadorUtil.formatarMoeda(funcionario.getSalario())
            );
        }

        System.out.println("=".repeat(100));
    }

    public void imprimirTotalSalarios() {
        BigDecimal total = calcularTotalSalarios();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("TOTAL DOS SALARIOS");
        System.out.println("=".repeat(80));
        System.out.println("  Total: " + FormatadorUtil.formatarMoeda(total));
        System.out.println("=".repeat(80));
    }

    public void imprimirSalariosMinimos(BigDecimal salarioMinimo) {
        List<Funcionario> funcionarios = buscarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("\nNenhum funcionario cadastrado.");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println(
                "QUANTOS SALARIOS MINIMOS CADA FUNCIONARIO GANHA (Salario Minimo: "
                        + FormatadorUtil.formatarNumero(salarioMinimo) + ")"
        );
        System.out.println("=".repeat(100));

        funcionarios.forEach(funcionario -> {
            BigDecimal quantidadeSalariosMinimos = funcionario.calcularSalariosMinimos(salarioMinimo);
            System.out.println(
                    "  - " + funcionario.getNome() + ": "
                            + FormatadorUtil.formatarNumero(quantidadeSalariosMinimos)
                            + " salarios minimos"
            );
        });

        System.out.println("=".repeat(100));
    }
}
