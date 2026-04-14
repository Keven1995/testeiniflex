package com.iniflex.gestao.funcionarios.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iniflex.gestao.funcionarios.model.Funcionario;
import com.iniflex.gestao.funcionarios.service.FuncionarioService;

@ExtendWith(MockitoExtension.class)
@DisplayName("FuncionarioController Tests")
class FuncionarioControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private FuncionarioController funcionarioController;

    private Funcionario funcionarioTeste1;
    private Funcionario funcionarioTeste2;

    @BeforeEach
    void setUp() {
        funcionarioTeste1 = new Funcionario("João Silva", LocalDate.of(1990, 5, 15), 
                                           new BigDecimal("3000.00"), "Gerente");
        funcionarioTeste1.setId(1L);

        funcionarioTeste2 = new Funcionario("Maria Santos", LocalDate.of(1985, 3, 20), 
                                           new BigDecimal("2500.00"), "Analista");
        funcionarioTeste2.setId(2L);
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void testListarTodos() {
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioTeste2);
        when(funcionarioService.buscarTodos()).thenReturn(funcionarios);

        ResponseEntity<List<Funcionario>> resposta = funcionarioController.listarTodos();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        List<Funcionario> body = Objects.requireNonNull(resposta.getBody());
        assertEquals(2, body.size());
        verify(funcionarioService, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há funcionários")
    void testListarTodos_Vazio() {
        when(funcionarioService.buscarTodos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Funcionario>> resposta = funcionarioController.listarTodos();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        List<Funcionario> body = Objects.requireNonNull(resposta.getBody());
        assertTrue(body.isEmpty());
        verify(funcionarioService, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID existente")
    void testBuscarPorId_Existente() {
        when(funcionarioService.buscarPorId(1L)).thenReturn(Optional.of(funcionarioTeste1));

        ResponseEntity<Funcionario> resposta = funcionarioController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Funcionario body = Objects.requireNonNull(resposta.getBody());
        assertEquals(funcionarioTeste1.getNome(), body.getNome());
        verify(funcionarioService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar funcionário por ID inexistente")
    void testBuscarPorId_Inexistente() {
        when(funcionarioService.buscarPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Funcionario> resposta = funcionarioController.buscarPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(funcionarioService, times(1)).buscarPorId(999L);
    }

    @Test
    @DisplayName("Deve criar um novo funcionário")
    void testCriar() {
        Funcionario novoFuncionario = new Funcionario("Pedro Costa", LocalDate.of(1988, 12, 5), 
                                                      new BigDecimal("2800.00"), "Técnico");
        when(funcionarioService.criarFuncionario(novoFuncionario)).thenReturn(novoFuncionario);

        ResponseEntity<Funcionario> resposta = funcionarioController.criar(novoFuncionario);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Funcionario body = Objects.requireNonNull(resposta.getBody());
        assertEquals(novoFuncionario.getNome(), body.getNome());
        verify(funcionarioService, times(1)).criarFuncionario(novoFuncionario);
    }

    @Test
    @DisplayName("Deve atualizar funcionário existente")
    void testAtualizar_Existente() {
        Funcionario funcionarioAtualizado = new Funcionario("João Silva Atualizado", LocalDate.of(1990, 5, 15), 
                                                            new BigDecimal("3500.00"), "Gerente Sênior");
        funcionarioAtualizado.setId(1L);

        when(funcionarioService.buscarPorId(1L)).thenReturn(Optional.of(funcionarioTeste1));
        when(funcionarioService.atualizarFuncionario(funcionarioAtualizado)).thenReturn(funcionarioAtualizado);

        ResponseEntity<Funcionario> resposta = funcionarioController.atualizar(1L, funcionarioAtualizado);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Funcionario body = Objects.requireNonNull(resposta.getBody());
        assertEquals(new BigDecimal("3500.00"), body.getSalario());
        verify(funcionarioService, times(1)).buscarPorId(1L);
        verify(funcionarioService, times(1)).atualizarFuncionario(any(Funcionario.class));
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar funcionário inexistente")
    void testAtualizar_Inexistente() {
        when(funcionarioService.buscarPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Funcionario> resposta = funcionarioController.atualizar(999L, funcionarioTeste1);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(funcionarioService, times(1)).buscarPorId(999L);
        verify(funcionarioService, never()).atualizarFuncionario(any());
    }

    @Test
    @DisplayName("Deve deletar funcionário existente")
    void testDeletar_Existente() {
        when(funcionarioService.buscarPorId(1L)).thenReturn(Optional.of(funcionarioTeste1));

        ResponseEntity<Void> resposta = funcionarioController.deletar(1L);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(funcionarioService, times(1)).buscarPorId(1L);
        verify(funcionarioService, times(1)).removerFuncionario(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao deletar funcionário inexistente")
    void testDeletar_Inexistente() {
        when(funcionarioService.buscarPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Void> resposta = funcionarioController.deletar(999L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(funcionarioService, times(1)).buscarPorId(999L);
        verify(funcionarioService, never()).removerFuncionario(any());
    }

    @Test
    @DisplayName("Deve aplicar aumento de 10% em todos os salários")
    void testAumentarSalario() {
        doNothing().when(funcionarioService).aplicarAumentoSalarial();

        ResponseEntity<String> resposta = funcionarioController.aumentarSalario();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        String body = Objects.requireNonNull(resposta.getBody());
        assertTrue(body.contains("sucesso"));
        verify(funcionarioService, times(1)).aplicarAumentoSalarial();
    }

    @Test
    @DisplayName("Deve agrupar funcionários por função")
    void testAgruparPorFuncao() {
        Map<String, List<Funcionario>> mapa = new HashMap<>();
        mapa.put("Gerente", Collections.singletonList(funcionarioTeste1));
        mapa.put("Analista", Collections.singletonList(funcionarioTeste2));
        when(funcionarioService.agruparPorFuncao()).thenReturn(mapa);

        ResponseEntity<Map<String, List<Funcionario>>> resposta = funcionarioController.agruparPorFuncao();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Map<String, List<Funcionario>> body = Objects.requireNonNull(resposta.getBody());
        assertEquals(2, body.size());
        assertTrue(body.containsKey("Gerente"));
        assertTrue(body.containsKey("Analista"));
        verify(funcionarioService, times(1)).agruparPorFuncao();
    }

    @Test
    @DisplayName("Deve buscar funcionários ordenados por nome")
    void testBuscarOrdenados() {
        List<Funcionario> funcionariosOrdenados = Arrays.asList(funcionarioTeste2, funcionarioTeste1);
        when(funcionarioService.buscarTodosOrdenados()).thenReturn(funcionariosOrdenados);

        ResponseEntity<List<Funcionario>> resposta = funcionarioController.buscarOrdenados();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        List<Funcionario> body = Objects.requireNonNull(resposta.getBody());
        assertEquals(2, body.size());
        verify(funcionarioService, times(1)).buscarTodosOrdenados();
    }

    @Test
    @DisplayName("Deve buscar funcionário mais velho")
    void testBuscarMaisVelho_Existe() {
        when(funcionarioService.buscarFuncionarioMaisVelho()).thenReturn(Optional.of(funcionarioTeste2));

        ResponseEntity<Funcionario> resposta = funcionarioController.buscarMaisVelho();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Funcionario body = Objects.requireNonNull(resposta.getBody());
        assertEquals(funcionarioTeste2.getNome(), body.getNome());
        verify(funcionarioService, times(1)).buscarFuncionarioMaisVelho();
    }

    @Test
    @DisplayName("Deve retornar 404 quando não há funcionários (mais velho)")
    void testBuscarMaisVelho_Vazio() {
        when(funcionarioService.buscarFuncionarioMaisVelho()).thenReturn(Optional.empty());

        ResponseEntity<Funcionario> resposta = funcionarioController.buscarMaisVelho();

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(funcionarioService, times(1)).buscarFuncionarioMaisVelho();
    }

    @Test
    @DisplayName("Deve buscar aniversariantes")
    void testBuscarAniversariantes() {
        List<Funcionario> aniversariantes = Collections.singletonList(funcionarioTeste2);
        when(funcionarioService.buscarAniversariantes(10, 12)).thenReturn(aniversariantes);

        ResponseEntity<List<Funcionario>> resposta = funcionarioController.buscarAniversariantes();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        List<Funcionario> body = Objects.requireNonNull(resposta.getBody());
        assertEquals(1, body.size());
        verify(funcionarioService, times(1)).buscarAniversariantes(10, 12);
    }

    @Test
    @DisplayName("Deve retornar total de salários")
    void testTotalSalarios() {
        BigDecimal total = new BigDecimal("5500.00");
        when(funcionarioService.calcularTotalSalarios()).thenReturn(total);

        ResponseEntity<Map<String, BigDecimal>> resposta = funcionarioController.totalSalarios();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Map<String, BigDecimal> body = Objects.requireNonNull(resposta.getBody());
        assertTrue(body.containsKey("totalSalarios"));
        assertEquals(total, body.get("totalSalarios"));
        verify(funcionarioService, times(1)).calcularTotalSalarios();
    }
}
