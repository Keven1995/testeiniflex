package com.iniflex.gestao.funcionarios.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iniflex.gestao.funcionarios.model.Funcionario;
import com.iniflex.gestao.funcionarios.service.FuncionarioService;

/**
 * Controller que expõe endpoints REST para operações com Funcionários.
 */
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    /**
     * GET - Retorna todos os funcionários.
     */
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.buscarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    /**
     * GET - Retorna um funcionário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        return funcionario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST - Cria um novo funcionário.
     */
    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody Funcionario funcionario) {
        Funcionario salvo = funcionarioService.criarFuncionario(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * PUT - Atualiza um funcionário existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id,
                                                  @RequestBody Funcionario funcionario) {
        Optional<Funcionario> existente = funcionarioService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        funcionario.setId(id);
        Funcionario atualizado = funcionarioService.atualizarFuncionario(funcionario);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * DELETE - Remove um funcionário.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        if (funcionario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        funcionarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST - Aplica aumento de 10% para todos os funcionários.
     */
    @PostMapping("/aumentar-salario")
    public ResponseEntity<String> aumentarSalario() {
        funcionarioService.aplicarAumentoSalarial();
        return ResponseEntity.ok("Aumento de 10% aplicado com sucesso.");
    }

    /**
     * GET - Agrupa funcionários por função.
     */
    @GetMapping("/agrupar-por-funcao")
    public ResponseEntity<Map<String, List<Funcionario>>> agruparPorFuncao() {
        Map<String, List<Funcionario>> agrupados = funcionarioService.agruparPorFuncao();
        return ResponseEntity.ok(agrupados);
    }

    /**
     * GET - Busca funcionários ordenados por nome.
     */
    @GetMapping("/ordenados")
    public ResponseEntity<List<Funcionario>> buscarOrdenados() {
        List<Funcionario> funcionarios = funcionarioService.buscarTodosOrdenados();
        return ResponseEntity.ok(funcionarios);
    }

    /**
     * GET - Busca o funcionário mais velho.
     */
    @GetMapping("/mais-velho")
    public ResponseEntity<Funcionario> buscarMaisVelho() {
        Optional<Funcionario> maisVelho = funcionarioService.buscarFuncionarioMaisVelho();
        return maisVelho.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET - Busca funcionários que fazem aniversário em abril e maio.
     */
    @GetMapping("/aniversariantes")
    public ResponseEntity<List<Funcionario>> buscarAniversariantes() {
        List<Funcionario> aniversariantes = funcionarioService.buscarAniversariantes(10, 12);
        return ResponseEntity.ok(aniversariantes);
    }

    /**
     * GET - Retorna o total de salários.
     */
    @GetMapping("/total-salarios")
    public ResponseEntity<Map<String, BigDecimal>> totalSalarios() {
        BigDecimal total = funcionarioService.calcularTotalSalarios();
        return ResponseEntity.ok(Map.of("totalSalarios", total));
    }
}
